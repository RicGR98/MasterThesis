import java.util.stream.Stream;

public class Agent extends Entity {
    private final World world;
    private final State state;
    private float money;
    private final Product product;

    public Agent(World world, State state, float money) {
        super("Agent");
        this.world = world;
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
        product.incrementStock();
    }

    public void sell(){
        this.product.decrementStock();
    }

    public void buy(){
        this.world.market.buy(this, Product.getRandomType());
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
