import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Market {
    // Market.get(State).get(Product.Type) = ArrayList<Product>
    public final HashMap<State, HashMap<Product.Type, ArrayList<Product>>> products;

    public Market(ArrayList<State> states, ArrayList<Agent> agents){
        products = new HashMap<>();
        for (State state : states) {
            products.put(state, new HashMap<>());
            for (Product.Type type: Product.getTypes()){
                products.get(state).put(type, new ArrayList<>());
                for (Agent agent: agents){
                    if (agent.getState() == state && agent.getProduct().getType() == type){
                        products.get(state).get(type).add(agent.getProduct());
                    }
                }
            }
        }
    }

    /**
     * @return Total stocks of all products of the market
     */
    public int getProductCount(){
        int res = 0;
        for (State state : products.keySet()) {
            for (Product.Type type: Product.getTypes()){
                for (Product product: products.get(state).get(type)){
                    res += product.getStock();
                }
            }
        }
        return res;
    }

    /**
     * @param buyer Agent who wants to buy a product on the market
     * @param type Type of product the agent wants to buy
     * @return List of filtered product according to State, Type, Stocks, Price, ...
     */
    public List<Product> getFilteredProducts(Agent buyer, Product.Type type){
        return products
                .get(buyer.getState())
                .get(type)
                .stream()
                .filter(product -> product.getStock() > 0)
                .filter(product -> product.getPrice() < buyer.getMoney())
                .collect(Collectors.toList());
    }

    /**
     * @param buyer Consumer who wishes to buy a product on the Market
     * @param type Type of product the buyer wishes to buy
     */
    public void buy(Agent buyer, Product.Type type){
        var matchingProducts = getFilteredProducts(buyer, type);
        if (matchingProducts.size() == 0) return;
        var chosenProduct = Utils.randomChoice(matchingProducts);
        var price = chosenProduct.getPrice();
        var seller = chosenProduct.getProducer();
        seller.addMoney(price);
        buyer.subtractMoney(price);
        chosenProduct.decrementStock();
    }
}
