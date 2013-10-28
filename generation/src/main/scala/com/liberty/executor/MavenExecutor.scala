package com.liberty.executor

/**
 * User: Maxxis
 * Date: 28.10.13
 * Time: 9:28
 */
class MavenExecutor {

  val executor: CommandExecutor = new CommandExecutor();

  def create(groupId: String, projectName: String) {
    val command: String = "mvn archetype:generate -DgroupId=" + groupId + " -DartifactId=" + projectName + " -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false";
    executor.execute(command);
  }

  def clean() {
    val command: String = "mvn clean";
    executor.execute(command);
  }

  def build() {
    val command: String = "mvn package";
    executor.execute(command);
  }

  def install() {
    val command: String = "mvn install";
    executor.execute(command);
  }

  def run() {
    val command: String = "mvn clean";
    executor.execute(command);
  }

  def runTest() {
    val command: String = "mvn test";
    executor.execute(command);
  }

  def runPlugin() {
    val command: String = "mvn clean";
    executor.execute(command);
  }
}