package rgomesro;

import rgomesro.models.World;
import rgomesro.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<String> jsons = FileUtils.readFile("params/all.txt");

        ArrayList<Thread> worlds = new ArrayList<>();
        for (int i = 0; i < jsons.size(); i++) {
            Thread thread = new Thread(new World(i, jsons.get(i)));
            worlds.add(thread);
        }
        for (Thread world : worlds) {
            world.start();
        }
        for (Thread world : worlds) {
            world.join();
        }
    }
}
