package rgomesro.models.allowances;

import rgomesro.models.entities.Agent;
import rgomesro.models.entities.State;
import rgomesro.utils.MathUtils;
import rgomesro.utils.RandomUtils;
import rgomesro.utils.TransactionUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an Allowance given to Agents by their State
 */
public class Allowance {
    private final State state;
    public enum Type {Flat, Fair}
    private final Type type;

    /* ==================================
     * ==== Constructors
     * ================================== */
    /**
     * @param state State which implements the Allowance
     * @param type Type of Allowance: Flat (≈ UBI) or Fair
     */
    public Allowance(State state, Type type){
        this.state = state;
        this.type = type;
    }

    public Allowance(State state){
        this(
                state,
                RandomUtils.choose(Type.values())
        );
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    public Type getType() {
        return type;
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * Distribute money of the State among its Agents equally
     * I.e.: all Agents receive the same share of money
     */
    private void distributeFlat(){
        float moneyToDistribute = state.getMoney() * 0.99f;
        var allowance = (float) (Math.floor(moneyToDistribute / state.getAgents().size() * 100) / 100); // Round down
        state.getAgents().forEach(agent -> TransactionUtils.make(state, agent, allowance));
    }

    /**
     * Distribute money of the State among its Agents in a fairer way
     */
    private void distributeFair(){
        List<Float> agentsMoney = state.getAgents()
                                        .stream()
                                        .sorted(Comparator.comparingDouble(Agent::getMoney))
                                        .map(Agent::getMoney)
                                        .collect(Collectors.toList());
        float average = MathUtils.getAverage(agentsMoney);
        float totalToDistribute = 0f;
        HashMap<Agent, Float> diffWithMedian = new HashMap<>();
        for (Agent agent: state.getAgents()){
            float money = average - agent.getMoney();
            if (money < 0)
                money = 0;
            diffWithMedian.put(agent, money);
            totalToDistribute += money;
        }
        float stateMoney = state.getMoney() * 0.99f;
        for (Agent agent: state.getAgents()){
            float agentPercentage = diffWithMedian.get(agent)/totalToDistribute;
            float idealValue = diffWithMedian.get(agent);
            float stateValue = stateMoney * agentPercentage;
            float toDistribute = idealValue;
            if (stateValue < toDistribute)
                toDistribute = stateValue;
            TransactionUtils.make(state, agent, toDistribute);
        }
        //Distribute the rest flatly
        distributeFlat();
    }

    /**
     * Distribute the Allowance according to the Type chosen by the State
     */
    public void distribute(){
        if (getType() == Type.Flat)
            distributeFlat();
        else
            distributeFair();
    }
}
