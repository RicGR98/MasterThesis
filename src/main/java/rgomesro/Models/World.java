package rgomesro.Models;

import java.util.ArrayList;
import java.util.stream.Collectors;
import rgomesro.Utils;

public class World {
    private final int nbTicks;
    public final Market market;
    private final ArrayList<State> states;
    private final ArrayList<Agent> agents;

    public World(int nbTicks, int nbStates, int nbAgents){
        this.nbTicks = nbTicks;
        this.states = new ArrayList<>(nbStates);
        for (int i = 0; i < nbStates; i++) {
            this.states.add(new State());
        }
        this.agents = new ArrayList<>(nbAgents);
        for (int i = 0; i < nbAgents; i++) {
            Agent agent = new Agent(this, Utils.randomChoice(states), Utils.getRandomInt(0, 1000));
            this.agents.add(agent);
        }
        this.market = new Market(states, agents);
    }

    public void tick(){
        agents.forEach(Agent::tick);
        states.forEach(State::tick);
    }

    public void run(){
        for (int t = 0; t < nbTicks; t++) {
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
