package rgomesro;

public final class Constants {
    private Constants() { }

    /**
     * World's constants
     */
    public static final class World {
        private World() {}

        public static final int NB_STATES = 200;
        public static final int NB_AGENTS = 5000;
        public static final int NB_TICKS = 5000;
        public static final int NB_TICKS_SAVE_CSV = 500;

        public static final String CSV_STATES = "res/states.csv";
        public static final String CSV_AGENTS = "res/agents.csv";
        public static final String CSV_PRODUCTS = "res/products.csv";
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

            public static final float VAT_MIN = 0f;
            public static final float VAT_MAX = 100f;

            public static final float WEALTH_TAX_MIN_THRESHOLD = 0f;
            public static final float WEALTH_TAX_MAX_THRESHOLD = Agent.INIT_MONEY;

            public static final float WEALTH_TAX_MIN_VALUE = 0f;
            public static final float WEALTH_TAX_MAX_VALUE = 100f;
        }

        /**
         * Allowances' constants
         */
        public static final class Allowance {
            private Allowance() {}

            public static final float UBI_MIN = 0f;
            public static final float UBI_MAX = Agent.INIT_MONEY/10;
        }
    }

    /**
     * Agent's constants
     */
    public static final class Agent {
        private Agent() {}

        public static final float INIT_MONEY = 1000f;

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
        public static final float MAX_PRICE = 100;
        public static final float MAX_STOCK = 50;
    }
}