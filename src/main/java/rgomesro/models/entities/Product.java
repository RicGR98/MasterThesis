package rgomesro.models.entities;

import rgomesro.Params;
import rgomesro.utils.RandomUtils;

import java.util.stream.Stream;

/**
 * Represent a Product that can be produced, sold and bought by an Agent on the Market
 */
public class Product extends Entity {
    private final Params.Product params;
    private final Agent producer;
    private final int type;
    private final float productionPrice;
    private final float sellingPrice;
    private int stock;
    private int sales;

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
        super(agent.getId());
        this.params = Params.getInstance().product;
        this.producer = agent;
        this.type = type;
        this.sellingPrice = sellingPrice;
        this.productionPrice = 0f; //sellingPrice/10f;
        this.stock = 0;
        this.sales = 0;
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

    public Integer getSales(){
        return sales;
    }

    public Boolean canBeProduced(){
        return this.stock < params.MAX_STOCK;
    }

    public static Integer getRandomType(){
        return RandomUtils.getInt(0, Params.getInstance().product.NB_DIFF_PRODUCTS);
    }

    /* ==================================
     * === Methods: csv
     * ================================== */
    public static String csvHeader() {
        return "Id,Type,ProductionPrice,SellingPrice,Stock,Sales";
    }

    @Override
    public Stream<String> properties(){
        return Stream.of(
                id,
                getType().toString(),
                getProductionPrice().toString(),
                getSellingPrice().toString(),
                getStock().toString(),
                getSales().toString()
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
        sales++;
    }

    /**
     * Produce a unit of this Product
     */
    public void produce(){
        stock++;
    }
}
