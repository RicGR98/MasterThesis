package rgomesro.models.entities;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents the base for the Models who need an Id
 */
public abstract class Entity {
    protected final String id;

    /* ==================================
     * ==== Constructors
     * ================================== */
    public Entity(int id){
        this.id = String.valueOf(id);
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.getId();
    }

    /* ==================================
     * ==== Methods: csv
     * ================================== */
    /**
     * @return Stream of all the property values of an Entity
     */
    public abstract Stream<String> properties();

    /**
     * @return representation of a csv row of all the property values of an Entity
     */
    public String toCsv() {
        return properties()
                .map(value -> value.replaceAll("\"", "\"\""))
                .map(value -> Stream.of("\"", ",").anyMatch(value::contains) ? "\"" + value + "\"" : value)
                .collect(Collectors.joining(","));
    }
}
