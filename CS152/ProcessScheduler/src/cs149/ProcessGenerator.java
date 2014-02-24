package cs149;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates enough processes in the given quanta, where the CPU is never
 * idle for more than 2 consecutive quanta.
 * @author Home
 *
 */
public class ProcessGenerator {
    private int totalQuanta;
    private List<Process> processes;
    private char name;
    private Scheduler schedule;
    
    public ProcessGenerator(int totalQuanta, long seed) {
        this.totalQuanta = totalQuanta;
        name = 'a';
        generateProcesses(seed);
    }
    
    public ProcessGenerator(int totalQuanta) {
        this.totalQuanta = totalQuanta;
        name = 'a';
        generateProcesses();
    }
    
    public void generateProcesses(long seed) {
        schedule = new Scheduler(totalQuanta);
        name = 'a';
        Process newProcess;
        
        while (schedule.shouldCreateProcess()) {
            if (name == '{') {
                name = 'A';
            }
            newProcess = new Process(name, seed);
            processes = schedule.addProcess(newProcess);
            name++;
        }
    }
    
    public void generateProcesses() {
        schedule = new Scheduler(totalQuanta);
        name = 'a';
        Process newProcess;
        
        while (schedule.shouldCreateProcess()) {
            if (name == '{') {
                name = 'A';
            }
            newProcess = new Process(name);
            processes = schedule.addProcess(newProcess);
            name++;
        }
    }
    
    public void listProcesses() {
        int size = processes.size();
        System.out.printf("%d processes:\n", size);
        for (int i = 0; i < size; i++) {
            Process current = processes.get(i);
            System.out.printf("%c: arrival=%.1f,runtime=%.1f,priority=%d\n", 
                    current.getName(), current.getArrivalTime(), 
                    current.getRunTime(), current.getPriority());
        }
    }
    
    private class Scheduler {
        private boolean create;
        private List<Process> processes;
        
        public Scheduler(int totalQuanta) {
            create = true;
            processes = new ArrayList<Process>();
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
                endTime = arrivalTime + runTime;
                startingBlock = (int) Math.floor(arrivalTime);
                
                if (i == 0) {
                    /* Ex: arrivalTime = 4.5, takes up whole block from 4-5 
                     * startingBlock is also the number of blocks before arrivalTime
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
                        endOfLastBlock += (int) Math.ceil(runTime);
                    }
                }
                /* If a process runs past the totalQuanta mark, stop checking */
                if (endOfLastBlock > totalQuanta) {
                    break;
                }
            }
            
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
