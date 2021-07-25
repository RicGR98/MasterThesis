package rgomesro.models;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import rgomesro.Params;
import rgomesro.models.entities.Agent;
import rgomesro.models.entities.Product;
import rgomesro.models.entities.State;
import rgomesro.utils.FileUtils;
import rgomesro.utils.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Represents the World holding the Market, States, Agents, ...
 */
public class World implements Runnable{
    private final Params.World params;
    private final Integer id;
    private final WorldMarket worldMarket;
    private final ArrayList<State> states;
    private final ArrayList<Agent> agents;
    private final HashMap<Integer, Float> productPrices;

    /* ==================================
     * ==== Constructors
     * ================================== */
    public World(int id, String paramsFile){
        super();
        Params.getInstance().load(paramsFile);
        this.params = Params.getInstance().world;
        System.out.println(
                "NB_STATES: " + params.NB_STATES + ", " +
                "NB_AGENTS: " + params.NB_AGENTS + ", " +
                "NB_TICKS: " + params.NB_TICKS + ", " +
                "CLUSTER_SIZE: " + Params.getInstance().connections.CLUSTER_SIZE + ", " +
                "PROB_CONNECTION: " + Params.getInstance().connections.PROB_CONNECTION + ", " +
                "VAT: " + Params.getInstance().tax.MIN_VAT + ", " +
                "LEVY: " + Params.getInstance().tax.MIN_LEVY + ", " +
                "TARIFF: " + Params.getInstance().tax.MIN_TARIFF + ", " +
                "WEALTH: " + Params.getInstance().tax.MIN_WEALTH_TAX_VALUE
        );
        this.id = id;
        this.worldMarket = new WorldMarket(this);
        this.states = new ArrayList<>(params.NB_STATES);
        this.agents = new ArrayList<>(params.NB_AGENTS);
        this.productPrices = new HashMap<>();
        this.init();
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
     * Initialize the World with its States, Agents, ...
     */
    private void init(){
        //Initialize States
        for (int i = 0; i < params.NB_STATES; i++) {
            State state = new State(i);
            this.states.add(state);
        }

        //Initialize Products prices
        var productParams = Params.getInstance().product;
        for (int i = 0; i < productParams.NB_DIFF_PRODUCTS; i++) {
            productPrices.put(
                    i,
                    RandomUtils.getFloat(
                            productParams.MIN_PRICE,
                            productParams.MAX_PRICE)
            );
        }

        //Initialize Agents
        for (int i = 0; i < params.NB_AGENTS; i++) {
            Agent agent = new Agent(
                    i,
                    worldMarket,
                    RandomUtils.choose(states),
                    RandomUtils.choose(productPrices.entrySet())
            );
            this.agents.add(agent);
        }

        //Initialize Markets
        worldMarket.init();

        //Update Connections
        Connections connections = new Connections(states);
        connections.updateConnectedStates();
    }

    /**
     * Represent a step in the World's lifetime where its Entities can performd actions
     */
    private void tick(int currentTick){
        if (currentTick % params.NB_TICKS_SAVE_CSV == 0){
            saveAllToCsv(); //Temporary save
        }
        agents.forEach(Agent::tick);
        states.forEach(state -> state.tick(currentTick));
    }

    /**
     * Start the World simulation
     */
    public void run(){
        ProgressBarBuilder pbb = new ProgressBarBuilder();
        pbb.setTaskName("Simulation:");
        pbb.setUnit(" ticks", 1);
        ProgressBar
                .wrap(IntStream.range(1, params.NB_TICKS), pbb)
                .forEach(this::tick);
        this.saveAllToCsv();
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
        filename = filename + "/" + id + ".csv";
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
                params.CSV_AGENTS,
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
                params.CSV_STATES,
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
                params.CSV_PRODUCTS,
                Product.csvHeader(),
                agents.stream()
                        .map(Agent::getProduct)
                        .map(Product::toCsv)
                        .collect(Collectors.joining("\n"))
        );
    }
}
