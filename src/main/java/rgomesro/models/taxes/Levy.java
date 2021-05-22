package rgomesro.models.taxes;

import rgomesro.Params;
import rgomesro.models.entities.Agent;
import rgomesro.models.entities.State;
import rgomesro.utils.RandomUtils;
import rgomesro.utils.TransactionUtils;

/**
 * Represents a periodic Tax from a State on all its Agents
 */
public class Levy extends Tax {
    private final State state;

    /* ==================================
     * ==== Constructors
     * ================================== */
    /**
     * @param state State implementing the Levy
     * @param value Value of the Levy
     */
    public Levy(State state, Float value) {
        super(value);
        this.state = state;
    }

    public Levy(State state){
        this(
                state,
                RandomUtils.getFloat(
                        Params.getInstance().tax.MIN_LEVY,
                        Params.getInstance().tax.MAX_LEVY)
        );
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * Collect the tax from all the Agents of the State
     */
    public void collect(){
        if (getValue() == 0)
            return;
        for (Agent agent: state.getAgents()){
            float tax = this.compute(agent);
            TransactionUtils.make(agent, state, tax);
        }
    }
}
