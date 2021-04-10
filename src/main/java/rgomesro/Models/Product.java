package rgomesro.Models;

import rgomesro.Constants;
import rgomesro.Utils;

import java.util.stream.Stream;

public class Product extends Entity {
    private final Agent producer;
    private final int type;
    private final float price;
    private int stock;

    public Product(Agent agent, int type, float price) {
        super();
        this.producer = agent;
        this.type = type;
        this.price = price;
        this.stock = 0;
    }

    public Product(Agent agent, float price){
        this(agent, Utils.getRandomInt(0, Constants.Product.NB_UNIQUE), price);
    }

    public Product(Agent agent){
        this(agent, Utils.getRandomInt(0, Constants.Product.NB_UNIQUE), Utils.getRandomFloat(Constants.Product.MIN_PRICE, Constants.Product.MAX_PRICE));
    }

    public Agent getProducer() {
        return producer;
    }

    public int getType() {
        return type;
    }

    public Float getPrice() {
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

    public Stream<String> csvFields(){
        return Stream.of(getName());
    }
}
