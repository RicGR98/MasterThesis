package rgomesro.Models;

import rgomesro.Constants;
import rgomesro.Utils;

import java.util.stream.Stream;

public class Product extends Entity {
    private final Agent producer;
    private final int type;
    private final float price;
    private int stock;
    private int sold;

    public Product(Agent agent, int type, float price) {
        super();
        this.producer = agent;
        this.type = type;
        this.price = price;
        this.stock = 0;
        this.sold = 0;
    }

    public Product(Agent agent, float price){
        this(agent, Utils.getRandomInt(0, Constants.Product.NB_DIFF_PRODUCTS), price);
    }

    public Product(Agent agent){
        this(agent, Utils.getRandomInt(0, Constants.Product.NB_DIFF_PRODUCTS), Utils.getRandomFloat(Constants.Product.MIN_PRICE, Constants.Product.MAX_PRICE));
    }

    public Agent getProducer() {
        return producer;
    }

    public Integer getType() {
        return type;
    }

    public Float getPrice() {
        return price;
    }

    public int getStock(){
        return stock;
    }

    public void sell(){
        assert (stock > 0);
        stock--;
        sold++;
    }

    public void produce(){
        stock++;
    }

    public Stream<String> csvFields(){
        return Stream.of(getId());
    }
}
