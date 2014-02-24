package cs149;

import java.util.ArrayList;
import java.util.List;

public class FirstComeFirstServed implements Algorithm {
    
    private List<Process> processes;
    private ProcessGenerator generator;
    
    public FirstComeFirstServed() {
        generator = new ProcessGenerator(100);
        processes = generator.getProcesses();
    }
    
    /**
     * Runs FCFS algorithm.
     */
    public void run() {
        
        
    }
}
