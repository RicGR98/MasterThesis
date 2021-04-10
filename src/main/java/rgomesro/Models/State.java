package rgomesro.Models;

import rgomesro.Constants;
import rgomesro.Models.Entity;
import rgomesro.Taxes.VAT;
import rgomesro.Utils;

import java.util.stream.Stream;

public class State extends Entity {
    private Float money = 0f;
    private final VAT vat;

    public State() {
        super("State");
        this.vat = new VAT(Utils.getRandomFloat(Constants.State.MIN_VAT, Constants.State.MAX_VAT));
    }

    public VAT getVat(){
        return this.vat;
    }

    public static String csvColumnsNames(){
        return "Name,Vat,Money";
    }

    public Stream<String> csvFields(){
        return Stream.of(getName(), vat.getValue().toString(), money.toString());
    }

    public void addMoney(float money){
        this.money += money;
    }

    public void collectTaxes(){

    }

    public void tick(){

    }
}
