package rgomesro.models;

import org.apache.commons.math3.util.Pair;
import rgomesro.models.entities.Agent;
import rgomesro.models.entities.Market;
import rgomesro.models.entities.Product;
import rgomesro.models.entities.State;
import rgomesro.utils.RandomUtils;
import rgomesro.utils.TransactionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a Market where Products are sold by Agent's
 */
public class WorldMarket {
    private final World world;
    private final HashMap<State, Market> markets;

    /* ==================================
     * ==== Constructors
     * ================================== */
    public WorldMarket(World world){
        this.world = world;
        this.markets = new HashMap<>();
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

    public int getNbSales(){
        int total = 0;
        for (State state: world.getStates()){
            total += state.getNbSales();
        }
        return total;
    }

    public int getNbPurchases(){
        int total = 0;
        for (State state: world.getStates()){
            total += state.getNbPurchases();
        }
        return total;
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
     * with their inverse prices (for weighted random)
     */
    public List<Pair<Product, Double>> getFilteredProducts(Agent buyer, int type){
        List<Pair<Product, Double>> products = new ArrayList<>();
        for (State state: world.getStates()){
            products.addAll(state.getMarket().getFilteredProducts(buyer, type));
        }
        return products;
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
        //Filter Products with a List<Pair<Product, Double>>
        var matchingProducts = getFilteredProducts(buyer, type);
        if (matchingProducts.size() == 0) return false;

        //Get list of Products and cheapest one among them
        var products = new ArrayList<Product>();
        Product cheapest = matchingProducts.get(0).getFirst();
        for (Pair<Product, Double> productDoublePair: matchingProducts){
            Product product = productDoublePair.getFirst();
            products.add(product);
            if (product.getSellingPrice() < cheapest.getSellingPrice()){
                cheapest = product;
            }
        }

        //Choose Product according to Product choice method
        Product product;
        switch (world.getProductChoice()){
            case RANDOM:
                product = RandomUtils.choose(products);
                break;
            case WEIGHTED_RANDOM:
                product = RandomUtils.weightedChoose(matchingProducts);
                break;
            default: //CHEAPEST
                product = cheapest;
                break;
        }

        transaction(buyer, product);
        return true;
    }

    public Boolean buy(Agent buyer){
        return this.buy(buyer, Product.getRandomType());
    }

    /**
     * Perform a transaction of a Product between a buyer and a seller
     * taking into accounts taxes (VAT and Tariff), GDP, black economy (no taxes paid)
     * @param buyer Consumer who wishes to buy a product on the Market
     * @param product Product the consumer chose to buy
     */
    public void transaction(Agent buyer, Product product){
        product.sell();
        var seller = product.getProducer();
        var sellerState = seller.getState();
        var buyerState = buyer.getState();
        sellerState.getMarket().incrementNbSales();
        buyerState.getMarket().incrementNbPurchases();
        TransactionUtils.make(buyer, seller, product.getSellingPrice());
        //Probability of black transaction (no VAT, no Tariff, no GDP)
        if (buyerState.getBlack() > RandomUtils.getRandom()){
            return; //Perform black transaction by skipping following instructions
        }
        TransactionUtils.make(buyer, buyerState, buyerState.getTariff().compute(buyer, product));
        TransactionUtils.make(buyer, sellerState, sellerState.getVat().compute(product));
        sellerState.addToGdp(product.getSellingPrice());
    }
}
