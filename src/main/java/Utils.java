import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Utils {
    /**
     * @param min Minimum value
     * @param max Maximum value
     * @return Random integer in [min, max[
     */
    public static int getRandomInt(int min, int max){
        return new Random().nextInt(max - min) + min;
    }

    /**
     * @param min Minimum value
     * @param max Maximum value
     * @return Random float in [min, max[
     */
    public static float getRandomFloat(float min, float max){
        return new Random().nextFloat() * (max - min) + min;
    }

    /**
     * @return Random float in [0, 1[
     */
    public static float getRandom(){
        return getRandomFloat(0, 1);
    }

    /**
     * @param possibilities List of possibilities
     * @param <T> Type of the possibilities
     * @return Random object in the list of possibilities
     */
    public static <T> T randomChoice(List<T> possibilities){
        return possibilities.get(getRandomInt(0, possibilities.size()));
    }

    /**
     * @param possibilities Array of possibilities
     * @param <T> Type of the possibilities
     * @return Random object in the list of possibilities
     */
    public static <T> T randomChoice(T[] possibilities){
        return possibilities[getRandomInt(0, possibilities.length)];
    }

    /**
     * @param possibilities List of possibilities
     * @param nbElems Number of random elements to retrieve
     * @param <T> Type of the possibilities
     * @return List of random object in the list of possibilities
     */
    public static <T> List<T> randomChoices(List<T> possibilities, int nbElems){
        Collections.shuffle(possibilities);
        return possibilities.subList(0, nbElems);
    }

    /**
     * @param possibilities List of possibilities
     * @param percentage Percentage of elements to retrieve form the list
     * @param <T> Type of the possibilities
     * @return List of random object in the list of possibilities
     */
    public static <T> List<T> randomChoicesPercentage(List<T> possibilities, float percentage){
        return randomChoices(possibilities, (int) (possibilities.size()*percentage));
    }

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
