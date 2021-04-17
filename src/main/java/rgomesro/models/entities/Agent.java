package rgomesro.models.entities;

import rgomesro.models.Market;
import rgomesro.utils.RandomUtils;

import java.util.stream.Stream;

import static rgomesro.Constants.Agent.*;

/**
 * Represents an Agent in the World who produces, sells, and buys Products on the Market
 */
public class Agent extends Entity {
    private final Market market;
    private final State state;
    private Float money;
    private final Product product;
    private Integer nbProductsBought = 0;

    /* ==================================
     * ==== Constructors
     * ================================== */
    /**
     * @param market Market of the World
     * @param state State to which the Agent belongs
     * @param money Initial money of the Agent
     */
    public Agent(Market market, State state, float money) {
        super();
        this.market = market;
        this.state = state;
        this.state.addAgent(this);
        this.money = money;
        this.product = new Product(this);
    }

    public Agent(Market market, State state){
        this(market, state, INIT_MONEY);
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    public State getState() {
        return state;
    }

    public Product getProduct(){
        return product;
    }

    public Float getMoney(){
        return money;
    }

    /* ==================================
     * ==== Methods: csv
     * ================================== */
    public static String csvHeader() {
        return "Id,Product,Money,State,SoldProducts,BoughtProducts";
    }

    public Stream<String> properties(){
        return Stream.of(
                id,
                product.getType().toString(),
                money.toString(),
                state.toString(),
                product.getSold().toString(),
                nbProductsBought.toString());
    }

    /* ==================================
     * ==== Methods: money
     * ================================== */
    /**
     * @param money Amount of money to check
     * @return true if user has, at least, the amount of money
     */
    public Boolean hasEnoughMoney(Float money) {
        return this.money > money;
    }

    /**
     * @param value Money to add to the Agent's money
     */
    public void addMoney(float value){
        this.money += value;
    }

    /**
     * @param value Money to subtract from the Agent's money
     */
    public void subtractMoney(float value){
        assert (this.money > value);
        this.money -= value;
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * Produce a unit of its product
     */
    public void produce(){
        if (hasEnoughMoney(product.getProductionPrice()) && product.canBeProduced()){
            product.produce();
            this.subtractMoney(product.getProductionPrice());
        }
    }

    /**
     * Buy a product on the market
     */
    public void buy(){
        if (this.market.buy(this))
            nbProductsBought++;
    }

    /**
     * Represent a step in the Agent's lifetime where it can perform actions
     */
    public void tick(){
        if (RandomUtils.getRandom() < RATIO_BUY)
            buy();
        if (RandomUtils.getRandom() < RATIO_PRODUCE)
            produce();
    }
}
