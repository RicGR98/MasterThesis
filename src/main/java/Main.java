public class Main {
    public static void main(String[] args){
        World world = new World(1001,10, 2000);
        long start = System.nanoTime();
        world.run();
        System.out.println((System.nanoTime()-start)/1000000000.0);
        world.saveToCsv();
    }
}
