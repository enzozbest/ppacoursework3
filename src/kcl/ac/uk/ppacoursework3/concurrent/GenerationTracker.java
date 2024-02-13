package src.kcl.ac.uk.ppacoursework3.concurrent;

import src.kcl.ac.uk.ppacoursework3.GUI.SimulatorView;
import src.kcl.ac.uk.ppacoursework3.simulation.Simulator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class keeps track of how many generations it has been since an event.
 * For example, in this project it keeps track of how many generations ago a Lycoperdon has burst.
 * <p></p>
 * Multiple concurrent Threads are used for this functionality. They are handled in a cached Thread pool, which creates
 * as many Threads as necessary but is also able to reuse previously created ones that become available.
 * This choice comes from the fact that cached Thread pools optimise performance of very small operations, as is
 * the case with this project.
 * <p></p>
 * The Thread Pool is itself run on a separate Thread because the Main Thread must manage all of these threads and
 * the simulation Thread. By running this class separately we avoid situations in which the simulation gets stuck.
 * <p></p>
 * To use this tracker, create a new GenerationTracker object by passing the "current" generation at time of creation.
 * Then use the start() method to begin execution.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz(K23000902)
 * @version 2024.02.13
 */
public class GenerationTracker extends Thread {
    public static final ExecutorService executor = Executors.newCachedThreadPool();
    private final int initialGeneration;
    private final Simulator simulator;
    private int currentGen;
    private final int futureGen;
    private Future<?> state;

    /**
     * Create a GenerationTracker object by passing in the current generation at the time of creation.
     *
     * @param initialGeneration the current generation at the time of creation.
     * @param futureGenerations the number of generations you wish to track.
     */
    public GenerationTracker(int initialGeneration, int futureGenerations) {
        this.futureGen = futureGenerations;
        this.simulator = SimulatorView.simulator;
        this.initialGeneration = initialGeneration;
    }

    /**
     * Submit a task for execution in the Thread pool.
     * The task will check every 0.5s if the correct number of generations has passed.
     * Once this is the case, the method ends and so does the Thread.
     */
    @Override
    public void run() {
        state = executor.submit(() -> {
            try {
                currentGen = simulator.getGeneration();
                while (currentGen != initialGeneration + futureGen) {
                    Thread.sleep(500);
                    currentGen = simulator.getGeneration();
                }

            } catch (InterruptedException e) {
                System.out.println("Thread has ended unexpectedly");
            }
        });
    }

    /**
     * @return the Future object of a Thread run in the thread pool.
     */
    public Future<?> getFuture() {
        return state;
    }
}
