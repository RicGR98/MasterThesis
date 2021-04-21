package rgomesro.models;

import rgomesro.models.entities.Agent;
import rgomesro.models.entities.Market;
import rgomesro.models.entities.Product;
import rgomesro.models.entities.State;
import rgomesro.utils.RandomUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a Market where Products are sold by Agent's
 */
public class WorldMarket {
    public final World world;
    public final HashMap<State, Market> markets;

    /* ==================================
     * ==== Constructors
     * ================================== */
    public WorldMarket(World world){
        this.world = world;
        markets = new HashMap<>();
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    /**
     * @return Total stocks of all products of the World's Market
     */
    public int getProductCount(){
        int res = 0;
        for (State state : markets.keySet()) {
            res += state.getMarket().getProductCount();
        }
        return res;
    }

    /* ==================================
     * ==== Methods: products
     * ================================== */
    /**
     * Initialize the Market's products classified by State and Product type
     */
    public void init(){
        for (State state : world.getStates()) {
            markets.put(state, state.getMarket());
            state.getMarket().initMarket();
        }
    }

    /**
     * Checks if a buyer is allowed to buy a Product
     * @param buyer Buyer of the Product
     * @param product Product we want to buy
     * @return True if the Buyer can buy the Product, else false
     */
    public Boolean isProductBuyable(Agent buyer, Product product){
        //TODO
        return null;
    }

    /**
     * @param buyer Agent who wants to buy a product on the market
     * @param type Type of product the agent wants to buy
     * @return List of filtered product according to State, Type, Stocks, Price, ...
     */
    public List<Product> getFilteredProducts(Agent buyer, int type){
        var res = buyer.getState().getMarket().getFilteredProducts(buyer, type);
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
        return this.buy(buyer, Product.getRandomType());
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
