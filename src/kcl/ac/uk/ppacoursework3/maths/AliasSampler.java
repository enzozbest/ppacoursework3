package src.kcl.ac.uk.ppacoursework3.maths;

import src.kcl.ac.uk.ppacoursework3.utils.Randomizer;

import java.util.Random;

/**
 * This class represents a sampler from a biased distribution.
 * The Alias method is used to choose a number from a list each with a given bias.
 * These numbers, in this project, are used as indexes into an Enum (LifeForms), which contains the enumeration
 * of all the Life forms implemented.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.01
 */
public class AliasSampler {

    private final int[] alias;
    private final double[] probability;
    private final Random rand;

    /**
     * Build an AliasSampler object by:
     * 1. Creating an alias table
     * 2. Categorising elements as "underfull", "overfull", and "exactly full".
     * 3. Balancing the probabilities until everything is "exactly full"
     * 4. Filling in remaining probabilities if there are any left.
     *
     * @param probabilities the desired array of biases
     */
    public AliasSampler(double[] probabilities) {
        rand = Randomizer.getRandom();
        int n = probabilities.length;
        alias = new int[n];
        probability = new double[n];
        double[] weightedProbs = new double[n];
        int[] underfull = new int[n];
        int[] overfull = new int[n];
        int smallCount = 0;
        int largeCount = 0;

        for (int i = 0; i < n; i++) {
            weightedProbs[i] = probabilities[i] * n; //Build alias table
            if (weightedProbs[i] < 1) underfull[smallCount++] = i; //Categorise each alias as "underfull" or "overfull"
            else overfull[largeCount++] = i;
        }

        //Balance probabilities until everything is "exactly full":
        while (smallCount > 0 && largeCount > 0) {
            int lesser = underfull[--smallCount];
            int larger = overfull[--largeCount];
            probability[lesser] = weightedProbs[lesser];
            alias[lesser] = larger;
            weightedProbs[larger] += weightedProbs[lesser] - 1;

            if (weightedProbs[larger] < 1) underfull[smallCount++] = larger;
            else overfull[largeCount++] = larger;
        }

        //Fill in remaining probs if there are any:
        while (largeCount > 0) probability[overfull[--largeCount]] = 1;
        while (smallCount > 0) probability[underfull[--smallCount]] = 1;
    }

    /**
     * Generates an AliasSampler object with default biases.
     */
    public AliasSampler() {
        this(new double[]{0.4, 0.2, 0.3, 0.005, 0.1, 0.895});
    }

    /**
     * Once the object is created, this method can be called to choose one number based on the specified biases.
     * This will return an integer value that can then be further manipulated to get, for example, a LifeForms constant.
     *
     * @return the chosen integer
     */
    public int sample() {
        if (probability.length == 0) return -1; //@enzozbest: May be problematic? Not sure...
        int column = rand.nextInt(probability.length);
        boolean coinToss = rand.nextDouble() < probability[column];

        if (coinToss) return column;

        return alias[column];
    }
}
