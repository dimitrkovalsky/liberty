package com.liberty.loaders

/**
 * User: dkovalskyi
 * Date: 16.07.13
 * Time: 15:29
 */
object SharpLoader {
  def load() {
    println("[SharpLoader]")
    val executor = new CommandExecutor
    executor.setCommand("C:\\sharp_startup.bat")
    new Thread(executor).start()
  }
}
