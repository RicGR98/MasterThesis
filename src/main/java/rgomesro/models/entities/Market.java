package rgomesro.models.entities;

import rgomesro.Params;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;


public class Market extends Entity {
    private final Params params;
    private final State state;
    private final ArrayList<ArrayList<Product>> products;
    private Integer nbSales;
    private Integer nbPurchases;

    /* ==================================
     * ==== Constructors
     * ================================== */
    public Market(State state) {
        super(state.getId());
        this.params = Params.getInstance();
        this.state = state;
        this.products = new ArrayList<>();
        this.nbSales = 0;
        this.nbPurchases = 0;
    }

    /* ==================================
     * ==== Getters
     * ================================== */
    /**
     * @return Total stocks of all products of the State's Market
     */
    public int getProductCount(){
        int total = 0;
        for (ArrayList<Product> typeProduct: products){
            for (Product product: typeProduct){
                total += product.getStock();
            }
        }
        return total;
    }

    public Integer getNbSales() {
        return nbSales;
    }

    public Integer getNbPurchases() {
        return nbPurchases;
    }

    /* ==================================
     * ==== Methods: csv
     * ================================== */
    public static String csvHeader() {
        return "Id";
    }

    @Override
    public Stream<String> properties(){
        return Stream.of(
                id
        );
    }

    /* ==================================
     * ==== Methods: Products
     * ================================== */
    /**
     * Initialize the Market's products classified Product type
     */
    public void initMarket(){
        //Create keys (types of Product)
        for (int type = 0; type < params.product.NB_DIFF_PRODUCTS; type++){
            products.add(new ArrayList<>());
        }
        //Add Products according to their type
        for (Agent agent: state.getAgents()){
            Product product = agent.getProduct();
            products.get(product.getType()).add(agent.getProduct());
        }
    }

    /* ==================================
     * ==== Methods: actions
     * ================================== */
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

    /**
     * Checks if a buyer is allowed to buy a Product
     * @param buyer Buyer of the Product
     * @param product Product we want to buy
     * @return True if the Buyer can buy the Product, else false
     */
    public static Boolean isProductBuyable(Agent buyer, Product product){
        if (product.getStock() <= 0) //Check stocks
            return false;
        if (buyer == product.getProducer()) //Agent cannot buy Product from itself
            return false;
        float totalPrice = getTotalPrice(buyer, product);
        return buyer.hasEnoughMoney(totalPrice);
    }

    /**
     * @param buyer Agent who wants to buy a product on the market
     * @param type Type of product the agent wants to buy
     * @return List of filtered product according to Type, Stocks, Price, ...
     */
    public List<Product> getFilteredProducts(Agent buyer, int type){
        var res = new ArrayList<Product>();
        var typeProducts = products.get(type);
        for (Product product: typeProducts){
            if (isProductBuyable(buyer, product))
                res.add(product);
        }
        res.sort(Comparator.comparing(Product::getSellingPrice)); // From lowest to highest price
        return res;
    }

    /**
     * Increment number of sales after a sell
     */
    public void incrementNbSales(){
        nbSales++;
    }

    /**
     * Increment number of purchases after a purchase
     */
    public void incrementNbPurchases(){
        nbPurchases++;
    }
}
