import java.net.URL

plugins {
    java
    checkstyle
    jacoco
}

dependencies {
    val gdxVersion: String by project
    implementation("com.badlogicgames.gdx:gdx-backend-headless:$gdxVersion")
    implementation(project(":core"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    checkstyle("com.puppycrawl.tools:checkstyle:10.12.1")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
    val classDirs = files(
        "${buildDir}/classes/java/main",
        "${buildDir}/classes/java/test"
    )
    val sourceDirs = files(
        "${projectDir}/src/main/java",
        "${projectDir}/src/test/java"
    )
    classDirectories.setFrom(classDirs)
    sourceDirectories.setFrom(sourceDirs)
    executionData.setFrom(fileTree(buildDir).include("**/jacoco/test.exec"))
}

tasks.register("downloadGoogleStyle") {
    val outputFile = file("$buildDir/checkstyle/google_checks.xml")
    outputs.file(outputFile)
    doLast {
        outputFile.parentFile.mkdirs()
        val url = "https://raw.githubusercontent.com/checkstyle/checkstyle/checkstyle-10.12.1/src/main/resources/google_checks.xml"
        val content = URL(url).readText()
        val modifiedContent = content.replaceFirst(Regex("<!DOCTYPE.*?>"), "")
        outputFile.writeText(modifiedContent)
    }
}

checkstyle {
    toolVersion = "10.12.1"
    configFile = file("$buildDir/checkstyle/google_checks.xml")
}

tasks.withType<Checkstyle> {
    dependsOn("downloadGoogleStyle")
}
