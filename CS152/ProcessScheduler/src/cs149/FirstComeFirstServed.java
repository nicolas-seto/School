package cs149;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstComeFirstServed implements Algorithm {
    
    private List<Process> processes;
    private ProcessGenerator generator;
    private int runTimeSum;
    private StringBuilder timechart;
    private static final int TOTAL_QUANTA = 100;
    
    public FirstComeFirstServed() {
        generator = new ProcessGenerator(TOTAL_QUANTA);
        processes = generator.getProcesses();
        runTimeSum = 0;
        timechart = new StringBuilder();
    }
    
    /**
     * Runs FCFS algorithm.
     */
    public Map<String, Float> run() {
        /* TOTAL_QUANTA is the total running time, in this case 100 units */
        while (runTimeSum < TOTAL_QUANTA) {
            
        }
        return new HashMap<String, Float>();
    }
}
