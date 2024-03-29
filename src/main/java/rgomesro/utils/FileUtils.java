package rgomesro.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    /**
     * @param filename Name of the to read
     * @return Full text content
     */
    public static List<String> readFile(String filename){
        try {
            return Files.readAllLines(Path.of(filename));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return new ArrayList<>();
    }

    /**
     * @param filename Name of the file in which to store the text
     * @param text Text to store in the file
     */
    public static void writeToFile(String filename, final String text){
        try {
            File file = new File(Path.of(filename).toString());
            file.getParentFile().mkdirs(); //Create directory if it does not exist
            Files.writeString(
                    Path.of(filename),
                    text + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Error while writing to " + filename);
        }
    }

    /**
     * @param filename Filename whose existence we want to check
     * @return True if the file exists
     */
    public static boolean fileExists(String filename){
        File f = new File(filename);
        return f.exists() && !f.isDirectory();
    }

    /**
     * @param filename Filename to delete
     */
    public static void fileDelete(String filename){
        if (fileExists(filename))
            new File(filename).delete();
    }
}
