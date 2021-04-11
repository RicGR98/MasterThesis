package rgomesro.Models;

import rgomesro.Taxes.VAT;
import rgomesro.Utils;

import java.util.stream.Stream;

import static rgomesro.Constants.State.MAX_VAT;
import static rgomesro.Constants.State.MIN_VAT;

public class State extends Entity {
    private Float money = 0f;
    private final VAT vat;

    public State() {
        super();
        this.vat = new VAT(Utils.getRandomFloat(MIN_VAT, MAX_VAT));
    }

    public VAT getVat(){
        return this.vat;
    }

    public static String csvColumnsNames(){
        return "Id,VAT,Money";
    }

    public Stream<String> csvFields(){
        return Stream.of(getId(), vat.getValue().toString(), money.toString());
    }

    public void addMoney(float money){
        this.money += money;
    }

    public void collectTaxes(){

    }

    public void tick(){

    }
}
