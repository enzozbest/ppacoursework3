package src.kcl.ac.uk.ppacoursework3.lifeForms;

import java.util.Random;

public enum LifeForms {

    MYCOPLASMA(0.25), FUNGUS(0.10), DEFAULT(0);

    public double SPAWN_PROB;

    LifeForms(double SPAWN_PROB) {
        this.SPAWN_PROB = SPAWN_PROB;
    }

    private static class RandomEnum<T extends Enum<T>> {
        private static Random rand = new Random();

        private final T[] values;

        public RandomEnum(Class<T> token){
            this.values = token.getEnumConstants();
        }
        public T random(){
            return values[rand.nextInt(values.length)];
        }
    }
}
