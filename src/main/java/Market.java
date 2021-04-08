import java.util.ArrayList;

public class Market {
    private final ArrayList<Product> products;
    private int nbTransactions = 0;

    public Market(){
        this.products = new ArrayList<>();
    }

    public void produce(Product product){
        this.products.add(product);
    }

    /**
     * @param buyer Agent that buys a product
     * @param type Type of the product wished to be bought
     * @return List of products which match multiple criteria (type, price, ...)
     */
    public ArrayList<Product> getProductsMatch(Agent buyer, Product.Type type){
        var res = new ArrayList<Product>();
        for(Product product: products){
            if (product.getType() != type) continue;
            if (product.getPrice() > buyer.getMoney()) continue;
            if (product.getProducer().getState() != buyer.getState()) continue;
            res.add(product);
        }
        return res;
    }

    /**
     * @param buyer Consumer who wishes to buy a product on the Market
     * @param type Type of product the buyer wishes to buy
     */
    public void buy(Agent buyer, Product.Type type){
        var possibleProducts = getProductsMatch(buyer, type);
        if (possibleProducts.size() == 0) return;
        var chosenProduct = Utils.randomChoice(possibleProducts);
        var price = chosenProduct.getPrice();
        var seller = chosenProduct.getProducer();
        seller.addMoney(price);
        buyer.subtractMoney(price);
        this.products.remove(chosenProduct);
        System.out.println("Transaction: " + buyer +
                " bought from " + seller + " for " + chosenProduct.getPrice());
        nbTransactions++;
    }
}
