package rgomesro.Taxes;

import rgomesro.Models.Product;

public class VAT extends Tax {
    private final float value;

    public VAT(float value){
        this.value = value;
    }

    public Float getValue() {
        return value;
    }

    public float compute(Product product){
        return product.getPrice() * value;
    }
}
