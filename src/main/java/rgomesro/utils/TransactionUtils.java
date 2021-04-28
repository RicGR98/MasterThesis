package rgomesro.utils;

import rgomesro.models.entities.Agent;
import rgomesro.models.entities.State;

public class TransactionUtils {
    public static void make(Agent from, Agent to, float amount){
        from.subtractMoney(amount);
        to.addMoney(amount);
    }

    public static void make(Agent from, State to, float amount){
        from.subtractMoney(amount);
        to.addMoney(amount);
    }

    public static void make(State from, Agent to, float amount){
        make(to, from, -amount);
    }
}
