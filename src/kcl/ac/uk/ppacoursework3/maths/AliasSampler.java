package src.kcl.ac.uk.ppacoursework3.maths;

import src.kcl.ac.uk.ppacoursework3.lifeForms.LifeForms;
import src.kcl.ac.uk.ppacoursework3.simulation.Randomizer;

import java.util.Random;

public class AliasSampler {

    private final int[] alias;
    private final double[] probability;
    private final Random rand;

    public AliasSampler(double[] probabilities) {
        //Build the alias tables for sampling:
        int n = probabilities.length;
        alias = new int[n];
        probability = new double[n];
        double[] weightedProbs = new double[n];
        int[] small = new int[n];
        int[] large = new int[n];
        int smallCount = 0;
        int largeCount = 0;

        //Scale probabilities:
        for (int i = 0; i < n; i++) {
            weightedProbs[i] = probabilities[i] * n;
            if (weightedProbs[i] < 1) small[smallCount++] = i;
            else large[largeCount++] = i;
        }

        //Balance probabilities:
        while (smallCount > 0 && largeCount > 0) {
            int lesser = small[--smallCount];
            int larger = large[--largeCount];
            probability[lesser] = weightedProbs[lesser];
            alias[lesser] = larger;
            weightedProbs[larger] += weightedProbs[lesser] - 1;

            if (weightedProbs[larger] < 1) small[smallCount++] = larger;
            else large[largeCount++] = larger;
        }

        //Fill in remaining probs if there are any:
        while (largeCount > 0) probability[large[--largeCount]] = 1;
        while (smallCount > 0) probability[small[--smallCount]] = 1;

        //Initialise random from Randomizer:
        rand = Randomizer.getRandom();
    }

    public LifeForms nextSample() {
        int column = rand.nextInt(probability.length);
        boolean coinToss = rand.nextDouble() < probability[column];
        int id = coinToss ? column : alias[column];
        return LifeForms.getByID(id);
    }


    public AliasSampler() {
        this(new double[]{0.25, 0.3, 0.45});
    }
}
