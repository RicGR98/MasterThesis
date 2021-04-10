package rgomesro.Models;

import rgomesro.Constants;
import rgomesro.Utils;
import rgomesro.World;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Agent extends Entity {
    private final World world;
    private final State state;
    private Float money;
    private final ArrayList<Product> products;

    public Agent(World world, State state, float money) {
        super();
        this.world = world;
        this.state = state;
        this.money = money;
        this.products = new ArrayList<>();
        for (int i = 0; i < Constants.Agent.NB_PRODUCTS; i++) {
            this.products.add(new Product(this));
        }
    }

    public State getState() {
        return state;
    }

    public ArrayList<Product> getProducts(){
        return products;
    }

    public Float getMoney() {
        return money;
    }

    public void addMoney(float value){
        this.money += value;
    }

    public void subtractMoney(float value){
        this.money -= value;
    }

    public void produce(){
        Utils.randomChoice(products).incrementStock();
    }

    public void buy(){
        this.world.market.buy(this, Utils.getRandomInt(0, Constants.Product.NB_UNIQUE));
    }

    public void tick(){
        if (Utils.getRandom() < Constants.Agent.RATIO_BUY)
            buy();
        if (Utils.getRandom() < Constants.Agent.RATIO_PRODUCE)
            produce();
    }

    public static String csvColumnsNames(){
        return "Name,Money,State";
    }

    public Stream<String> csvFields(){
        return Stream.of(getName(), money.toString(), getState().toString());
    }
}
