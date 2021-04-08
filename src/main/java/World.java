import java.util.ArrayList;
import java.util.stream.Collectors;

public class World {
    private final Market market;
    private final ArrayList<State> states;
    private final ArrayList<Agent> agents;

    public World(int nbStates, int nbAgents){
        this.market = new Market();
        this.states = new ArrayList<>(nbStates);
        this.agents = new ArrayList<>(nbAgents);
        for (int i = 0; i < nbStates; i++) {
            this.states.add(new State());
        }
        for (int i = 0; i < nbAgents; i++) {
            Agent agent = new Agent(this, market, Utils.randomChoice(states), Utils.getRandomInt(0, 1000));
            this.agents.add(agent);
        }
    }

    public void tick(){
        agents.forEach(Agent::tick);
        states.forEach(State::tick);
    }

    public void run(int nbTicks){
        long startTime = System.nanoTime();
        for (int i = 0; i < nbTicks; i++) {
            this.tick();
        }
        long endTime = System.nanoTime();
        System.out.println((endTime - startTime)/1000000000.0);
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
