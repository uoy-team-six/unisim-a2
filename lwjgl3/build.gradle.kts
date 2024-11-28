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

tasks.jar {
    archiveBaseName.set("unisim-a2-lwjgl3-all") 
    archiveVersion.set("1.0.0")    
    destinationDirectory.set(file("$buildDir/libs")) 
}
