// TODO: Use jacoco plugin to produce test coverage report

dependencies {
    val gdxVersion: String by project
    implementation("com.badlogicgames.gdx:gdx-backend-headless:$gdxVersion")
    implementation(project(":core"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}


plugins {
    jacoco
}


tasks.test {
    finalizedBy(tasks.jacocoTestReport) 
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) 
}

tasks.jar {
    archiveBaseName.set("builds") 
    archiveVersion.set("1.0.0")    
    destinationDirectory.set(file("$buildDir/libs")) 
}


