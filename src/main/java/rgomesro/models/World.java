package rgomesro.models;

import rgomesro.models.entities.Agent;
import rgomesro.models.entities.State;
import rgomesro.utils.FileUtils;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static rgomesro.Constants.World.*;

/**
 * Represents the World holding the Market, States, Agents, ...
 */
public class World {
    private final Market market;
    private final ArrayList<State> states;
    private final ArrayList<Agent> agents;

    /* ==================================
     * ==== Constructors
     * ================================== */
    public World(){
        this.market = new Market(this);
        this.states = new ArrayList<>(NB_STATES);
        for (int i = 0; i < NB_STATES; i++) {
            this.states.add(new State());
        }
        this.agents = new ArrayList<>(NB_AGENTS);
        for (int i = 0; i < NB_AGENTS; i++) {
            Agent agent = new Agent(market, states.get(i % NB_STATES), 1000);
            this.agents.add(agent);
        }
        market.initMarket();
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    public ArrayList<State> getStates() {
        return states;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * Represent a step in the World's lifetime where its Entities can performd actions
     */
    public void tick(){
        agents.forEach(Agent::tick);
        states.forEach(State::tick);
    }

    /**
     * Start the World simulation
     */
    public void run(){
        for (int t = 0; t < NB_TICKS; t++) {
            this.tick();
            if (t % 100 == 0)
                System.out.println(t + " " + market.getProductCount());
        }
    }

    /* ==================================
     * ==== Methods: csv
     * ================================== */
    /**
     * Save all Entities to csv files for analysis later
     */
    public void saveToCsv(){
        saveAgentsToCsv();
        saveStatesToCsv();
    }

    /**
     * Save all Agents to csv
     */
    public void saveAgentsToCsv(){
        String csv = Agent.csvHeader() + "\n";
        csv += agents.stream()
                .map(Agent::toCsv)
                .collect(Collectors.joining("\n"));
        FileUtils.writeToFile("res/agents.csv", csv);
    }

    /**
     * Save all States to csv
     */
    public void saveStatesToCsv(){
        String csv = State.csvHeader() + "\n";
        csv += states.stream()
                .map(State::toCsv)
                .collect(Collectors.joining("\n"));
        FileUtils.writeToFile("res/states.csv", csv);
    }
}