import java.nio.file.Paths

dependencies {
    implementation("dev.hollowcube:minestom-ce:${project.property("minestom_version")}")
    implementation("net.kyori:adventure-text-minimessage:${project.property("mini_message_version")}")
    implementation("dev.hollowcube:polar:${project.property("polar_version")}")
    implementation(project(":core"))
}

tasks {
    withType(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class.java) {
        destinationDirectory.set(Paths.get("${project.property("archive_output_directory")}").toFile())
        archiveBaseName.set("${project.property("archive_base_name")}")
        archiveClassifier.set("${project.property("archive_classifier")}")
        archiveVersion.set("${project.property("archive_version")}")
        manifest {
            attributes(
                "Main-Class" to "me.pixlent.Main",
                "Multi-Release" to true
            )
        }
    }
}
