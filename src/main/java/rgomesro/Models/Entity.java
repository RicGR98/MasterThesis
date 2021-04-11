package rgomesro.Models;

import com.github.javafaker.Faker;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.capitalize;

public abstract class Entity {
    protected final String id;

    /* ==================================
     *  ==== Constructors
     *  ================================== */
    /**
     * Represents the base for the Models who need an Id
     */
    public Entity(){
        this.id = capitalize(new Faker().lorem().fixedString(5));
    }

    /* ==================================
     *  ==== Getters
     *  ================================== */
    public String getId() {
        return id;
    }

    /* ==================================
     *  ==== Methods: csv
     *  ================================== */
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
