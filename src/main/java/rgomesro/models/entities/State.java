package rgomesro.models.entities;

import rgomesro.Params;
import rgomesro.models.allowances.Allowance;
import rgomesro.models.taxes.Levy;
import rgomesro.models.taxes.Tariff;
import rgomesro.models.taxes.VAT;
import rgomesro.models.taxes.WealthTax;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Represents a State to which Agents belong
 */
public class State extends Entity {
    private final Params.State params;
    private final Market market;
    private final ArrayList<State> connectedStates;
    private final VAT vat;
    private final Levy levy;
    private final Tariff tariff;
    private final WealthTax wealthTax;
    private final Allowance allowance;
    private final ArrayList<Agent> agents;
    private Float money = 0f;

    /* ==================================
     * ==== Constructors
     * ================================== */
    /**
     * @param id Id of the State
     */
    public State(int id) {
        super(id);
        this.params = Params.getInstance().state;
        this.market = new Market(this);
        this.connectedStates = new ArrayList<>();
        this.agents = new ArrayList<>();
        this.vat = new VAT();
        this.levy = new Levy(this);
        this.tariff = new Tariff();
        this.wealthTax = new WealthTax(this);
        this.allowance = new Allowance(this);
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    public VAT getVat(){
        return this.vat;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public Float getMoney() {
        return money;
    }

    public ArrayList<Agent> getAgents(){
        return this.agents;
    }

    public Market getMarket() {
        return market;
    }

    public ArrayList<State> getConnectedStates() {
        return connectedStates;
    }

    /**
     * @return Total amount of money of the population
     */
    public Float getAgentsTotalMoney(){
        Float total = 0f;
        for (Agent agent: getAgents()){
            total += agent.getMoney();
        }
        return total;
    }

    /**
     * @return Total number of transaction (=amount of sold products by the population)
     */
    public Integer getNbTransactions(){
        Integer total = 0;
        for (Agent agent: getAgents()){
            total += agent.getProduct().getSales();
        }
        return total;
    }

    public String getConnectedStatesCsv(){
        String csv = "";
        for (State connectedState: connectedStates){
            csv += connectedState + ",";
        }
        return csv;
    }

    /* ==================================
     * ==== Setters
     * ================================== */
    /**
     * @param agent Agent to add to the population of the State
     */
    public void addAgent(Agent agent){
        agents.add(agent);
    }

    /**
     * @param state Connected State to the current one
     */
    public void addConnectedState(State state){
        connectedStates.add(state);
    }

    /* ==================================
     * ==== Methods: csv
     * ================================== */
    public static String csvHeader(){
        return "Id,PopSize,VAT,Levy,Tariff,WealthTax,AllowanceType,AllowanceValue,Money,PopTotalMoney,NbTransactions,ConnectedStates";
    }

    @Override
    public Stream<String> properties(){
        return Stream.of(
                id,
                String.valueOf(getAgents().size()),
                vat.getValue().toString(),
                levy.getValue().toString(),
                tariff.getValue().toString(),
                wealthTax.getValue().toString(),
                allowance.getType().toString(),
                allowance.getPercentage().toString(),
                money.toString(),
                getAgentsTotalMoney().toString(),
                getNbTransactions().toString(),
                getConnectedStatesCsv());
    }

    /* ==================================
     * ==== Methods: money
     * ================================== */
    /**
     * @param value Money to add to the State's money
     */
    public void addMoney(float value){
        assert (value >= 0);
        this.money += value;
    }

    /**
     * @param value Money to subtract from the State's money
     */
    public void subtractMoney(float value){
        assert (this.money >= value);
        this.money -= value;
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * Collect all taxes the State has implemented
     */
    public void collectTaxes(){
        wealthTax.collect();
        levy.collect();
    }

    /**
     * Distribute allowances to all Agents
     */
    public void distributeAllowances(){
        allowance.distribute();
    }

    /**
     * Represent a step in the State's lifetime where it can perform actions
     */
    public void tick(int currentTick){
        if (currentTick % params.NB_TICKS_COLLECT_TAXES == 0)
            collectTaxes();
        if (currentTick % params.NB_TICKS_DISTRIBUTE_ALLOWANCES == 0)
            distributeAllowances();
    }
}
