package cs149;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighestPriorityFirstNP implements Algorithm {
    
    private List<Process> processes;
    private ProcessGenerator generator;
    private int runTimeSum, count;
    private StringBuilder timechart, timestamp;
    private Map<String, Float> runOutput;
    private static final int TOTAL_QUANTA = 100;
    
    public HighestPriorityFirstNP() {
        generator = new ProcessGenerator(TOTAL_QUANTA);
        processes = generator.getProcesses();
        runTimeSum = 0;
        count = 0;
        timechart = new StringBuilder();
        timestamp = new StringBuilder();
        runOutput = run();
    }
    
    /**
     * Runs SJF algorithm.
     */
    private Map<String, Float> run() {
        int counter = 0, idleBlocks = 0, processesSize = processes.size();
        float turnaroundTime = 0, waitingTime = 0, responseTime = 0;
        List<Process> processesRan = new ArrayList<Process>();
        Map<String, Float> outputs = new HashMap<String, Float>();
        
        /* TOTAL_QUANTA is the total running time, in this case 100 units */
        while (runTimeSum <= TOTAL_QUANTA && counter < processesSize) {
            Process aProcess = processes.get(counter);
            String name = aProcess.getName();
            int processBlockSize = aProcess.getRunBlockSize();
            float arrivalTime = aProcess.getArrivalTime();
            float runTime = aProcess.getRunTime();
            float currentWait = 0.0f;
            String timestampSnippet = "";
            /* This endTime is the assumption that the process starts at the end
             * of a quantum if it arrives in the middle.
              */
            float endTime = (float) Math.ceil(arrivalTime) + runTime;
            
            if (counter == 0) {
                /* First process is run. If the arrival is a decimal, there is
                 * a wait for the next quantum to start; otherwise, there is 
                 * no wait. 
                 */
                currentWait = (float) Math.ceil(arrivalTime) - arrivalTime;
                idleBlocks = (int) Math.ceil(arrivalTime);
                runTimeSum = (int) Math.ceil(endTime);
            } else {
                /* There's an idle period > 0 between the last process and this
                 * process. The process may have to wait for next quantum to
                 * start.
                 */
                if (arrivalTime > (float) runTimeSum) {
                    currentWait = (float) Math.ceil(arrivalTime) - arrivalTime;
                    idleBlocks = (int) Math.ceil(arrivalTime) - runTimeSum;
                    runTimeSum = (int) Math.ceil(endTime);
                } else {
                /* This process starts right after the end of the quantum of
                 * the last process
                 */
                    runTimeSum += processBlockSize;
                    currentWait = (float) runTimeSum - arrivalTime;
                    idleBlocks = 0;
                }
            }
            turnaroundTime += currentWait + runTime;
            waitingTime += currentWait;
            responseTime += currentWait;
            
            /* Build timechart */
            for (int i = 0; i < idleBlocks; i++) {
                timechart.append("[  ]");
                
                if (count % 10 == 0) {
                    timestampSnippet = "0   ";
                } else {
                    timestampSnippet = "    ";
                }
                timestamp.append(timestampSnippet);
                count++;
            }
            for (int i = 0; i < processBlockSize; i++) {
                timechart.append("[" + name + "]");
                
                if (count % 10 == 0) {
                    timestampSnippet = "0   ";
                } else {
                    timestampSnippet = "    ";
                }
                timestamp.append(timestampSnippet);
                count++;
            }
            /* Update order of processes after timechart is updated. Don't run
             * if the last process just ran.
             */
            if (counter != processesSize - 1) {
                reorderReadyProcesses(counter, runTimeSum);
            }
            
            counter++;
            if (runTimeSum > TOTAL_QUANTA || counter == processesSize) {
                processesRan = processes.subList(0, counter);
                break;
            }
        }
        
        float runSize = (float) processesRan.size();
        outputs.put("avgTurnaround", turnaroundTime / runSize);
        outputs.put("avgWaiting", waitingTime / runSize);
        outputs.put("avgResponse", responseTime / runSize);
        outputs.put("throughput", runSize);
        
        System.out.printf("Timechart:\n%s\n", listTimechart());
        System.out.printf("%s\n", listTimestamp());
        System.out.printf("Average Turnaround Time: %.2f\n", outputs.get("avgTurnaround"));
        System.out.printf("Average Waiting Time: %.2f\n", outputs.get("avgWaiting"));
        System.out.printf("Average Response Time: %.2f\n", outputs.get("avgResponse"));
        System.out.printf("Throughput: %.2f\n\n", outputs.get("throughput"));
        
        listUsedProcesses(processesRan);
        
        return outputs;
    }
    
    /**
     * Returns the output for this run.
     */
    public Map<String, Float> getOutput() {
        return runOutput;
    }
    
    private String listTimechart() {
        return timechart.toString();
    }
    
    private String listTimestamp() {
        return timestamp.toString();
    }
    
    /**
     * List the used processes.
     */
    private void listUsedProcesses(List<Process> processes) {
        int size = processes.size();
        System.out.printf("%d processes:\n", size);
        for (int i = 0; i < size; i++) {
            Process current = processes.get(i);
            System.out.printf("%s: arrival=%.2f,runtime=%.2f,priority=%d\n", 
                    current.getName(), current.getArrivalTime(), 
                    current.getRunTime(), current.getPriority());
            if (i == size - 1) {
                System.out.printf("\n");
            }
        }
    }
    
    /**
     * Finds processes that are ready and reorders them based on estimated
     * run time.
     * @param index the index of the process that just ran
     * @param blocksUsed the number of blocks that have been used
     */
    private void reorderReadyProcesses(int index, int blocksUsed) {
        int start = index + 1, size = processes.size(), savedIndex = start;
        int minimum = 5;
        float minimumArrival = 100.0f;
        
        for (int i = start; i < size; i++) {
            Process aProcess = processes.get(i);
            int priority = aProcess.getPriority();
            float arrivalTime = aProcess.getArrivalTime();
            
            /* Check all the processes with an arrival time < number of blocks
             * used.
             */
            if (arrivalTime < (float) blocksUsed) {
                /* Priority is at least 4 or less */
                if (priority <= minimum) {
                    /* If priority of process is equal to current minimum, 
                     * check to see if its arrival time is less than
                     * the current saved earliest arrival time. If the priority is 
                     * smaller than the current one, save the time regardless.
                     */
                    if ((priority == minimum && arrivalTime < minimumArrival) 
                            || priority < minimum) {
                        minimumArrival = arrivalTime;
                    }
                    minimum = priority;
                    savedIndex = i;
                }
            } else {
                break;
            }
        }
        
        Process savedProcess = processes.remove(savedIndex);
        processes.add(start, savedProcess);
    }
}
