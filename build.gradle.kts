plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

subprojects {
    plugins.apply("java")
    plugins.apply("com.github.johnrengelman.shadow")

    group = "me.pixlent"
    version = "1.0"

    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:${project.property("lombok_version")}")
        annotationProcessor("org.projectlombok:lombok:${project.property("lombok_version")}")
        implementation("org.slf4j:slf4j-nop:2.0.9")
    }
}
