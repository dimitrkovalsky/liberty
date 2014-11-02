package com.liberty.executors

import java.io.{InputStreamReader, BufferedReader, File}

import com.liberty.executor.{CommandExecutor, MavenExecutor}
import org.junit.Test

class MavenExecutorTest {
  @Test
  def test(): Unit = {
    val mvnExec: MavenExecutor = new MavenExecutor
    mvnExec.create()
  }
}
