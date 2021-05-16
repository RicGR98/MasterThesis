package rgomesro.models.taxes;

import rgomesro.Params;
import rgomesro.models.entities.Product;
import rgomesro.utils.RandomUtils;


/**
 * Represents the Value Added Tax on a Product
 */
public class VAT extends Tax {
    /* ==================================
     * ==== Constructors
     * ================================== */
    /**
     * @param value Percentage value of the VAT
     */
    public VAT(float value){
        super(value);
    }

    public VAT(){
        this(
                RandomUtils.getFloat(
                        Params.getInstance().tax.MIN_VAT,
                        Params.getInstance().tax.MAX_VAT)
        ); // TODO: Analyze
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * @param product Product from which we collect the VAT
     * @return The value of the VAT for this Product
     */
    @Override
    public Float compute(Product product) {
        return super.compute(product);
    }
}
