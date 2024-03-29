package rgomesro.utils;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class RandomUtils {
    private static final Random gen = new Random();

    /**
     * @param min Minimum value
     * @param max Maximum value
     * @return Random integer in [min, max[
     */
    public static int getInt(int min, int max){
        if (min == max) return min;
        return gen.nextInt(max - min) + min;
    }

    /**
     * @param min Minimum value
     * @param max Maximum value
     * @return Random float in [min, max[
     */
    public static float getFloat(float min, float max){
        if (min == max) return min;
        return gen.nextFloat() * (max - min) + min;
    }

    /**
     * @param prob Probability of being False
     * @return True/False according to certain probability prob
     */
    public static Boolean getBoolean(float prob){
        return getRandom() > prob;
    }

    /**
     * @return Random float in [0, 1[
     */
    public static float getRandom(){
        return getFloat(0, 1);
    }

    /**
     * @param possibilities List of possibilities
     * @param <T> Type of the possibilities
     * @return Random object in the list of possibilities
     */
    public static <T> T choose(List<T> possibilities){
        return possibilities.get(getInt(0, possibilities.size()));
    }

    /**
     * @param possibilities Set of possibilities
     * @param <T> Type of the possibilities
     * @return Random object in the set of possibilities
     */
    public static <T> T choose(Set<T> possibilities){
        return choose(new ArrayList<>(possibilities));
    }

    /**
     * @param possibilities Array of possibilities
     * @param <T> Type of the possibilities
     * @return Random object in the list of possibilities
     */
    public static <T> T choose(T[] possibilities){
        return possibilities[(getInt(0, possibilities.length))];
    }

    /**
     * @param possibilities List of possibilities with a weight for each of them
     * @param <T> Type of the possibilities
     * @return Random object in the list of possibilities according to the weights of the elements
     */
    public static <T> T weightedChoose(List<Pair<T, Double>> possibilities){
        return new EnumeratedDistribution<>(possibilities).sample();
    }
}
