package rgomesro.utils;

import java.util.List;
import java.util.Random;

public class RandomUtils {
    private static final Random gen = new Random(0); // System.currentTimeMillis()

    /**
     * @param min Minimum value
     * @param max Maximum value
     * @return Random integer in [min, max[
     */
    public static int getInt(int min, int max){
        return gen.nextInt(max - min) + min;
    }

    /**
     * @param min Minimum value
     * @param max Maximum value
     * @return Random float in [min, max[
     */
    public static float getFloat(float min, float max){
        return gen.nextFloat() * (max - min) + min;
    }

    /**
     * @return Random float in [0, 1[
     */
    public static float getRandom(){
        return getFloat(0, 1);
    }

    /**
     * @return Random percentage value in [0, 100[
     */
    public static float getPercentage(){
        return getFloat(0, 100);
    }

    /**
     * @param possibilities List of possibilities
     * @param <T> Type of the possibilities
     * @return Random object in the list of possibilities
     */
    public static <T> T choose(List<T> possibilities){
        return possibilities.get(getInt(0, possibilities.size()));
    }
}
