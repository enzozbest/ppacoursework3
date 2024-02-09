package src.kcl.ac.uk.ppacoursework3.maths;

import src.kcl.ac.uk.ppacoursework3.lifeForms.LifeForms;

@FunctionalInterface
public interface Sampler {
    LifeForms sample(int[] ratio);
}
