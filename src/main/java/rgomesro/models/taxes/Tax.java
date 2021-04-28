package rgomesro.models.taxes;

import rgomesro.models.entities.Agent;
import rgomesro.models.entities.Product;

/**
 * Represents the base for all the Taxes
 */
public abstract class Tax {
    protected final Float value; // in [0, 1]

    protected Tax(Float value) {
        assert (value >= 0f && value <= 1f);
        this.value = value;
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
    public Float compute(Product product){
        return product.getSellingPrice() * getValue();
    }

    /**
     * @param agent Agent taxed
     * @return Amount of the value the Agent should pay as Tax
     */
    public Float compute(Agent agent){
        return agent.getMoney() * getValue();
    }
}
