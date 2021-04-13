package rgomesro.models;

import rgomesro.models.entities.Agent;
import rgomesro.models.entities.State;
import rgomesro.utils.FileUtils;
import rgomesro.utils.RandomUtils;

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
            this.states.add(new State(this));
        }
        this.agents = new ArrayList<>(NB_AGENTS);
        for (int i = 0; i < NB_AGENTS; i++) {
            Agent agent = new Agent(market, RandomUtils.randomChoice(states), 1000);
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

    /**
     * @param state State
     * @return Agents belonging to a State
     */
    public ArrayList<Agent> getPopulationOfState(State state){
        var population = new ArrayList<Agent>();
        agents.forEach(agent -> {
            if (agent.getState() == state)
                population.add(agent);
        });
        return population;
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
            if (t % NB_TICKS_SAVE_CSV == 0){
                System.out.println(t + " " + market.getProductCount());
                saveToCsv(); //Partial save
            }
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
        FileUtils.fileDelete(CSV_AGENTS);
        String csv = "";
        if (!FileUtils.fileExists(CSV_AGENTS))
            csv += Agent.csvHeader() + "\n";
        csv += agents.stream()
                .map(Agent::toCsv)
                .collect(Collectors.joining("\n"));
        FileUtils.writeToFile(CSV_AGENTS, csv);
    }

    /**
     * Save all States to csv
     */
    public void saveStatesToCsv(){
        FileUtils.fileDelete(CSV_STATES);
        String csv = "";
        if (!FileUtils.fileExists(CSV_STATES))
            csv += State.csvHeader() + "\n";
        csv += states.stream()
                .map(State::toCsv)
                .collect(Collectors.joining("\n"));
        FileUtils.writeToFile(CSV_STATES, csv);
    }
}
