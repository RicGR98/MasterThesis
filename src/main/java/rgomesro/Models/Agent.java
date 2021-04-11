package rgomesro.Models;

import rgomesro.Market;
import rgomesro.Utils;

import java.util.stream.Stream;

import static rgomesro.Constants.Agent.RATIO_BUY;
import static rgomesro.Constants.Agent.RATIO_PRODUCE;

public class Agent extends Entity {
    private final Market market;
    private final State state;
    private Float money;
    private final Product product;

    public Agent(Market market, State state, float money) {
        super();
        this.market = market;
        this.state = state;
        this.money = money;
        this.product = new Product(this);
    }

    public State getState() {
        return state;
    }

    public Product getProduct(){
        return product;
    }

    public Float getMoney() {
        return money;
    }

    public Boolean hasEnoughMoney(Float money) {
        return this.money > money;
    }

    public void addMoney(float value){
        this.money += value;
    }

    public void subtractMoney(float value){
        this.money -= value;
        assert (this.money > 0);
    }

    public void produce(){
        product.produce();
    }

    public void buy(){
        this.market.buy(this);
    }

    public void tick(){
        if (Utils.getRandom() < RATIO_BUY)
            buy();
        if (Utils.getRandom() < RATIO_PRODUCE)
            produce();
    }

    public static String csvColumnsNames(){
        return "Id,Product,Money,State";
    }

    public Stream<String> csvFields(){
        return Stream.of(getId(), product.getType().toString(), money.toString(), getState().toString());
    }
}
