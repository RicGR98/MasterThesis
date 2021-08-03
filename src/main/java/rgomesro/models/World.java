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
    private final String paramsFile;
    private final Params.World params;
    private final WorldMarket worldMarket;
    private final ArrayList<State> states;
    private final ArrayList<Agent> agents;
    private final HashMap<Integer, Float> productPrices;
    private Integer currentTick;

    /* ==================================
     * ==== Constructors
     * ================================== */
    public World(String paramsFile){
        super();
        Params.getInstance().load(paramsFile);
        this.paramsFile = paramsFile.replace(".json", "");
        this.params = Params.getInstance().world;
        if (this.paramsFile.contains("exp")){ //Only show params when doing Experiments
            System.out.println(
                    "NB_STATES: " + params.NB_STATES + ", " +
                    "NB_AGENTS: " + params.NB_AGENTS + ", " +
                    "NB_TICKS: " + params.NB_TICKS + ", " +
                    "PRODUCT_CHOICE: " + params.PRODUCT_CHOICE + ", " +
                    "CLUSTER_SIZE: " + Params.getInstance().connections.CLUSTER_SIZE + ", " +
                    "PROB_CONNECTION: " + Params.getInstance().connections.PROB_CONNECTION + ", " +
                    "VAT: [" + Params.getInstance().tax.MIN_VAT + ", " + Params.getInstance().tax.MAX_VAT + "], " +
                    "LEVY: [" + Params.getInstance().tax.MIN_LEVY + ", " + Params.getInstance().tax.MAX_LEVY + "], " +
                    "TARIFF: [" + Params.getInstance().tax.MIN_TARIFF + ", " + Params.getInstance().tax.MAX_TARIFF + "], " +
                    "WEALTH: [" + Params.getInstance().tax.MIN_WEALTH_TAX_VALUE + ", " + Params.getInstance().tax.MAX_WEALTH_TAX_VALUE + "], " +
                    "UNEMPLOYMENT: [" + Params.getInstance().state.MIN_UNEMPLOYMENT + ", " + Params.getInstance().state.MAX_UNEMPLOYMENT + "], " +
                    "BLACK: [" + Params.getInstance().state.MIN_BLACK + ", " + Params.getInstance().state.MAX_BLACK + "], " +
                    "INIT_MONEY: [" + Params.getInstance().agent.MIN_INIT_MONEY + ", " + Params.getInstance().agent.MAX_INIT_MONEY + "]"
            );
        }
        this.worldMarket = new WorldMarket(this);
        this.states = new ArrayList<>(params.NB_STATES);
        this.agents = new ArrayList<>(params.NB_AGENTS);
        this.productPrices = new HashMap<>();
        this.currentTick = 0;
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

        //Delete previous ticks file
        String ticksFilename = params.CSV_TICKS + "/" + paramsFile + ".csv";
        FileUtils.fileDelete(ticksFilename);
        FileUtils.writeToFile(ticksFilename, getTicksCsvHeader());
    }

    /**
     * Represent a step in the World's lifetime where its Entities can performd actions
     */
    private void tick(int currentTick){
        this.currentTick = currentTick;
        agents.forEach(Agent::tick);
        states.forEach(state -> state.tick(currentTick));
        if (currentTick % params.NB_TICKS_SAVE_CSV == 0){
            saveAllToCsv(); //Temporary save
        }
    }

    /**
     * Start the World simulation
     */
    public void run(){
        ProgressBarBuilder pbb = new ProgressBarBuilder();
        pbb.setTaskName("Simulation:");
        pbb.setUnit(" ticks", 1);
        ProgressBar
                .wrap(IntStream.range(1, params.NB_TICKS+1), pbb)
                .forEach(this::tick);
//        this.saveAllToCsv();
    }

    /* ==================================
     * ==== Methods: Getters
     * ================================== */

    /**
     * @return How a Product should be chosen from a list of possibilities
     */
    public Params.ProductChoice getProductChoice() {
        return params.PRODUCT_CHOICE;
    }

    /**
     * @return Total amount of money States have
     */
    private float getTotalStatesMoney(){
        float total = 0f;
        for (State state: states){
            total += state.getMoney();
        }
        return total;
    }

    /**
     * @return Total amount of money Agents have
     */
    private float getTotalAgentsMoney(){
        float total = 0f;
        for (Agent agent: agents){
            total += agent.getMoney();
        }
        return total;
    }

    /**
     * @return Total GDP of all States
     */
    private float getTotalGdp(){
        float total = 0f;
        for (State state: states){
            total += state.getGdp();
        }
        return total;
    }

    /* ==================================
     * ==== Methods: csv
     * ================================== */
    /**
     * Save all Entities to csv files for analysis later
     */
    public void saveAllToCsv(){
        saveTicksToCsv();
        saveAgentsToCsv();
        saveStatesToCsv();
        saveProductsToCsv();
    }


    /**
     * @return Csv header for the ticks file
     */
    private static String getTicksCsvHeader(){
        return "Tick,WorldNbTransactions,WorldStatesMoney,WorldAgentsMoney,WorldGdp";
    }

    /**
     * Save evolution (ticks) of key metrics to csv
     */
    private void saveTicksToCsv(){
        String ticksFilename = params.CSV_TICKS + "/" + paramsFile + ".csv";
        String line = currentTick + "," +
                worldMarket.getNbSales() + "," +
                getTotalStatesMoney() + "," +
                getTotalAgentsMoney() + "," +
                getTotalGdp();
        FileUtils.writeToFile(ticksFilename, line);
    }

    /**
     * @param filename Filename of the csv in which to write the results
     * @param header Header of the csv, i.e. column names
     * @param rows Rows of the csv file (each one representing one Entity)
     */
    private void saveEntitiesToCsv(String filename, String header, String rows){
        filename = filename + "/" + paramsFile + ".csv";
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
        saveEntitiesToCsv(
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
        saveEntitiesToCsv(
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
        saveEntitiesToCsv(
                params.CSV_PRODUCTS,
                Product.csvHeader(),
                agents.stream()
                        .map(Agent::getProduct)
                        .map(Product::toCsv)
                        .collect(Collectors.joining("\n"))
        );
    }
}
