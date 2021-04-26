package rgomesro.models.allowances;

import rgomesro.Params;
import rgomesro.models.entities.Agent;
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
     * Distribute money of the State among its Agents equally
     * I.e.: all Agents receive the same share of money
     */
    public void distributeEqual(){
        float moneyToDistribute = state.getMoney() * percentage;
        var apa = moneyToDistribute / state.getAgents().size(); //Apa = Allowance per Agent
        state.getAgents().forEach(agent -> {
            agent.addMoney(apa);
            state.subtractMoney(apa);
        });
    }

    /**
     * Distribute money of the State among its Agents in a fair way
     * I.e.: poorer Agents will receive more money than richer ones.
     * Computed as follows:
     * - The State has {moneyToDistribute} money to distribute.
     * - Compute how much money an Agent has compared to the total
     *      amount of money held by all Agents together (in %)
     *      Notation: fractionOfTotalMoney (FOTM).
     * - Converted to 1/FOTM because we want Agents with a high
     *      FOTM (i.e. richer than others Agents) to get a smaller
     *      allowance. Notation: X (Agent poor => X high)
     * - We add all x's to obtain div which is the denominator of
     *      the next formula. Notation: div.
     * - Each Agent receives X/div (in %, notation: percentageAllowance)
     *      of the moneyToDistribute of the State. Thus, it receives:
     *      percentageAllowance * moneyToDistribute
     *      <=> X/div * moneyToDistribute
     *      <=> (1/fractionOfTotalMoney)/div * moneyToDistribute
     */
    public void distributeFair(){
        float moneyToDistribute = state.getMoney() * percentage;
        float agentsTotalMoney = state.getAgentsTotalMoney();
        float div = 0f;
        for (Agent agent: state.getAgents()){
            float fractionOfTotalMoney = agent.getMoney()/agentsTotalMoney;
            div += 1f/fractionOfTotalMoney;
        }
        for (Agent agent: state.getAgents()){
            float fractionOfTotalMoney = agent.getMoney()/agentsTotalMoney;
            float percentageAllowance = (1/fractionOfTotalMoney)/div;
            float allowance = moneyToDistribute * percentageAllowance;
            state.subtractMoney(allowance);
            agent.addMoney(allowance);
        }
    }
}
