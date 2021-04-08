import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Market {
    public final ArrayList<Product> products;

    public Market(ArrayList<Agent> agents){
        products = new ArrayList<>();
        agents.forEach(agent -> products.add(agent.getProduct()));
    }

    public int getProductCount(){
        return products.stream().mapToInt(Product::getStock).sum();
    }

    public List<Product> getMatchingProducts(Agent buyer, Product.Type type){
        return products.stream()
                .filter(product -> product.getStock() > 0)
                .filter(product -> product.getType() == type)
                .filter(product -> product.getProducer().getState() == buyer.getState())
                .collect(Collectors.toList());
    }

    /**
     * @param buyer Consumer who wishes to buy a product on the Market
     * @param type Type of product the buyer wishes to buy
     */
    public void buy(Agent buyer, Product.Type type){
        var matchingProducts = getMatchingProducts(buyer, type);
        if (matchingProducts.size() == 0) return;
        var chosenProduct = Utils.randomChoice(matchingProducts);
        var price = chosenProduct.getPrice();
        var seller = chosenProduct.getProducer();
        seller.addMoney(price);
        buyer.subtractMoney(price);
        chosenProduct.decrementStock();
    }
}
