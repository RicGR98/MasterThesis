package rgomesro.models.entities;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Represents a Cluster (group) of States allowing some advantages
 */
public class Cluster extends Entity {
    private final ArrayList<State> states;
    /* ==================================
     * ==== Constructors
     * ================================== */
    public Cluster() {
        this.states = new ArrayList<>();
    }

    /* ==================================
     * ==== Getters & setters
     * ================================== */
    public ArrayList<State> getStates() {
        return states;
    }

    public void addState(State state){
        this.states.add(state);
    }

    public Boolean contains(State state){
        return states.contains(state);
    }

    /* ==================================
     * ==== Methods: csv
     * ================================== */
    public static String csvHeader() {
        return "Id,NumberStates";
    }

    @Override
    public Stream<String> properties() {
        return Stream.of(id, String.valueOf(states.size()));
    }
}
