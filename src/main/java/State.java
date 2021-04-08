import java.util.stream.Stream;

public class State extends Entity{
    private float money = 0;

    public State() {
        super("State");
    }

    public Stream<String> csvFields(){
        return Stream.of(getName());
    }

    public void collectVAT(){

    }

    public void tick(){

    }
}
