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
     * @param clusterSize Size of the Cluster of States
     * @param probAttached Probability of two States being connected to each other
     */
    public Connections(ArrayList<State> states, int clusterSize, float probAttached) {
        this.states = states;
        this.clusterMembers = new ArrayList<>();
        this.graph = new SimpleGraph<>(DefaultEdge.class);
        for (State state: states){
            graph.addVertex(state);
        }
        this.createCluster(clusterSize);
        this.createEdges(probAttached);
    }

    public Connections(ArrayList<State> states){
        this(states, CLUSTER_SIZE, PROB_CONNECTION);
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * Create a cluster where all States are connected to each other
     * @param size How many State members are in the Cluster
     */
    public void createCluster(int size){
        assert (size < states.size());
        while (clusterMembers.size() != size){
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
     * @param probability probability of two States being connected
     */
    public void createEdges(float probability){
        var freeStates = new ArrayList<>(states);
        freeStates.removeAll(clusterMembers);
        for (State state: freeStates){
            if (RandomUtils.getRandom() < probability){
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
        graph.edgeSet().forEach(edge -> {
            State state1 = graph.getEdgeSource(edge);
            State state2 = graph.getEdgeTarget(edge);
            state1.addConnectedState(state2);
            state2.addConnectedState(state1);
        });
    }
}
