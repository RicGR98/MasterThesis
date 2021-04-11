package rgomesro.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    /**
     * @param filename Name of the file in which to store the text
     * @param text Text to store in the file
     */
    public static void writeToFile(String filename, String text){
        try {
            PrintWriter writer = new PrintWriter(filename, StandardCharsets.UTF_8);
            writer.println(text);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error while writing to " + filename);
        }
    }
}
