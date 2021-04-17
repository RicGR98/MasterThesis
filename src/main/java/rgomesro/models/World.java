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
    private int currentTick = 0;

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
            Agent agent = new Agent(market, RandomUtils.randomChoice(states));
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

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * Represent a step in the World's lifetime where its Entities can performd actions
     */
    public void tick(){
        if (currentTick % NB_TICKS_SAVE_CSV == 0){
            System.out.println(currentTick + " " + market.getProductCount());
            saveToCsv(); //Partial save
        }
        agents.forEach(Agent::tick);
        states.forEach(state -> state.tick(this.currentTick));
        this.currentTick++;
    }

    /**
     * Start the World simulation
     */
    public void run(){
        for (int i = 0; i < NB_TICKS; i++) {
            this.tick();
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
