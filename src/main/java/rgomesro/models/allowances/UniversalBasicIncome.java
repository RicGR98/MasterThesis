package rgomesro.models.allowances;

import rgomesro.models.entities.State;
import rgomesro.utils.RandomUtils;

import static rgomesro.Constants.State.Allowance.MAX_UBI;
import static rgomesro.Constants.State.Allowance.MIN_UBI;

/**
 * Represents the Universal Basic Income allowance given to Agents by their State
 */
public class UniversalBasicIncome extends Allowance {
    private final State state;
    private final Float allowance;

    /* ==================================
     * ==== Constructors
     * ================================== */
    /**
     * @param state State which implements the UBI
     * @param allowance Value of the allowance given by the State it its Agents
     */
    public UniversalBasicIncome(State state, float allowance){
        this.state = state;
        this.allowance = allowance;
    }

    public UniversalBasicIncome(State state){
        this(state, RandomUtils.getFloat(MIN_UBI, MAX_UBI));
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    public Float getAllowance() {
        return allowance;
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * Distribute money of the State among its Agents
     */
    public void distribute(){
         state.getAgents().forEach(agent -> {
             agent.addMoney(allowance);
             state.subtractMoney(allowance);
         });
    }
}
