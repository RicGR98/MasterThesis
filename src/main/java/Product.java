import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Product extends Entity{
    public enum Type {A, B, C, D, E}

    private final Type type;
    private final Agent producer;
    private final float price;
    private int stock;

    public Product(Agent agent, Type type, float price) {
        super("Product");
        this.producer = agent;
        this.type = type;
        this.price = price;
        this.stock = 0;
    }

    public Product(Agent agent, float price){
        this(agent, getRandomType(), price);
    }

    public Product(Agent agent){
        this(agent, getRandomType(), Utils.getRandomFloat(Constants.Product.MIN_PRICE, Constants.Product.MAX_PRICE));
    }

    public Agent getProducer() {
        return producer;
    }

    public Type getType() {
        return type;
    }

    public float getPrice() {
        return price;
    }

    public int getStock(){
        return stock;
    }

    public void incrementStock(){
        stock++;
    }

    public void decrementStock(){
        stock--;
    }

    public static List<Type> getTypes(){
        return Arrays.asList(Type.values());
    }

    public static Type getRandomType(){
        return Utils.randomChoice(getTypes());
    }

    public Stream<String> csvFields(){
        return Stream.of(getName());
    }
}
