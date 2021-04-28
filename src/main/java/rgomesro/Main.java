package rgomesro;

import rgomesro.models.World;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String[] jsons = {"small.json"};
        ArrayList<Thread> worlds = new ArrayList<>();
        for (int i = 0; i < jsons.length; i++) {
            Thread thread = new Thread(new World(i, jsons[i]));
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
