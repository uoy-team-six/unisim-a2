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
