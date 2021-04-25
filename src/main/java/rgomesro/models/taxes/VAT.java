package rgomesro.models.taxes;

import rgomesro.utils.RandomUtils;

import static rgomesro.Params.State.Tax.MAX_VAT;
import static rgomesro.Params.State.Tax.MIN_VAT;

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
        this(RandomUtils.getFloat(MIN_VAT, MAX_VAT)); // TODO: Analyze
    }
}
