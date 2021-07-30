package rgomesro.models.entities;

import rgomesro.Params;
import rgomesro.models.WorldMarket;
import rgomesro.utils.RandomUtils;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Represents an Agent in the World who produces, sells, and buys Products on the Market
 */
public class Agent extends Entity {
    private final Params.Agent params;
    private final WorldMarket worldMarket;
    private final State state;
    private final Float initMoney;
    private Float money;
    private final Float talent;
    private final Product product;
    private final Boolean isProducer;
    private Integer nbPurchases = 0;

    /* ==================================
     * ==== Constructors
     * ================================== */
    /**
     * @param worldMarket Market of the World
     * @param state State to which the Agent belongs
     * @param money Initial money of the Agent
     * @param chosenProduct Map entry where key = product type, value = product price
     * @param talent Talent of production [0, 1]. The bigger => the cheaper the production price
     */
    public Agent(
            int id,
            WorldMarket worldMarket,
            State state,
            float money,
            Map.Entry<Integer, Float> chosenProduct,
            float talent,
            boolean isProducer) {
        super(id);
        this.params = Params.getInstance().agent;
        this.worldMarket = worldMarket;
        this.state = state;
        this.state.addAgent(this);
        this.initMoney = money;
        this.money = money;
        this.talent = talent;
        this.isProducer = isProducer;
        this.product = new Product(
                this,
                chosenProduct.getKey(),
                chosenProduct.getValue() * (1 - this.talent),
                0f
        );
    }

    public Agent(
            int id,
            WorldMarket worldMarket,
            State state,
            Map.Entry<Integer, Float> chosenProduct){
        this(
                id,
                worldMarket,
                state,
                RandomUtils.getFloat(
                        Params.getInstance().agent.MIN_INIT_MONEY,
                        Params.getInstance().agent.MAX_INIT_MONEY),
                chosenProduct,
                RandomUtils.getRandom(),
                RandomUtils.getBoolean(state.getUnemployment())
        );
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
        return "Id,State,AgentInitMoney,AgentMoney,Talent,IsProducer,Purchases";
    }

    @Override
    public Stream<String> properties(){
        return Stream.of(
                id,
                state.toString(),
                initMoney.toString(),
                money.toString(),
                talent.toString(),
                isProducer.toString(),
                nbPurchases.toString());
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
        assert (value >= 0);
        this.money += value;
    }

    /**
     * @param value Money to subtract from the Agent's money
     */
    public void subtractMoney(float value){
        assert (hasEnoughMoney(value));
        this.money -= value;
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * Produce a unit of its product
     */
    public void produce(){
        if (this.isProducer && hasEnoughMoney(product.getProductionPrice()) && product.canBeProduced()){
            product.produce();
            this.subtractMoney(product.getProductionPrice());
        }
    }

    /**
     * Buy a product on the market
     */
    public void buy(){
        if (this.worldMarket.buy(this))
            nbPurchases++;
    }

    /**
     * Represent a step in the Agent's lifetime where it can perform actions
     */
    public void tick(){
        if (RandomUtils.getRandom() < params.RATIO_BUY)
            buy();
        if (RandomUtils.getRandom() < params.RATIO_PRODUCE)
            produce();
    }
}
