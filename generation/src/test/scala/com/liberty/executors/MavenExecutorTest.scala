package com.liberty.executors

import java.io.{InputStreamReader, BufferedReader, File}

import com.liberty.executor.{CommandExecutor, MavenExecutor}
import com.liberty.model.xml.PomXml
import org.junit.Test

class MavenExecutorTest {

  @Test
  def test(): Unit ={
    testCreationProject()
    testCretionPom()
  }

  def testCreationProject(): Unit = {
    val mvnExec: MavenExecutor = new MavenExecutor
    mvnExec.create()
  }


  def testCretionPom(): Unit = {
    new PomXml().createXmlFile()
  }
}
