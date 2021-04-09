package rgomesro;

public final class Constants {

    private Constants() { }

    public static final class State {
        private State() {}

        public static final float MIN_VAT = 0f;
        public static final float MAX_VAT = 100f;
    }

    public static final class Agent {
        private Agent() {}

        public static final int NB_PRODUCTS = 3;
        public static final float RATIO_BUY = 0.05f;
        public static final float RATIO_PRODUCE = 0.05f;
    }

    public static final class Product {
        private Product() {}

        public static final int NB_UNIQUE = 10;
        public static final float MIN_PRICE = 0;
        public static final float MAX_PRICE = 100;
    }
}