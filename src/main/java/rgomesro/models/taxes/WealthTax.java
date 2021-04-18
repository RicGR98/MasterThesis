package rgomesro.models.taxes;

import rgomesro.models.entities.Agent;
import rgomesro.models.entities.State;

import java.util.Comparator;
import java.util.List;

import static rgomesro.Constants.State.Tax.VAL_WEALTH_TAX_TOP;
import static rgomesro.Constants.State.Tax.VAL_WEALTH_TAX_VALUE;

/**
 * Represents the Wealth Tax a State might implement:
 * The top {top}% are taxed {value}%
 */
public class WealthTax extends Tax {
    private final State state;
    private final Float top; //E.g.: Top 10% (top = 0.1) wealthiest Agents are taxed
    private final Float value; //E.g.: 10% (value = 0.1) Wealth tax

    /* ==================================
     * ==== Constructors
     * ================================== */
    public WealthTax(State state, Float top, Float value){
        this.state = state;
        this.top = top;
        this.value = value;
    }

    public WealthTax(State state){
        this(state, VAL_WEALTH_TAX_TOP, VAL_WEALTH_TAX_VALUE);
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    public Float getTop() {
        return top;
    }

    public Float getValue() {
        return value;
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * @return Number of Agents to retrieve to match the top x% required
     */
    private Integer percentageToNumber(){
        return (int) (state.getAgents().size()*getTop());
    }

    /**
     * @return List of top x% wealthiest Agents of a State
     */
    private List<Agent> getWealthiest(){
        List<Agent> res = state.getAgents();
        res.sort(Comparator.comparing(Agent::getMoney));
        var from = res.size()-percentageToNumber()-1;
        var to = res.size()-1;
        res = res.subList(from, to);
        return res;
    }

    /**
     * @param agent Wealthy Agent taxed
     * @return Amount of the value the Agent should pay as Wealth tax
     */
    public Float computeTax(Agent agent){
        return agent.getMoney() * getValue();
    }

    /**
     * Collect the tax from all the wealthy Agents of the State
     */
    public void collect(){
        if (getTop() == 0 || getValue() == 0)
            return;
        List<Agent> wealthiest = getWealthiest();
        wealthiest.forEach(agent -> {
            Float tax = computeTax(agent);
            agent.subtractMoney(tax);
            state.addMoney(tax);
        });
    }
}
