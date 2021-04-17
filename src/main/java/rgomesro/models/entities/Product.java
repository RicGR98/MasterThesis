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
    private final float productionPrice;
    private final float sellingPrice;
    private int stock;
    private int sold;

    /* ==================================
     * ==== Constructors
     * ================================== */
    /**
     * @param agent Agent who produces this product
     * @param type Type of the product
     * @param sellingPrice Selling price of the product (without taxes)
     * @param productionPrice Price when producing a unit of this Product
     */
    public Product(Agent agent, int type, float sellingPrice, float productionPrice) {
        super();
        this.producer = agent;
        this.type = type;
        this.productionPrice = productionPrice;
        this.sellingPrice = sellingPrice;
        this.stock = 0;
        this.sold = 0;
    }

    public Product(Agent agent, int type, float sellingPrice){
        this(agent, type, sellingPrice, 0f);
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

    public Float getSellingPrice() {
        return sellingPrice;
    }

    public Float getProductionPrice() {
        return productionPrice;
    }

    public Integer getStock(){
        return stock;
    }

    public Integer getSold(){
        return sold;
    }

    public Boolean canBeProduced(){
        return this.stock < MAX_STOCK;
    }

    /* ==================================
     * === Methods: csv
     * ================================== */
    public static String csvHeader() {
        return "Id,Type,ProductionPrice,SellingPrice,Stock,Sold";
    }

    public Stream<String> properties(){
        return Stream.of(
                id,
                getType().toString(),
                getProductionPrice().toString(),
                getSellingPrice().toString(),
                getStock().toString(),
                getSold().toString()
        );
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
