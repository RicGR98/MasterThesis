package rgomesro.Models;

import com.github.javafaker.Faker;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.capitalize;

public abstract class Entity {
    private final String id;

    public Entity(){
        this.id = capitalize(new Faker().lorem().fixedString(5));
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.id;
    }

    public abstract Stream<String> csvFields();

    public String toCsv() {
        return csvFields()
                .map(value -> value.replaceAll("\"", "\"\""))
                .map(value -> Stream.of("\"", ",").anyMatch(value::contains) ? "\"" + value + "\"" : value)
                .collect(Collectors.joining(","));
    }
}
