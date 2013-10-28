package com.liberty.executor

import org.junit.{Assert, Test}
import com.liberty.executor.MavenExecutor

/**
 * User: mkontarev
 * Date: 10/10/13
 * Time: 3:37 PM
 */
class MavenExecutorTest {

  def toBeOrNotToBe(): Boolean = {
    return true;
  }

  @Test
  def createArchetype() {
    val groupId = "com.test"
    val projectName = "test"
    val mavenExecutor : MavenExecutor = new MavenExecutor()

    mavenExecutor.create(groupId, projectName)
    Assert.assertTrue(toBeOrNotToBe());
  }
}

