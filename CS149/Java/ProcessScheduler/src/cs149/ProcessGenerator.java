package cs149;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates enough processes in the given quanta when instantiated, 
 * where the CPU is never idle for more than 2 consecutive quanta.
 * @author Nic
 *
 */
public class ProcessGenerator {
    private int totalQuanta;
    private List<Process> processes;
    private char prefix;
    private Scheduler schedule;
    
    public ProcessGenerator(int totalQuanta, long seed) {
        this.totalQuanta = totalQuanta;
        generateProcesses(seed);
    }
    
    public ProcessGenerator(int totalQuanta) {
        this.totalQuanta = totalQuanta;
        generateProcesses();
    }
    
    public void generateProcesses(long seed) {
        schedule = new Scheduler(totalQuanta);
        prefix = 'a';
        int iteration = 0;
        Process newProcess;
        
        while (schedule.shouldCreateProcess()) {
            if (prefix == '{') {
                prefix = 'A';
            } else if (prefix == '[') {
                ++iteration;
                prefix = 'a';
            }
            newProcess = new Process(prefix + "" + iteration, seed);
            processes = schedule.addProcess(newProcess);
            prefix++;
        }
    }
    
    /**
     * Generate a process via insertion sort. Check to see
     * if there are gaps larger than 2 units based on arrival times of the
     * processes. If there is, continue generating processes. If not, stop.
     */
    public void generateProcesses() {
        schedule = new Scheduler(totalQuanta);
        prefix = 'a';
        int iteration = 0;
        Process newProcess;
        
        while (schedule.shouldCreateProcess()) {
            if (prefix == '{') {
                prefix = 'A';
            } else if (prefix == '[') {
                ++iteration;
                prefix = 'a';
            }
            newProcess = new Process(prefix + "" + iteration);
            processes = schedule.addProcess(newProcess);
            prefix++;
        }
    }
    
    /**
     * List the processes.
     */
    public void listProcesses() {
        int size = processes.size();
        System.out.printf("%d processes:\n", size);
        for (int i = 0; i < size; i++) {
            Process current = processes.get(i);
            System.out.printf("%s: arrival=%.1f,runtime=%.1f,priority=%d\n", 
                    current.getName(), current.getArrivalTime(), 
                    current.getRunTime(), current.getPriority());
        }
    }
    
    /**
     * Return the processes.
     * @return the processes
     */
    public List<Process> getProcesses() {
        return processes;
    }
    
    private class Scheduler {
        private boolean create;
        private List<Process> processes;
        private int TOTAL_QUANTA;
        
        public Scheduler(int totalQuanta) {
            create = true;
            processes = new ArrayList<Process>();
            TOTAL_QUANTA = totalQuanta;
        }
        
        public boolean shouldCreateProcess() {
            return create;
        }
        
        /**
         * Insertion sort based on arrival time. 
         * @param theProcess the process to be added
         * @return the list of processes
         */
        public List<Process> addProcess(Process theProcess) {
            int size = processes.size(), position = 0;
            float processArrivalTime = theProcess.getArrivalTime();
            
            for (int i = 0; i < size; i++) {
                position = i;
                /* If arrival time of process to be added is less than 
                 * process at index i, shift everything to the right
                 */
                if (processArrivalTime < processes.get(i).getArrivalTime()) {
                    break;
                  /* If the end of the list is reached, append process to end of list */
                } else if (i == size - 1){
                    position = i + 1;
                }
            }
            /* First process added to position 0 */
            processes.add(position, theProcess);
            create = checkWait();
            return processes;
        }
        
        private boolean checkWait() {
            ArrayList<Integer> waitBlocks = new ArrayList<Integer>();
            int size = processes.size(), size2, startingBlock = 0, endOfLastBlock = 0;
            float arrivalTime, runTime, endTime;
            
            /* endOfLastBlock is a marker for the end position of the last process 
             * with respect to overlapping and non-overlapping arrival times
             */
            for (int i = 0; i < size; i++) {
                arrivalTime = processes.get(i).getArrivalTime();
                runTime = processes.get(i).getRunTime();
                startingBlock = (int) Math.ceil(arrivalTime);
                endTime = (float) startingBlock + runTime;
                
                if (i == 0) {
                    /* Ex: arrivalTime = 4.5, takes up whole block from 4-5 
                     * startingBlock is also the number of blocks before the process runs
                     */
                    waitBlocks.add(startingBlock);
                    endOfLastBlock = (int) Math.ceil(endTime);
                } else {
                    /* There's a wait block between the last process and this
                     * process
                     */
                    if (startingBlock >= endOfLastBlock) {
                        waitBlocks.add(startingBlock - endOfLastBlock);
                        endOfLastBlock = (int) Math.ceil(endTime);
                    } else {
                        endOfLastBlock += processes.get(i).getRunBlockSize();
                    }
                }
                /* If a process runs past the totalQuanta mark, stop checking */
                if (endOfLastBlock > totalQuanta) {
                    break;
                }
            }
            
            waitBlocks.add(TOTAL_QUANTA - endOfLastBlock);
            size2 = waitBlocks.size();
            
            for (int j = 0; j < size2; j++) {
                if (waitBlocks.get(j) > 2) {
                    return true;
                }
            }
            /* All the wait times are either 0 or 1 */
            return false;
        }
    }
}
