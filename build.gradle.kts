import java.net.URL

plugins {
    `java`
    checkstyle
    jacoco
}

group = "io.github.unisim"
version = "1.0"

repositories {
    mavenCentral()
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

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "checkstyle")
    apply(plugin = "jacoco")

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.release.set(17)
    }

    tasks.withType<Jar> {
        archiveBaseName.set("${rootProject.name}-${project.name}")
    }

    configure<CheckstyleExtension> {
        toolVersion = "10.12.1"
        configFile = rootProject.file("${rootProject.buildDir}/checkstyle/google_checks.xml")
    }

    tasks.withType<Checkstyle> {
        dependsOn(rootProject.tasks.named("downloadGoogleStyle"))
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        finalizedBy("jacocoTestReport")
    }

    tasks.named<JacocoReport>("jacocoTestReport") {
        dependsOn(tasks.named("test"))
    }
}
