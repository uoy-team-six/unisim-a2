import java.net.URL
import java.io.InputStream
import java.io.OutputStream

plugins {
    `java`
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
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
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
