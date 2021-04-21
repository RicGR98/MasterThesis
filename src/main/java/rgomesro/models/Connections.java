package rgomesro.models;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import rgomesro.models.entities.State;
import rgomesro.utils.RandomUtils;

import java.util.ArrayList;

import static rgomesro.Params.Connections.CLUSTER_SIZE;
import static rgomesro.Params.Connections.PROB_CONNECTION;

public class Connections {
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
        assert (CLUSTER_SIZE <= states.size());
        while (clusterMembers.size() != CLUSTER_SIZE){
            clusterMembers.add(RandomUtils.choose(states));
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
            if (RandomUtils.getRandom() < PROB_CONNECTION){
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
