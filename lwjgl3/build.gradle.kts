plugins {
    id("com.gradleup.shadow") version "8.3.3"
}

dependencies {
    val gdxVersion: String by project
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")
    implementation(project(":core"))
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "io.github.unisim.lwjgl3.Lwjgl3Launcher"
        )
    }
}

tasks.shadowJar {
    from("${rootProject.projectDir}/assets") {
        into("/")
    }
    minimize()
}
