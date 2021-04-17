package rgomesro.models;

import rgomesro.models.entities.Agent;
import rgomesro.models.entities.Cluster;
import rgomesro.models.entities.Product;
import rgomesro.models.entities.State;
import rgomesro.utils.FileUtils;
import rgomesro.utils.RandomUtils;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static rgomesro.Constants.Cluster.PROB_ATTACHED;
import static rgomesro.Constants.World.*;

/**
 * Represents the World holding the Market, States, Agents, ...
 */
public class World {
    private final Market market;
    private final Cluster cluster;
    private final ArrayList<State> states;
    private final ArrayList<Agent> agents;
    private int currentTick = 0;

    /* ==================================
     * ==== Constructors
     * ================================== */
    public World(){
        this.market = new Market(this);
        this.cluster = new Cluster();
        this.states = new ArrayList<>(NB_STATES);
        for (int i = 0; i < NB_STATES; i++) {
            State state = new State(this);
            this.states.add(state);
            if (RandomUtils.getRandom() < PROB_ATTACHED){
                this.cluster.addState(state);
            }
        }
        this.agents = new ArrayList<>(NB_AGENTS);
        for (int i = 0; i < NB_AGENTS; i++) {
            Agent agent = new Agent(market, RandomUtils.choose(states));
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

    public Cluster getCluster() {
        return cluster;
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * Represent a step in the World's lifetime where its Entities can performd actions
     */
    private void tick(){
        if (currentTick % NB_TICKS_SAVE_CSV == 0){
            System.out.println(currentTick + " " + market.getProductCount());
            saveAllToCsv(); //Partial save
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
    public void saveAllToCsv(){
        saveAgentsToCsv();
        saveStatesToCsv();
        saveProductsToCsv();
    }

    /**
     * @param filename Filename of the csv in which to write the results
     * @param header Header of the csv, i.e. column names
     * @param rows Rows of the csv file (each one representing one Entity)
     */
    private void saveToCsv(String filename, String header, String rows){
        FileUtils.fileDelete(filename);
        String csv = "";
        if (!FileUtils.fileExists(filename))
            csv += header + "\n";
        csv += rows;
        FileUtils.writeToFile(filename, csv);
    }

    /**
     * Save all Agents to csv
     */
    private void saveAgentsToCsv(){
        saveToCsv(
                CSV_AGENTS,
                Agent.csvHeader(),
                agents.stream()
                        .map(Agent::toCsv)
                        .collect(Collectors.joining("\n"))
        );
    }

    /**
     * Save all States to csv
     */
    private void saveStatesToCsv(){
        saveToCsv(
                CSV_STATES,
                State.csvHeader(),
                states.stream()
                        .map(State::toCsv)
                        .collect(Collectors.joining("\n"))
        );
    }

    /**
     * Save all Products to csv
     */
    private void saveProductsToCsv(){
        saveToCsv(
                CSV_PRODUCTS,
                Product.csvHeader(),
                agents.stream()
                        .map(Agent::getProduct)
                        .map(Product::toCsv)
                        .collect(Collectors.joining("\n"))
        );
    }
}
