package com.liberty.utils

import java.io.File
import org.junit.Assert
import scala.io.Source
import scala.reflect.io.Path

/**
 * User: dimitr
 * Date: 16.07.2014
 * Time: 14:30
 */
object FileChecker {
  /**
   * Current project location
   */
  val PROJECT_PATH = getProjectFolder


  private def getProjectFolder = {
    val path = new File(".").getAbsolutePath
    path.substring(0, path.length() - 1)
  }

  /**
   * Fails if directories are different. Checks file amount, file type and content.
   */
  def checkDirectories(expectedPath: String, availablePath: String) {
    val expected = recursiveListFiles(new File(expectedPath)).toList
    val available = recursiveListFiles(new File(availablePath)).toList
    checkFiles(expected, available)
  }

  /**
   * Fails if directories are the same
   */
  def checkDirectoriesFail(expectedPath: String, availablePath: String) {
    val expected = recursiveListFiles(new File(expectedPath)).toList
    val available = recursiveListFiles(new File(availablePath)).toList
    checkFilesFail(expected, available)
  }

  /**
   * Fails if files are different. Checks file amount, file type and content.
   */
  def checkFiles(expected: List[File], available: List[File]) {
    val eIterator = expected.iterator
    val aIterator = available.iterator
    if (expected.size != available.size) {
      Assert.assertEquals("File amount is different", expected.map(_.getName).mkString("\n"), available.map(_.getName).mkString("\n"))
    }

    while (eIterator.hasNext && aIterator.hasNext) {
      val exp = eIterator.next()
      val av = aIterator.next()
      Assert.assertEquals("File name are different", exp.getName, av.getName)
      if (exp.isFile && !av.isFile)
        Assert.fail(s"Available file : ${av.getCanonicalPath} is not file should be tha same as ${exp.getCanonicalPath}")
      if (exp.isFile) {
        Assert.assertEquals("File content are different", Source.fromFile(exp).mkString, Source.fromFile(av).mkString)
      }
    }
  }

  /**
   * Fails if files are the same
   */
  def checkFilesFail(expected: List[File], available: List[File]) {
    val eIterator = expected.iterator
    val aIterator = available.iterator
    if (expected.size != available.size) {
      return
    }

    while (eIterator.hasNext && aIterator.hasNext) {
      val exp = eIterator.next()
      val av = aIterator.next()
      if (exp.getName != av.getName)
        return
      if (exp.isFile && !av.isFile)
        return
      if (exp.isFile) {
        if (Source.fromFile(exp).mkString != Source.fromFile(av).mkString)
          return
      }
      Assert.fail("Files are equals")
    }
  }

  private def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }

  /**
   * Removes directory. Invoke only in the end of test because some files can be locked.
   */
  def removeDirectory(path: String) {
    System.gc() // Should invoke to remove some classes that lock some files for removing
    Assert.assertTrue("Folder cannot be deleted", Path(path).deleteRecursively())
  }
}