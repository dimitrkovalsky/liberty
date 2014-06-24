package liberty.loaders;

import java.io.*;
import java.util.List;

/**
 * User: dkovalskyi
 * Date: 16.07.13
 * Time: 15:21
 */
public class CommandExecutor implements Runnable {
    private ProcessBuilder processBuilder;
    private String[] commands;

    public void setCommand(String... command) throws IOException, InterruptedException {
        //processBuilder = new ProcessBuilder(command);
        //processBuilder.redirectErrorStream(true); // redirect ErrorStream into standard error stream
        commands = command;
    }

    @Override
    public void run() {
        try {
           /* Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }*/
            Runtime.getRuntime().exec(commands);
        } catch (Exception e) {
            System.err.println("[CommandExecutor] error");
        }

    }

    public void setCommand(List<String> command) throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true); // redirect ErrorStream into standard error stream
        Process process = processBuilder.start();

    }

    public Process getExecuteProcess(String... command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true); // redirect ErrorStream into standard error stream
        return processBuilder.start();
    }

    public InputStream getCommandOutputStream(String... command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true); // redirect ErrorStream into standard error stream
        Process process = processBuilder.start();

        InputStream inputStream = process.getInputStream();
        int exitVal = process.waitFor();
        if (exitVal == 0)
            return inputStream;
        else
            return null;
    }

    /**
     * Creates linux script file
     *
     * @param scriptName File name
     * @param body       Script text
     */
    public void createScript(String scriptName, List<String> body) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(scriptName)));
            for (String s : body)
                writer.println(s);
            writer.close();
        } catch (Exception ignored) {
        }
    }


}
