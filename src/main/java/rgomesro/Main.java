package rgomesro;

import rgomesro.models.World;
import rgomesro.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<String> jsons = FileUtils.readFile("params/configs.txt");
        var start = System.currentTimeMillis();
        ArrayList<Thread> worlds = new ArrayList<>();
        for (String json : jsons) {
            Thread thread = new Thread(new World(json));
            worlds.add(thread);
        }
        for (Thread world : worlds) {
            world.start();
        }
        for (Thread world : worlds) {
            world.join();
        }
        System.out.println((System.currentTimeMillis()-start)/1000f);
    }
}
