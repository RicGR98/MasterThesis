package rgomesro.models.entities;

import rgomesro.utils.RandomUtils;

import java.util.stream.Stream;

import static rgomesro.Constants.Product.*;

/**
 * Represent a Product that can be produced, sold and bought by an Agent on the Market
 */
public class Product extends Entity {
    private final Agent producer;
    private final int type;
    private final float price;
    private int stock;
    private int sold;

    /* ==================================
     * ==== Constructors
     * ================================== */
    /**
     * @param agent Agent who produces this product
     * @param type Type of the product
     * @param price Price of the product (without taxes)
     */
    public Product(Agent agent, int type, float price) {
        super();
        this.producer = agent;
        this.type = type;
        this.price = price;
        this.stock = 0;
        this.sold = 0;
    }

    public Product(Agent agent, float price){
        this(agent, RandomUtils.getRandomInt(0, NB_DIFF_PRODUCTS), price);
    }

    public Product(Agent agent){
        this(agent, RandomUtils.getRandomInt(0, NB_DIFF_PRODUCTS), RandomUtils.getRandomFloat(MIN_PRICE, MAX_PRICE));
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    public Agent getProducer() {
        return producer;
    }

    public Integer getType() {
        return type;
    }

    public Float getPrice() {
        return price;
    }

    public Integer getStock(){
        return stock;
    }

    public Integer getSold(){
        return sold;
    }

    /* ==================================
     * === Methods: csv
     * ================================== */
    public String csvHeader() {
        return "Id";
    }

    public Stream<String> properties(){
        return Stream.of(id);
    }

    /* ==================================
     * === Methods: actions
     * ================================== */
    /**
     * Sell of a unit of this Product
     */
    public void sell(){
        assert (stock > 0);
        stock--;
        sold++;
    }

    /**
     * Produce a unit of this Product
     */
    public void produce(){
        stock++;
    }
}
