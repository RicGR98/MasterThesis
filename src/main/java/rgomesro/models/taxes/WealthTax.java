package rgomesro.models.taxes;

import rgomesro.models.entities.Agent;
import rgomesro.models.entities.State;

import java.util.Comparator;
import java.util.List;

public class WealthTax extends Tax {
    private final State state;
    private final Integer top; //Top x% of wealthiest. E.g.: Top 10%
    private final Float value; //E.g.: 10% Wealth tax

    /* ==================================
     * ==== Constructors
     * ================================== */
    public WealthTax(State state, Integer top, Float value){
        this.state = state;
        this.top = top;
        this.value = value;
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    public Integer getTop() {
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
        return state.getAgents().size()*getTop()/100;
    }

    /**
     * @return List of top x% wealthiest Agents of a State
     */
    private List<Agent> getWealthiest(){
        List<Agent> res = state.getAgents();
        res.sort(Comparator.comparing(Agent::getMoney));
        res = res.subList(res.size()-percentageToNumber()-1, res.size()-1);
        return res;
    }

    /**
     * @param agent Wealthy Agent taxed
     * @return Amount of the value the Agent should pay as Wealth tax
     */
    public Float computeTax(Agent agent){
        return agent.getMoney() * getValue() / 100;
    }

    /**
     * Collect the tax from all the wealthy Agents of the State
     */
    public void collect(){
        List<Agent> wealthiest = getWealthiest();
        wealthiest.forEach(agent -> {
            Float tax = computeTax(agent);
            agent.subtractMoney(tax);
            state.addMoney(tax);
        });
    }
}
