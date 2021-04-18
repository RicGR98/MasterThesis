package rgomesro;

import rgomesro.models.World;

public class Main {
    public static void main(String[] args){
        long start = System.currentTimeMillis();
        World world = new World();
        world.run();
        world.saveAllToCsv();
        long end = System.currentTimeMillis();
        System.out.println((end - start)/1000 + " seconds");
    }
}
