package rgomesro;

import rgomesro.Models.Agent;
import rgomesro.Models.Product;
import rgomesro.Models.State;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static rgomesro.Constants.Product.NB_DIFF_PRODUCTS;

public class Market {
    // Market.get(State).get(Product.Type) = ArrayList<Product>
    public final HashMap<State, HashMap<Integer, ArrayList<Product>>> products;

    public Market(){
        products = new HashMap<>();
    }

    public void initMarket(ArrayList<State> states, ArrayList<Agent> agents){
        for (State state : states) {
            products.put(state, new HashMap<>());
            for (int type = 0; type < NB_DIFF_PRODUCTS; type++){
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
            for (int type = 0; type < NB_DIFF_PRODUCTS; type++){
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
    public List<Product> getFilteredProducts(Agent buyer, int type){
        var res = new ArrayList<Product>();
        products.get(buyer.getState()).get(type).forEach(product -> {
            if (product.getStock() <= 0) return;
            var stateVAT = product.getProducer().getState().getVat();
            if (!buyer.hasEnoughMoney(product.getPrice() +  stateVAT.compute(product))) return;
            res.add(product);
        });
        res.sort(Comparator.comparing(Product::getPrice)); // From lowest to highest price
        return res;
    }

    /**
     * @param buyer Consumer who wishes to buy a product on the Market
     * @param type Type of product the buyer wishes to buy
     */
    public void buy(Agent buyer, int type){
        var matchingProducts = getFilteredProducts(buyer, type);
        if (matchingProducts.size() == 0) return;
        var product = Utils.randomChoice(matchingProducts);
        transaction(buyer, product);
    }

    public void buy(Agent buyer){
        this.buy(buyer, Utils.getRandomInt(0, NB_DIFF_PRODUCTS));
    }

    /**
     * @param buyer Consumer who wishes to buy a product on the Market
     * @param product Product the consumer chose to buy
     */
    public void transaction(Agent buyer, Product product){
        var seller = product.getProducer();
        var state = seller.getState();
        seller.addMoney(product.getPrice());
        state.addMoney(state.getVat().compute(product));
        buyer.subtractMoney(product.getPrice());
        product.sell();
    }
}
