package rgomesro;

import rgomesro.models.World;

public class Main {
    public static void main(String[] args){
        long start = System.nanoTime();
        World world = new World();
        long startRun = System.nanoTime();
        world.run();
        long end = System.nanoTime();
        long denom = 1000000000;
        System.out.println((startRun - start)/denom + " + " + (end - startRun)/denom + " = " + (end - start)/denom + "s");
        world.saveAllToCsv();
    }
}
