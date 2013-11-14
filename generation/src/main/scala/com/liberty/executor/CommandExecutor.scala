package com.liberty.executor

import java.io.{InputStreamReader, BufferedReader, File}

/**
 * User: Maxxis
 * Date: 28.10.13
 * Time: 9:16
 */
object CommandExecutor {

    val WORKING_DIRECTORY: String = "C:\\"

    def execute(command: String) {
        startProcess(createBuilder(command, WORKING_DIRECTORY));
    }

    def execute(command: String, workingDirectory: String) {
        startProcess(createBuilder(command, workingDirectory));
    }

    private def createBuilder(command: String, workingDirectory: String): ProcessBuilder = {
        val builder: ProcessBuilder = new ProcessBuilder()

        builder.directory(new File(workingDirectory))
        builder.command("cmd.exe", "/c", command)

        builder.redirectErrorStream(true)
    }

    private def startProcess(builder: ProcessBuilder) {
        val p: Process = builder.start()
        val r: BufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream))
        var line: String = ""

        while (line != null) {
            line = r.readLine()
            System.out.println(line)
        }
    }
}
