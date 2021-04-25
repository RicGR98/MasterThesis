package rgomesro;

import rgomesro.models.World;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        ArrayList<Thread> worlds = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Thread thread = new Thread(new World(i));
            thread.start();
            worlds.add(thread);
        }
        for (Thread world : worlds) {
            world.join();
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start)/1000 + " seconds");
    }
}
