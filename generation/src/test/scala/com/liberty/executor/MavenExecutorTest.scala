package com.liberty.executor

import org.junit.{Assert, Test}
import com.liberty.executor.MavenExecutor
import com.liberty.entities.MavenEntity

/**
 * User: mkontarev
 * Date: 10/10/13
 * Time: 3:37 PM
 */
class MavenExecutorTest {

    def toBeOrNotToBe: Boolean = true

    @Test
    def test() {
        val me: MavenEntity = new MavenEntity(projectName = "test", groupId = "com.test", path = "C:\\")
        val mavenExecutor: MavenExecutor = new MavenExecutor(me)

        mavenExecutor.create()
        Assert.assertTrue(toBeOrNotToBe)
        mavenExecutor.clean()
        mavenExecutor.build()

    }
}

