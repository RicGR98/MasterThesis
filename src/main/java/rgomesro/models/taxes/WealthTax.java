package rgomesro.models.taxes;

import rgomesro.Params;
import rgomesro.models.entities.Agent;
import rgomesro.models.entities.State;
import rgomesro.utils.RandomUtils;
import rgomesro.utils.TransactionUtils;

import java.util.Comparator;
import java.util.List;


/**
 * Represents the Wealth Tax a State might implement:
 * The top {top}% are taxed {value}%
 */
public class WealthTax extends Tax {
    private final State state;
    private final Float top;

    /* ==================================
     * ==== Constructors
     * ================================== */
    public WealthTax(State state, Float top, Float value){
        super(value);
        this.state = state;
        this.top = top;
    }

    public WealthTax(State state){
        this(
                state,
                Params.getInstance().tax.VAL_WEALTH_TAX_TOP,
                RandomUtils.getFloat(
                        Params.getInstance().tax.MIN_WEALTH_TAX_VALUE,
                        Params.getInstance().tax.MAX_WEALTH_TAX_VALUE)
        ); // TODO: Analyze
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    public Float getTop() {
        return top;
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
     * Collect the tax from all the wealthy Agents of the State
     */
    public void collect(){
        if (getTop() == 0 || getValue() == 0)
            return;
        List<Agent> wealthiest = getWealthiest();
        for (Agent agent: wealthiest){
            Float tax = compute(agent);
            TransactionUtils.make(agent, state, tax);
        }
    }
}
