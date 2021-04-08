import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Agent extends Entity {
    private final World world;
    private final Market market;
    private final State state;
    private float money;

    public Agent(World world, Market market, State state, float money) {
        super("Agent");
        this.world = world;
        this.market = market;
        this.state = state;
        this.money = money;
    }

    public State getState() {
        return state;
    }

    public float getMoney() {
        return money;
    }

    public void addMoney(float value){
        this.money += value;
    }

    public void subtractMoney(float value){
        this.money -= value;
    }

    public void produce(){
        this.market.produce(new Product(this));
    }

    public void buy(){
        this.market.buy(this, Product.getRandomType());
    }

    public void tick(){
        if (Utils.getRandom() < Constants.Agent.RATIO_BUY)
            buy();
        if (Utils.getRandom() < Constants.Agent.RATIO_PRODUCE)
            produce();
    }

    public Stream<String> csvFields(){
        return Stream.of(getName(), String.valueOf(money), getState().toString());
    }
}
