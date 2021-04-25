package rgomesro.models;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import rgomesro.Params;
import rgomesro.models.entities.State;
import rgomesro.utils.RandomUtils;

import java.util.ArrayList;


public class Connections {
    private final Params.Connections params;
    private final ArrayList<State> states;
    private final Graph<State, DefaultEdge> graph;
    private final ArrayList<State> clusterMembers;

    /* ==================================
     * ==== Constructors
     * ================================== */
    /**
     * @param states List of States
     */
    public Connections(ArrayList<State> states) {
        this.params = Params.getInstance().connections;
        this.states = states;
        this.clusterMembers = new ArrayList<>();
        this.graph = new SimpleGraph<>(DefaultEdge.class);
        for (State state: states){
            graph.addVertex(state);
        }
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * Create a cluster where all States are connected to each other
     */
    private void createCluster(){
        assert (params.CLUSTER_SIZE <= states.size());
        while (clusterMembers.size() != params.CLUSTER_SIZE){
            State member = RandomUtils.choose(states);
            if (!clusterMembers.contains(member))
                clusterMembers.add(member);
        }
        for (State state: clusterMembers){
            for (State state2: clusterMembers){
                if (state != state2)
                    graph.addEdge(state, state2);
            }
        }
    }

    /**
     * Create random edges between States
     */
    private void createEdges(){
        var freeStates = new ArrayList<>(states);
        freeStates.removeAll(clusterMembers);
        for (State state: freeStates){
            if (RandomUtils.getRandom() < params.PROB_CONNECTION){
                State state2 = RandomUtils.choose(freeStates);
                if (state != state2)
                    graph.addEdge(state, state2);
            }
        }
    }

    /**
     * For each State, add all States it is connected to
     * in the graph.
     */
    public void updateConnectedStates(){
        this.createCluster();
        this.createEdges();
        graph.edgeSet().forEach(edge -> {
            State state1 = graph.getEdgeSource(edge);
            State state2 = graph.getEdgeTarget(edge);
            state1.addConnectedState(state2);
            state2.addConnectedState(state1);
        });
    }
}
