package my.takealook.memento

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

const val GROUP_ID = "my.takealook.memento"

internal fun Project.configureMavenPublish() {
    extensions.configure<MavenPublishBaseExtension> {
        val artifactId = projectDir
            .toString()
            .split("/")
            .last()

        val version = libs.findVersion("memento")
            .get()
            .toString()

        coordinates(
            groupId = GROUP_ID,
            artifactId = artifactId,
            version = version
        )

        pom {
            name.set("Memento")
            description.set(
                "An Instagram-Story-Like Image editor"
            )
            url.set("https://github.com/takea-look/Memento")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://github.com/takea-look/Memento/blob/main/LICENSE.txt")
                }

                developers {
                    developer {
                        id.set("team-takealook")
                        name.set("Team Take a Look")
                        email.set("team@takealook.my")
                    }
                }

                scm {
                    url.set("https://github.com/takea-look/Memento.git")
                }
            }
        }
        publishToMavenCentral(true)
        signAllPublications()
    }
}