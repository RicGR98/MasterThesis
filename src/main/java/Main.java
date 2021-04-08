public class Main {
    public static void main(String[] args){
        World world = new World(10001, 10, 5000);
        long start = System.nanoTime();
        world.run();
        System.out.println((System.nanoTime()-start)/1000000000.0);
        world.saveToCsv();
    }
}
