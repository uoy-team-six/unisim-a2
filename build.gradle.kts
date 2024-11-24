group = "io.github.unisim"
version = "1.0"

subprojects {
    apply(plugin = "java-library")

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.release = 17
    }

    tasks.withType<Jar> {
        archiveBaseName.set("${rootProject.name}-${project.name}")
    }
}
