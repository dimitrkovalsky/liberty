/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import sbt.Keys._
import sbt._

object ApplicationBuild extends Build {
  // Target JVM version
  val SCALAC_JVM_VERSION = "jvm-1.7"
  val JAVAC_JVM_VERSION = "1.7"

  lazy val root = Project("liberty", file("."), settings = rootSettings) aggregate (allProjects: _*)
  lazy val recognition = Project("recognition", file("recognition"), settings = recognitionSettings).dependsOn(generation)
  lazy val generation = Project("generation", file("generation"), settings = generationSettings)
  lazy val ui = Project("ui", file("ui"), settings = uiSettings)

 // generation.dependsOn(generation)

  lazy val allProjects = Seq[ProjectReference](recognition, generation, ui)

  addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.0" cross CrossVersion.full)

  resolvers += "repo.codahale.com" at "http://repo.codahale.com"

  def sharedSettings = Defaults.defaultSettings ++ Seq(
      organization := "com.liberty",
      version := "0.1.0",
      scalaVersion := "2.11.1",


      // also check the local Maven repository ~/.m2
      resolvers ++= Seq (Resolver.file("Local Maven Repo", file(Path.userHome + "/.m2/repository"))),

      publishMavenStyle := true,

    libraryDependencies ++= Seq(
      "org.scalatest" % "scalatest_2.11" % "2.2.0" % "test",
      "com.gilt" % "jerkson_2.11" % "0.6.6",
      "org.scala-lang.modules" %% "scala-xml" % "1.0.2",
      "com.google.code.javaparser" % "javaparser" % "1.0.11",
      "com.typesafe" % "config" % "1.0.2",
      "com.typesafe.akka" % "akka-actor_2.11" % "2.3.6",
      "junit" % "junit" % "4.11",
      "org.apache.commons" % "commons-lang3" % "3.1",
      "org.scalafx" % "scalafx_2.10" % "1.0.0-M6",
      "com.miglayout" % "miglayout-javafx" % "4.2",
      "commons-lang" % "commons-lang" % "2.6",
      "org.slf4j" % "slf4j-api" % "1.6.1",
      "org.slf4j" % "jcl-over-slf4j" % "1.6.1",
      "org.slf4j" % "slf4j-log4j12" % "1.6.1",
      "log4j" % "log4j" % "1.2.16"
    ),

    parallelExecution := true
  )


  def rootSettings = sharedSettings ++ Seq(
    publish := {}
  )

  def recognitionSettings = sharedSettings ++ Seq(
    name := "liberty-recognition",
    unmanagedSourceDirectories in Compile <++= baseDirectory {
      base =>
        Seq(
          base / "../recognition/src/main/scala"
        )
    }
  )

  def generationSettings = sharedSettings ++ Seq(
    name := "liberty-generation",
    unmanagedSourceDirectories in Compile <++= baseDirectory {
      base =>
        Seq(
          base / "../generation/src/main/scala"
        )
    }
  )

  def uiSettings = sharedSettings ++ Seq(
    name := "liberty-ui",
    unmanagedSourceDirectories in Compile <++= baseDirectory {
      base =>
        Seq(
          base / "../ui/src/main/scala"
        )
    }
  )


}
