plugins {
    `java`
    checkstyle
}

group = "io.github.unisim"
version = "2.0"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "checkstyle")

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
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
