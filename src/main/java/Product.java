import java.util.Arrays;
import java.util.stream.Stream;

public class Product extends Entity{
    public enum Type {A, B, C, D, E}

    private final Type type;
    private final Agent producer;
    private final float price;

    public Product(Agent producer, Type type, float price) {
        super("Product");
        this.producer = producer;
        this.type = type;
        this.price = price;
    }

    public Product(Agent producer, float price){
        this(producer, getRandomType(), price);
    }

    public Product(Agent producer){
        this(producer, getRandomType(), Utils.getRandomFloat(Constants.Product.MIN_PRICE, Constants.Product.MAX_PRICE));
    }

    public Type getType() {
        return type;
    }

    public Agent getProducer() {
        return producer;
    }

    public float getPrice() {
        return price;
    }

    public static Type getRandomType(){
        return Utils.randomChoice(Arrays.asList(Type.values()));
    }

    public Stream<String> csvFields(){
        return Stream.of(getName());
    }
}
