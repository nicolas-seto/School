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
    private float totalQuanta, runTimeSum;
    private List<Process> processes;
    private char name;
    private Scheduler schedule;
    
    public ProcessGenerator(int totalQuanta, long seed) {
        this.totalQuanta = new Integer(totalQuanta).floatValue();
        name = 'a';
        schedule = new Scheduler(totalQuanta);
        generateProcesses(seed);
    }
    
    public ProcessGenerator(int totalQuanta) {
        this.totalQuanta = new Integer(totalQuanta).floatValue();
        name = 'a';
        schedule = new Scheduler(totalQuanta);
        generateProcesses();
    }
    
    public void generateProcesses(long seed) {
        runTimeSum = 0.0f;
        name = 'a';
        Process newProcess;
        //processes = new ArrayList<Process>();
        
        while (schedule.getLongestWait() > 2) {
            if (name == 'z') {
                name = 'A';
            }
            newProcess = new Process(name, seed);
            
            name++;
        }
    }
    
    public void generateProcesses() {
        runTimeSum = 0.0f;
        name = 'a';
        Process newProcess;
        //processes = new ArrayList<Process>();
        
        while (schedule.shouldCreateProcess()) {
            if (name == 'z') {
                name = 'A';
            }
            newProcess = new Process(name);
            
            name++;
        }
    }
    
    private class Scheduler {
        //private int maxWaitPeriod;
        private boolean create;
        private List<Process> processes;
        
        public Scheduler(int totalQuanta) {
            //maxWaitPeriod = 3;
            create = true;
            processes = new ArrayList<Process>();
        }
        
        /*public int getLongestWait() {
            return maxWaitPeriod;
        }*/
        
        public boolean shouldCreateProcess() {
            return create;
        }
        
        public void addProcess(Process theProcess) {
            int size = processes.size(), position = 0;
            
            for (int i = 0; i < size; i++) {
                position = i;
                if (theProcess.getArrivalTime() < processes.get(i).getArrivalTime()) {
                    break;
                }
            }
            processes.add(position, theProcess);
            create = checkWait();
        }
        
        private boolean checkWait() {
            ArrayList<Integer> waitBlocks = new ArrayList<Integer>();
            int size = processes.size(), size2 = waitBlocks.size(), startingBlock = 0, endOfLastBlock = 0;
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
