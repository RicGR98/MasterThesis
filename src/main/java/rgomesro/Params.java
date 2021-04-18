package rgomesro;

import org.json.simple.JSONObject;
import rgomesro.utils.JsonUtils;

public final class Params {
    private static final JSONObject json = JsonUtils.read("params/medium.json");

    private Params() { }

    /**
     * World's constants
     */
    public static final class World {
        private static final JSONObject jsonWorld = JsonUtils.getJsonObject(json, "World");

        private World() {}

        public static final int NB_STATES = JsonUtils.getInt(jsonWorld, "NB_STATES");
        public static final int NB_AGENTS = JsonUtils.getInt(jsonWorld, "NB_AGENTS");
        public static final int NB_TICKS = JsonUtils.getInt(jsonWorld, "NB_TICKS");
        public static final int NB_TICKS_SAVE_CSV = JsonUtils.getInt(jsonWorld, "NB_TICKS_SAVE_CSV");

        public static final String DIRECTORY_RES_CSV = "res/csv/";
        public static final String CSV_STATES = DIRECTORY_RES_CSV + "states.csv";
        public static final String CSV_AGENTS = DIRECTORY_RES_CSV + "agents.csv";
        public static final String CSV_PRODUCTS = DIRECTORY_RES_CSV + "products.csv";
    }

    /**
     * Cluster's constants
     */
    public static final class Cluster {
        private static final JSONObject jsonCluster = JsonUtils.getJsonObject(json, "Cluster");

        private Cluster() {}

        // TODO: Analyze
        public static final Float PROB_ATTACHED = JsonUtils.getFloat(jsonCluster, "PROB_ATTACHED");
    }

    /**
     * State's constants
     */
    public static final class State {
        private static final JSONObject jsonState = JsonUtils.getJsonObject(json, "State");

        private State() {}

        public static final int NB_TICKS_COLLECT_TAXES = World.NB_TICKS/100;
        public static final int NB_TICKS_DISTRIBUTE_UBI = NB_TICKS_COLLECT_TAXES;

        /**
         * Taxes' constants
         */
        public static final class Tax {
            private static final JSONObject jsonTax = JsonUtils.getJsonObject(jsonState, "Tax");

            private Tax() {}

            //TODO: Analyze
            public static final float MIN_VAT = JsonUtils.getFloat(jsonTax, "MIN_VAT");
            public static final float MAX_VAT = JsonUtils.getFloat(jsonTax, "MAX_VAT");
            public static final float VAL_VAT = JsonUtils.getFloat(jsonTax, "VAL_VAT");

            // TODO: Analyze
            public static final float VAL_WEALTH_TAX_TOP = JsonUtils.getFloat(jsonTax, "VAL_WEALTH_TAX_TOP"); //E.g.: Top 10% (top = 0.1) wealthiest Agents are taxed
            public static final float VAL_WEALTH_TAX_VALUE = JsonUtils.getFloat(jsonTax, "VAL_WEALTH_TAX_VALUE");  //E.g.: 10% (value = 0.1) Wealth tax
        }

        /**
         * Allowances' constants
         */
        public static final class Allowance {
            private static final JSONObject jsonAllowance = JsonUtils.getJsonObject(jsonState, "Allowance");

            private Allowance() {}

            // TODO: Analyze
            public static final float MIN_UBI = JsonUtils.getFloat(jsonAllowance, "MIN_UBI");
            public static final float MAX_UBI = JsonUtils.getFloat(jsonAllowance, "MAX_UBI");
            public static final float VAL_UBI = JsonUtils.getFloat(jsonAllowance, "VAL_UBI");
        }
    }

    /**
     * Agent's constants
     */
    public static final class Agent {
        private static final JSONObject jsonAgent = JsonUtils.getJsonObject(json, "Agent");

        private Agent() {}

        // TODO: Analyze
        public static final float MIN_INIT_MONEY = JsonUtils.getFloat(jsonAgent, "MIN_INIT_MONEY");
        public static final float MAX_INIT_MONEY = JsonUtils.getFloat(jsonAgent, "MAX_INIT_MONEY");
        public static final float VAL_INIT_MONEY = JsonUtils.getFloat(jsonAgent, "VAL_INIT_MONEY");

        // TODO: Analyze
        public static final int NB_PRODUCED_PRODUCTS = JsonUtils.getInt(jsonAgent, "NB_PRODUCED_PRODUCTS");
        public static final float RATIO_BUY = JsonUtils.getFloat(jsonAgent, "RATIO_BUY");
        public static final float RATIO_PRODUCE = JsonUtils.getFloat(jsonAgent, "RATIO_PRODUCE");
    }

    /**
     * Product's constants
     */
    public static final class Product {
        private static final JSONObject jsonProduct = JsonUtils.getJsonObject(json, "Product");

        private Product() {}

        public static final int NB_DIFF_PRODUCTS = JsonUtils.getInt(jsonProduct, "NB_DIFF_PRODUCTS");
        public static final float MIN_PRICE = JsonUtils.getFloat(jsonProduct, "MIN_PRICE");
        public static final float MAX_PRICE = JsonUtils.getFloat(jsonProduct, "MAX_PRICE");
        public static final int MAX_STOCK = JsonUtils.getInt(jsonProduct, "MAX_STOCK");
    }
}