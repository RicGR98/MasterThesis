package rgomesro.models.taxes;

import rgomesro.Params;
import rgomesro.utils.RandomUtils;

/**
 * Represents a periodic Tax from a State on all its Agents
 */
public class Levy extends Tax {
    /* ==================================
     * ==== Constructors
     * ================================== */
    public Levy(Float value) {
        super(value);
    }

    public Levy(){
        this(
                RandomUtils.getFloat(
                        Params.getInstance().tax.MIN_LEVY,
                        Params.getInstance().tax.MAX_LEVY)
        ); // TODO: Analyze
    }
}
