package com.liberty.executor

import com.liberty.entities.MavenEntity

/**
 * User: Maxxis
 * Date: 28.10.13
 * Time: 9:28
 */
class MavenExecutor(me:MavenEntity) {
    var mavenEntity: Option[MavenEntity] = Some(me)
    def create(): Boolean = {
        val result = mavenEntity match {
            case Some(me: MavenEntity) => if (!me.path.isEmpty && !me.groupId.isEmpty && !me.projectName.isEmpty) {
                val command: String = "mvn archetype:generate -DgroupId=" + me.groupId + " -DartifactId=" + me.projectName + " -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false"
                CommandExecutor.execute(command, me.path)
                true
            } else {
                false
            }
        }
        result
    }

    def clean() {
        val command: String = "mvn clean"
        mavenEntity.map {
            me: MavenEntity => CommandExecutor.execute(command, me.path)
        }
    }

    def build() {
        val command: String = "mvn package";
        mavenEntity.map {
            me: MavenEntity => CommandExecutor.execute(command, me.path)
        }
    }

    def install() {
        val command: String = "mvn install"
        mavenEntity.map {
            me: MavenEntity => CommandExecutor.execute(command, me.path)
        }
    }

    def runTest() {
        val command: String = "mvn test";
        mavenEntity.map {
            me: MavenEntity => CommandExecutor.execute(command, me.path)
        }
    }
}