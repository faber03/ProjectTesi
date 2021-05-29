package autoscaler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;

public class AutoscalerLogger {

    private final String SEPARATOR = ";";
    private BufferedWriter fileWriter;

    public AutoscalerLogger(String filePath) throws IOException {

        this.fileWriter = Files.newBufferedWriter(Path.of(filePath));
        logHeader();
    }

    private void logHeader() throws IOException {
        fileWriter.write("timeStamp" + SEPARATOR +
                         "currentOldGem" + SEPARATOR +
                         "maxOldGen"+ SEPARATOR +
                         "minOldGen" + SEPARATOR +
                         "currentNumberOfTasks" + SEPARATOR +
                         "scalingMax" + SEPARATOR +
                         "scalingMin\n");
        fileWriter.flush();
    }

    public void log(long currentOldGem, long maxOldGen, long minOldGen, int currentNumberOfTasks, int scalingMax, int scalingMin) throws IOException {
        fileWriter.write(java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + SEPARATOR +
                currentOldGem + SEPARATOR +
                maxOldGen+ SEPARATOR +
                minOldGen + SEPARATOR +
                currentNumberOfTasks + SEPARATOR +
                scalingMax + SEPARATOR +
                scalingMin + "\n");
        fileWriter.flush();
    }

}
