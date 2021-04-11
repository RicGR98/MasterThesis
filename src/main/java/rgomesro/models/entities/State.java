package rgomesro.models.entities;

import rgomesro.models.World;
import rgomesro.models.taxes.VAT;
import rgomesro.utils.RandomUtils;

import java.util.ArrayList;
import java.util.stream.Stream;

import static rgomesro.Constants.State.MAX_VAT;
import static rgomesro.Constants.State.MIN_VAT;

/**
 * Represents a State to which Agents belong
 */
public class State extends Entity {
    private final World world;
    private final VAT vat;
    private Float money = 0f;

    /* ==================================
     * ==== Constructors
     * ================================== */
    /**
     * @param world Reference to the World
     */
    public State(World world) {
        super();
        this.world = world;
        this.vat = new VAT(RandomUtils.getRandomFloat(MIN_VAT, MAX_VAT));
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    public VAT getVat(){
        return this.vat;
    }

    public ArrayList<Agent> getPopulation(){
        return world.getPopulationOfState(this);
    }

    /**
     * @return Total amount of money of the population
     */
    public Float getPopulationMoney(){
        Float total = 0f;
        for (Agent agent: getPopulation()){
            total += agent.getMoney();
        }
        return total;
    }

    /* ==================================
     * ==== Methods: csv
     * ================================== */
    public static String csvHeader(){
        return "Id,VAT,Money,PopulationSize,PopulationMoney";
    }

    public Stream<String> properties(){
        return Stream.of(
                id,
                vat.getValue().toString(),
                money.toString(),
                String.valueOf(getPopulation().size()),
                getPopulationMoney().toString());
    }

    /* ==================================
     * ==== Methods: money
     * ================================== */
    /**
     * @param value Money to add to the State's money
     */
    public void addMoney(float value){
        this.money += value;
    }

    /**
     * @param value Money to subtract from the State's money
     */
    public void subtractMoney(float value){
        assert (this.money > value);
        this.money -= value;
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * Collect all taxes the State has implemented
     */
    public void collectTaxes(){

    }

    /**
     * Represent a step in the State's lifetime where it can perform actions
     */
    public void tick(){
        collectTaxes();
    }
}
