public class Main {
    public static void main(String[] args){
        World world = new World(10, 100);
        world.run(1000);
        world.saveToCsv();
    }
}
