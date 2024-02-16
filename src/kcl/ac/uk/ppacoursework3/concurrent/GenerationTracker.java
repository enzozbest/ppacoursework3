package src.kcl.ac.uk.ppacoursework3.concurrent;

import src.kcl.ac.uk.ppacoursework3.GUI.SimulatorView;
import src.kcl.ac.uk.ppacoursework3.simulation.Simulator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class keeps track of how many generations it has been since a given event.
 * For example, in this project it keeps track of how many generations ago a Lycoperdon has burst (changed colours).
 * <p></p>
 * Multiple concurrent Threads are used for this functionality. They are handled in a cached Thread pool, which creates
 * as many Threads as necessary but is also able to reuse previously created ones that become available.
 * This choice comes from the fact that cached Thread pools optimise performance of very small operations, as is
 * the case with this project.
 * <p></p>
 * To use this tracker, create a new GenerationTracker object by passing the "current" generation at time of creation.
 * Then use the run() method to begin execution and retrieve the Future object of the task.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz(K23000902)
 * @version 2024.02.15
 */
public class GenerationTracker {
    public static final ExecutorService executor = Executors.newCachedThreadPool();
    private final int initialGeneration;
    private final Simulator simulator;
    private int currentGen;
    private final int futureGen;

    /**
     * Create a GenerationTracker object by passing in the current generation at the time of creation and
     * the number of generations to keep track for.
     *
     * @param initialGeneration the current generation at the time of creation.
     * @param futureGenerations the number of generations you wish to track.
     */
    public GenerationTracker(int initialGeneration, int futureGenerations) {
        this.futureGen = futureGenerations;
        this.simulator = SimulatorView.simulator;
        this.initialGeneration = initialGeneration;
        this.currentGen = initialGeneration;
    }

    /**
     * Submit the counter task for execution in the Thread pool.
     *
     * @return the Future object of the task submitted.
     */
    public Future<?> run() {
        return executor.submit(() -> {
            try {
                while (currentGen != initialGeneration + futureGen) {
                    Thread.sleep(500);
                    currentGen = simulator.getGeneration();
                }
            } catch (InterruptedException e) {
                System.out.println("Thread has ended unexpectedly");
            }
        });
    }
}
