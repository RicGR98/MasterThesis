package rgomesro.models.taxes;

import rgomesro.models.entities.Agent;
import rgomesro.models.entities.Product;
import rgomesro.utils.RandomUtils;

import static rgomesro.Params.State.Tax.MAX_TARIFF;
import static rgomesro.Params.State.Tax.MIN_TARIFF;

public class Tariff extends Tax {
    /* ==================================
     * ==== Constructors
     * ================================== */
    public Tariff(Float value){
        super(value);
    }

    public Tariff(){
        this(RandomUtils.getFloat(MIN_TARIFF, MAX_TARIFF));
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    public Float getValue() {
        return value;
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * @param product Product from which we collect the Tax
     * @return The value of the Tax for this Product
     */
    public Float compute(Agent buyer, Product product){
        var buyerState = buyer.getState();
        var productState = product.getProducer().getState();
        if (buyerState == productState ||
                buyerState.getConnectedStates().contains(productState)){
            //No tariff because seller and buyer are in the same State (or their States are connected)
            return 0f;
        }
        return compute(product); //Tariff computed as normal Tax
    }
}
