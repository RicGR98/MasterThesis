package rgomesro;

public final class Constants {
    private Constants() { }

    /**
     * World's constants
     */
    public static final class World {
        private World() {}

        public static final int NB_TICKS = 20001;
        public static final int NB_STATES = 30;
        public static final int NB_AGENTS = 5000;

        public static final String CSV_STATES = "res/states.csv";
        public static final String CSV_AGENTS = "res/agents.csv";
    }

    /**
     * State's constants
     */
    public static final class State {
        private State() {}

        public static final float MIN_VAT = 0f;
        public static final float MAX_VAT = 100f;
    }

    /**
     * Agent's constants
     */
    public static final class Agent {
        private Agent() {}

        public static final int NB_PRODUCED_PRODUCTS = 3;
        public static final float RATIO_BUY = 0.05f;
        public static final float RATIO_PRODUCE = 0.05f;
    }

    /**
     * Product's constants
     */
    public static final class Product {
        private Product() {}

        public static final int NB_DIFF_PRODUCTS = 10;
        public static final float MIN_PRICE = 0;
        public static final float MAX_PRICE = 100;
    }
}