// Import statements must be at the very top in Kotlin DSL scripts
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

    checkstyle("com.puppycrawl.tools:checkstyle:10.12.1") // Use the latest version available
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
        URL("https://raw.githubusercontent.com/checkstyle/checkstyle/master/src/main/resources/google_checks.xml")
            .openStream().use { input: InputStream ->
                outputFile.outputStream().use { output: OutputStream ->
                    input.copyTo(output)
                }
            }
    }
}

checkstyle {
    toolVersion = "10.12.1" // Ensure this matches the version in dependencies
    configFile = file("$buildDir/checkstyle/google_checks.xml")
}

tasks.withType<Checkstyle> {
    dependsOn("downloadGoogleStyle")
}
