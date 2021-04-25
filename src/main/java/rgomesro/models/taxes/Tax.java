package rgomesro.models.taxes;

import rgomesro.models.entities.Product;

/**
 * Represents the base for all the Taxes
 */
public abstract class Tax {
    protected final Float value;

    protected Tax(Float value) {
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
    public float compute(Product product){
        return product.getSellingPrice() * getValue();
    }
}
