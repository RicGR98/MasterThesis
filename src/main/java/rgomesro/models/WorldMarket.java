package rgomesro.models;

import rgomesro.models.entities.Agent;
import rgomesro.models.entities.Market;
import rgomesro.models.entities.Product;
import rgomesro.models.entities.State;
import rgomesro.utils.RandomUtils;

import java.util.ArrayList;
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
     * @param buyer Agent who wants to buy a product on the market
     * @param type Type of product the agent wants to buy
     * @return List of filtered product according to State, Type, Stocks, Price, ...
     */
    public List<Product> getFilteredProducts(Agent buyer, int type){
        var res = new ArrayList<Product>();
        for (State state: world.getStates()){
            res.addAll(state.getMarket().getFilteredProducts(buyer, type));
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
        return this.buy(buyer, Product.getRandomType());
    }

    /**
     * @param buyer Consumer who wishes to buy a product on the Market
     * @param product Product the consumer chose to buy
     */
    public void transaction(Agent buyer, Product product){
        var seller = product.getProducer();
        var sellerState = seller.getState();
        var buyerState = buyer.getState();
        seller.addMoney(product.getSellingPrice());
        sellerState.addMoney(sellerState.getVat().compute(product));
        buyerState.addMoney(buyerState.getTariff().compute(product));
        buyer.subtractMoney(getTotalPrice(buyer, product));
        product.sell();
    }

    /**
     * @param buyer Buyer of the Product
     * @param product Product we want to buy
     * @return Total price of the product including taxes
     */
    public static Float getTotalPrice(Agent buyer, Product product){
        var stateVAT = product.getProducer().getState().getVat();
        var stateTariff = buyer.getState().getTariff();
        var totalPrice = product.getSellingPrice();
        totalPrice +=  stateVAT.compute(product);
        totalPrice += stateTariff.compute(buyer, product);
        return totalPrice;
    }
}
