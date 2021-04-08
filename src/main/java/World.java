import java.util.ArrayList;
import java.util.stream.Collectors;

public class World {
    private int nbTicks;
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
        this.market = new Market(agents);
    }

    public void tick(){
        agents.forEach(Agent::tick);
        states.forEach(State::tick);
    }

    public void run(){
        for (int i = 0; i < nbTicks; i++) {
            this.tick();
            if (i%100 == 0)
                System.out.println(i + " " + market.getProductCount());
        }
    }

    public void saveToCsv(){
        saveAgentsToCsv();
        saveStatesToCsv();
    }

    public void saveAgentsToCsv(){
        String csv = agents.stream()
                .map(Agent::toCsv)
                .collect(Collectors.joining(System.getProperty("line.separator")));
        Utils.writeToFile("res/agents.csv", csv);
    }

    public void saveStatesToCsv(){
        String csv = states.stream()
                .map(State::toCsv)
                .collect(Collectors.joining(System.getProperty("line.separator")));
        Utils.writeToFile("res/states.csv", csv);
    }
}
