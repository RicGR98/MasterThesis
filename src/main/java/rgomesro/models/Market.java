package rgomesro.models;

import rgomesro.models.entities.Agent;
import rgomesro.models.entities.Product;
import rgomesro.models.entities.State;
import rgomesro.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static rgomesro.Constants.Product.NB_DIFF_PRODUCTS;

/**
 * Represents a Market where Products are sold by Agent's
 */
public class Market {
    public final World world;
    public final HashMap<State, HashMap<Integer, ArrayList<Product>>> products; // Market.get(State).get(Product.Type) = ArrayList<Product>

    /* ==================================
     * ==== Constructors
     * ================================== */
    public Market(World world){
        this.world = world;
        products = new HashMap<>();
    }

    /* ==================================
     * ==== Getters
     * ================================== */
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

    /* ==================================
     * ==== Methods: products
     * ================================== */
    /**
     * Initialize the Market's products classified by State and Product type
     */
    public void initMarket(){
        for (State state : world.getStates()) {
            products.put(state, new HashMap<>());
            for (int type = 0; type < NB_DIFF_PRODUCTS; type++){
                products.get(state).put(type, new ArrayList<>());
                for (Agent agent: state.getAgents()){
                    if (agent.getProduct().getType() == type){
                        products.get(state).get(type).add(agent.getProduct());
                    }
                }
            }
        }
    }

    /**
     * Checks if a buyer is allowed to buy a Prodct
     * @param buyer Buyer of the Product
     * @param product Product we want to buy
     * @return True if the Buyer can buy the Product, else false
     */
    public Boolean isProductBuyable(Agent buyer, Product product){
        if (product.getStock() <= 0)
            return false;
        var stateVAT = product.getProducer().getState().getVat();
        if (!buyer.hasEnoughMoney(product.getSellingPrice() +  stateVAT.compute(product)))
            return false;
        return true;
    }

    /**
     * @param buyer Agent who wants to buy a product on the market
     * @param type Type of product the agent wants to buy
     * @return List of filtered product according to State, Type, Stocks, Price, ...
     */
    public List<Product> getFilteredProducts(Agent buyer, int type){
        var res = new ArrayList<Product>();
        if (!world.getCluster().contains(buyer.getState())){ //Buyer's State not in Cluster
            products.get(buyer.getState()).get(type).forEach(product -> {
                if (isProductBuyable(buyer, product))
                    res.add(product);
            });
        } else { //Buyer's State in the Cluster, we visit all the other States in it too
            for (State state: world.getCluster().getStates()){
                products.get(state).get(type).forEach(product -> {
                    if (isProductBuyable(buyer, product))
                        res.add(product);
                });
            }
        }
        res.sort(Comparator.comparing(Product::getSellingPrice)); // From lowest to highest price
        return res;
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
    /**
     * @param buyer Consumer who wishes to buy a product on the Market
     * @param type Type of product the buyer wishes to buy
     * @return True if a product was bought, else false
     */
    public boolean buy(Agent buyer, int type){
        var matchingProducts = getFilteredProducts(buyer, type);
        if (matchingProducts.size() == 0) return false;
        var product = RandomUtils.choose(matchingProducts);
        transaction(buyer, product);
        return true;
    }

    public Boolean buy(Agent buyer){
        return this.buy(buyer, RandomUtils.getInt(0, NB_DIFF_PRODUCTS));
    }

    /**
     * @param buyer Consumer who wishes to buy a product on the Market
     * @param product Product the consumer chose to buy
     */
    public void transaction(Agent buyer, Product product){
        var seller = product.getProducer();
        var state = seller.getState();
        seller.addMoney(product.getSellingPrice());
        state.addMoney(state.getVat().compute(product));
        buyer.subtractMoney(product.getSellingPrice());
        product.sell();
    }
}
