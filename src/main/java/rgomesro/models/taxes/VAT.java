package rgomesro.models.taxes;

import rgomesro.models.entities.Product;

import static rgomesro.Constants.State.Tax.VAL_VAT;

/**
 * Represents the Value Added Tax on a Product
 */
public class VAT extends Tax {
    private final float value;
    /* ==================================
     * ==== Constructors
     * ================================== */
    /**
     * @param value Percentage value of the VAT
     */
    public VAT(float value){
        this.value = value;
    }

    public VAT(){
        this(VAL_VAT);
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
     * @param product Product from which we collect the tax
     * @return The value of the VAT for this Product
     */
    public float compute(Product product){
        return product.getSellingPrice() * getValue();
    }
}
