package rgomesro;

public final class Constants {
    private Constants() { }

    /**
     * World's constants
     */
    public static final class World {
        private World() {}

        public static final int NB_STATES = 100;
        public static final int NB_AGENTS = 5000;
        public static final int NB_TICKS = 10000;
        public static final int NB_TICKS_SAVE_CSV = 500;

        public static final String DIRECTORY_RES_CSV = "res/csv/";
        public static final String CSV_STATES = DIRECTORY_RES_CSV + "states.csv";
        public static final String CSV_AGENTS = DIRECTORY_RES_CSV + "agents.csv";
        public static final String CSV_PRODUCTS = DIRECTORY_RES_CSV + "products.csv";
    }

    /**
     * Cluster's constants
     */
    public static final class Cluster {
        private Cluster() {}

        // TODO: Analyze
        public static final Float PROB_ATTACHED = 0f;
    }

    /**
     * State's constants
     */
    public static final class State {
        private State() {}

        public static final int NB_TICKS_COLLECT_TAXES = World.NB_TICKS/100;
        public static final int NB_TICKS_DISTRIBUTE_UBI = NB_TICKS_COLLECT_TAXES;

        /**
         * Taxes' constants
         */
        public static final class Tax {
            private Tax() {}

            //TODO: Analyze
            public static final float MIN_VAT = 0f;
            public static final float MAX_VAT = 100f;
            public static final float VAL_VAT = 21f;

            // TODO: Analyze
            public static final float VAL_WEALTH_TAX_TOP = 0.1f; //E.g.: Top 10% (top = 0.1) wealthiest Agents are taxed
            public static final float VAL_WEALTH_TAX_VALUE = 0.1f;  //E.g.: 10% (value = 0.1) Wealth tax
        }

        /**
         * Allowances' constants
         */
        public static final class Allowance {
            private Allowance() {}

            // TODO: Analyze
            public static final float MIN_UBI = 0f;
            public static final float MAX_UBI = Agent.VAL_INIT_MONEY /10;
            public static final float VAL_UBI = 0f;
        }
    }

    /**
     * Agent's constants
     */
    public static final class Agent {
        private Agent() {}

        // TODO: Analyze
        public static final float MIN_INIT_MONEY = 0f;
        public static final float MAX_INIT_MONEY = 1000f;
        public static final float VAL_INIT_MONEY = 1000f;

        // TODO: Analyze
        public static final int NB_PRODUCED_PRODUCTS = 3;
        public static final float RATIO_BUY = 0.2f;
        public static final float RATIO_PRODUCE = 0.2f;
    }

    /**
     * Product's constants
     */
    public static final class Product {
        private Product() {}

        public static final int NB_DIFF_PRODUCTS = 50;
        public static final float MIN_PRICE = 0;
        public static final float MAX_PRICE = Agent.MAX_INIT_MONEY/10;
        public static final float MAX_STOCK = 50;
    }
}