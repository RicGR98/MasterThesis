package rgomesro;

import org.json.simple.JSONObject;
import rgomesro.utils.JsonUtils;

public final class Params {
    private JSONObject json;
    public World world;
    public Connections connections;
    public State state;
    public State.Tax tax;
    public State.Allowance allowance;
    public State.Others others;
    public Agent agent;
    public Product product;

    private Params() { }

    private static class ParamsHolder {
        private final static Params instance = new Params();
    }

    public static Params getInstance() {
        return ParamsHolder.instance;
    }

    public void load(String filename) {
        json = JsonUtils.read("params/" + filename);
        world = new World();
        connections = new Connections();
        state = new State();
        tax = state.new Tax();
        allowance = state.new Allowance();
        others = state.new Others();
        agent = new Agent();
        product = new Product();
    }

    /**
     * World's constants
     */
    public final class World {
        public final int NB_STATES;
        public final int NB_AGENTS;
        public final int NB_TICKS;
        public final int NB_TICKS_SAVE_CSV;

        public final String DIRECTORY_RES_CSV = "res/csv/";
        public final String CSV_TICKS = DIRECTORY_RES_CSV + "ticks";
        public final String CSV_STATES = DIRECTORY_RES_CSV + "states";
        public final String CSV_AGENTS = DIRECTORY_RES_CSV + "agents";
        public final String CSV_PRODUCTS = DIRECTORY_RES_CSV + "products";

        private World() {
            JSONObject jsonWorld = JsonUtils.getJsonObject(json, "World");
            NB_STATES = JsonUtils.getInt(jsonWorld, "NB_STATES");
            NB_AGENTS = JsonUtils.getInt(jsonWorld, "NB_AGENTS");
            NB_TICKS = JsonUtils.getInt(jsonWorld, "NB_TICKS");
            NB_TICKS_SAVE_CSV = JsonUtils.getInt(jsonWorld, "NB_TICKS_SAVE_CSV");
        }
    }

    /**
     * Connections' constants
     */
    public final class Connections {
        public final int CLUSTER_SIZE;
        public final float PROB_CONNECTION;

        private Connections() {
            JSONObject jsonConnections = JsonUtils.getJsonObject(json, "Connections");
            CLUSTER_SIZE = JsonUtils.getInt(jsonConnections, "CLUSTER_SIZE");
            PROB_CONNECTION = JsonUtils.getFloat(jsonConnections, "PROB_CONNECTION");
        }
    }

        /**
     * State's constants
     */
    public final class State {
        public final JSONObject jsonState = JsonUtils.getJsonObject(json, "State");
        public int NB_TICKS_COLLECT_TAXES;
        public int NB_TICKS_DISTRIBUTE_ALLOWANCES;
        public float MIN_UNEMPLOYMENT;
        public float MAX_UNEMPLOYMENT;
        public float MIN_BLACK;
        public float MAX_BLACK;

        private State() {
        }

        /**
         * Taxes' constants
         */
        public final class Tax {
            public final float MIN_VAT;
            public final float MAX_VAT;
            public final float MIN_LEVY;
            public final float MAX_LEVY;
            public final float MIN_TARIFF;
            public final float MAX_TARIFF;
            public final float VAL_WEALTH_TAX_TOP;
            public final float MIN_WEALTH_TAX_VALUE;
            public final float MAX_WEALTH_TAX_VALUE;

            private Tax() {
                JSONObject jsonTax = JsonUtils.getJsonObject(jsonState, "Tax");

                MIN_VAT = JsonUtils.getFloat(jsonTax, "MIN_VAT");
                MAX_VAT = JsonUtils.getFloat(jsonTax, "MAX_VAT");

                MIN_LEVY = JsonUtils.getFloat(jsonTax, "MIN_LEVY");
                MAX_LEVY = JsonUtils.getFloat(jsonTax, "MAX_LEVY");

                MIN_TARIFF = JsonUtils.getFloat(jsonTax, "MIN_TARIFF");
                MAX_TARIFF = JsonUtils.getFloat(jsonTax, "MAX_TARIFF");

                VAL_WEALTH_TAX_TOP = JsonUtils.getFloat(jsonTax, "VAL_WEALTH_TAX_TOP"); //E.g.: Top 10% (top = 0.1) wealthiest Agents are taxed
                MIN_WEALTH_TAX_VALUE = JsonUtils.getFloat(jsonTax, "MIN_WEALTH_TAX_VALUE");  //E.g.: 10% (value = 0.1) Wealth tax
                MAX_WEALTH_TAX_VALUE = JsonUtils.getFloat(jsonTax, "MAX_WEALTH_TAX_VALUE");  //E.g.: 10% (value = 0.1) Wealth tax

                NB_TICKS_COLLECT_TAXES = JsonUtils.getInt(jsonTax, "NB_TICKS_COLLECT_TAXES");
            }
}

        /**
         * Allowances' constants
         */
        public final class Allowance {

            private Allowance() {
                JSONObject jsonAllowance = JsonUtils.getJsonObject(jsonState, "Allowance");

                NB_TICKS_DISTRIBUTE_ALLOWANCES = JsonUtils.getInt(jsonAllowance, "NB_TICKS_DISTRIBUTE_ALLOWANCES");
            }
        }

        /**
         * Other constants
         */
        public final class Others {

            private Others() {
                JSONObject jsonOthers = JsonUtils.getJsonObject(jsonState, "Others");

                MIN_UNEMPLOYMENT = JsonUtils.getFloat(jsonOthers, "MIN_UNEMPLOYMENT");
                MAX_UNEMPLOYMENT = JsonUtils.getFloat(jsonOthers, "MAX_UNEMPLOYMENT");

                MIN_BLACK = JsonUtils.getFloat(jsonOthers, "MIN_BLACK");
                MAX_BLACK = JsonUtils.getFloat(jsonOthers, "MAX_BLACK");
            }
        }
    }

    /**
     * Agent's constants
     */
    public final class Agent {
        public final float MIN_INIT_MONEY;
        public final float MAX_INIT_MONEY;
        public final int NB_PRODUCED_PRODUCTS;
        public final float RATIO_BUY;
        public final float RATIO_PRODUCE;

        private Agent() {
            JSONObject jsonAgent = JsonUtils.getJsonObject(json, "Agent");

            MIN_INIT_MONEY = JsonUtils.getFloat(jsonAgent, "MIN_INIT_MONEY");
            MAX_INIT_MONEY = JsonUtils.getFloat(jsonAgent, "MAX_INIT_MONEY");

            NB_PRODUCED_PRODUCTS = JsonUtils.getInt(jsonAgent, "NB_PRODUCED_PRODUCTS");
            RATIO_BUY = JsonUtils.getFloat(jsonAgent, "RATIO_BUY");
            RATIO_PRODUCE = JsonUtils.getFloat(jsonAgent, "RATIO_PRODUCE");
        }

    }

    /**
     * Product's constants
     */
    public final class Product {
        public final int NB_DIFF_PRODUCTS;
        public final float MIN_PRICE;
        public final float MAX_PRICE;
        public final int MAX_STOCK;

        private Product() {
            JSONObject jsonProduct = JsonUtils.getJsonObject(json, "Product");
            NB_DIFF_PRODUCTS = JsonUtils.getInt(jsonProduct, "NB_DIFF_PRODUCTS");
            MIN_PRICE = JsonUtils.getFloat(jsonProduct, "MIN_PRICE");
            MAX_PRICE = JsonUtils.getFloat(jsonProduct, "MAX_PRICE");
            MAX_STOCK = JsonUtils.getInt(jsonProduct, "MAX_STOCK");
        }
    }
}