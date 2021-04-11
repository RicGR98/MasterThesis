package rgomesro;

import rgomesro.Models.Agent;
import rgomesro.Models.State;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static rgomesro.Constants.World.*;

public class World {
    private final Market market;
    private final ArrayList<State> states;
    private final ArrayList<Agent> agents;

    public World(){
        this.market = new Market();
        this.states = new ArrayList<>(NB_STATES);
        for (int i = 0; i < NB_STATES; i++) {
            this.states.add(new State());
        }
        this.agents = new ArrayList<>(NB_AGENTS);
        for (int i = 0; i < NB_AGENTS; i++) {
            Agent agent = new Agent(market, states.get(i % NB_STATES), 1000);
            this.agents.add(agent);
        }
        market.initMarket(states, agents);
    }

    public void tick(){
        agents.forEach(Agent::tick);
        states.forEach(State::tick);
    }

    public void run(){
        for (int t = 0; t < NB_TICKS; t++) {
            this.tick();
            if (t % 100 == 0)
                System.out.println(t + " " + market.getProductCount());
        }
    }

    public void saveToCsv(){
        saveAgentsToCsv();
        saveStatesToCsv();
    }

    public void saveAgentsToCsv(){
        String csv = Agent.csvColumnsNames() + "\n";
        csv += agents.stream()
                .map(Agent::toCsv)
                .collect(Collectors.joining("\n"));
        Utils.writeToFile("res/agents.csv", csv);
    }

    public void saveStatesToCsv(){
        String csv = State.csvColumnsNames() + "\n";
        csv += states.stream()
                .map(State::toCsv)
                .collect(Collectors.joining("\n"));
        Utils.writeToFile("res/states.csv", csv);
    }
}
