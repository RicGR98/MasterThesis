package rgomesro.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JsonUtils {
    /**
     * @return Json Object with all pairs of (key, values) parameters
     */
    public static JSONObject read(String filename){
        try {
            FileReader reader = new FileReader(filename);
            return (JSONObject) new JSONParser().parse(reader);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * @param jsonObject Json Object from which to retrieve a JSONObject
     * @param key Key of the value to retrieve
     * @return JSONObject value of the key
     */
    public static JSONObject getJsonObject(JSONObject jsonObject, String key){
        var res = (JSONObject) jsonObject.get(key);
        assert (res != null);
        return res;
    }

    /**
     * @param jsonObject Json Object from which to retrieve an int
     * @param key Key of the value to retrieve
     * @return Int value of the key
     */
    public static int getInt(JSONObject jsonObject, String key){
        return ((Long) jsonObject.get(key)).intValue();
    }

    /**
     * @param jsonObject Json Object from which to retrieve a float
     * @param key Key of the value to retrieve
     * @return Float value of the key
     */
    public static float getFloat(JSONObject jsonObject, String key){
        return ((Double) jsonObject.get(key)).floatValue();
    }

    /**
     * @param jsonObject Json Object from which to retrieve a float
     * @param key Key of the value to retrieve
     * @return String value of the key
     */
    public static String getString(JSONObject jsonObject, String key){
        return jsonObject.get(key).toString();
    }
}
