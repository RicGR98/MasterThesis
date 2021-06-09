package rgomesro.utils;

import java.util.List;

public class MathUtils {
    /**
     * @param list List of elements of which we want to get the median
     * @param <T> Type of the elements in the list
     * @return Median value of the list
     */
    public static <T extends Number> float getMedian(List<T> list){
        float median;
        int size = list.size();
        if (size % 2 == 0)
            median = (list.get(size/2).floatValue() + list.get(size/2 - 1).floatValue())/2;
        else
            median = list.get(size/2).floatValue();
        return median;
    }

    /**
     * @param list List of elements of which we want to get the average
     * @param <T> Type of the elements in the list
     * @return Average value of the list
     */
    public static <T extends Number> float getAverage(List<T> list){
        float average = 0f;
        for (T elem: list){
            average += elem.floatValue();
        }
        average /= list.size();
        return average;
    }
}
