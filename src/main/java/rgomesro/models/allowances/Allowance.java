package rgomesro.models.allowances;

import rgomesro.Params;
import rgomesro.models.entities.State;
import rgomesro.utils.RandomUtils;

/**
 * Represents an Allowance given to Agents by their State
 */
public class Allowance {
    private final State state;
    private final Float percentage;

    /* ==================================
     * ==== Constructors
     * ================================== */
    /**
     * @param state State which implements the Allowance
     * @param percentage Value of the percentage of the State's money that
     *                   will be divided among the State's Agents
     */
    public Allowance(State state, float percentage){
        this.state = state;
        this.percentage = percentage;
    }

    public Allowance(State state){
        this(
                state,
                RandomUtils.getFloat(
                        Params.getInstance().allowance.MIN_ALLOWANCE,
                        Params.getInstance().allowance.MAX_ALLOWANCE)
        ); // TODO: Analyze
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    public Float getPercentage() {
        return percentage;
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * Distribute money of the State among its Agents
     */
    public void distributeEqual(){
        float moneyToDistribute = state.getMoney() * percentage;
        var apa = moneyToDistribute / state.getAgents().size(); //Apa = Allowance per Agent
        state.getAgents().forEach(agent -> {
            agent.addMoney(apa);
            state.subtractMoney(apa);
        });
    }
}
