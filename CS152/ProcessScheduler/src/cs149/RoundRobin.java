package cs149;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundRobin implements Algorithm {
    
    private List<Process> processes;
    private ProcessGenerator generator;
    private int runTimeSum, count;
    private StringBuilder timechart, timestamp;
    private Map<String, Float> runOutput;
    private static final int TOTAL_QUANTA = 100;
    private float turnaroundTime, waitingTime, responseTime;
    
    public RoundRobin() {
        generator = new ProcessGenerator(TOTAL_QUANTA);
        processes = generator.getProcesses();
        runTimeSum = 0;
        count = 0;
        timechart = new StringBuilder();
        timestamp = new StringBuilder();
        turnaroundTime = waitingTime = responseTime = 0;
        runOutput = run();
    }
    
    /**
     * Runs SRT algorithm.
     */
    private Map<String, Float> run() {
        int index = 0;
        List<Process> processesRan = new ArrayList<Process>();
        Map<String, Process> processesStarted = new HashMap<String, Process>();
        Map<String, Float> outputs = new HashMap<String, Float>();
        processes = subListProcesses(processes, TOTAL_QUANTA);
        
        while (!processes.isEmpty()) {
            /* We assume this process is the next process that will be run for
             * sure. Take care of the selection at the end of the loop. First 
             * process is the process with the earliest arrival time.
             */
            Process aProcess = processes.get(index);
            String name = aProcess.getName();
            float arrivalTime = aProcess.getArrivalTime();
            float currentWait = 0.0f;
            String timestampSnippet = "";
            
            /* There is a block of idleness */
            if ((float) runTimeSum < arrivalTime) {
                
                timechart.append("[  ]");
                
                if (count % 10 == 0) {
                    timestampSnippet = "0   ";
                } else {
                    timestampSnippet = "    ";
                }
                timestamp.append(timestampSnippet);
                count++;
                
            } else {
            /* The arrival time of the process was either before or equal to
             * the quantum. The process runs for one block.
             */
                for (Map.Entry<String, Process> entry : processesStarted.entrySet()) {
                    /* Need to have a Map to increment the wait times of every
                     * running process.
                     */
                    String entryKey = entry.getKey();
                    Process iteration = entry.getValue();
                    if (!name.equals(entryKey)) {
                        processesStarted.put(entryKey, iteration.incrementWaitTimeBy(1.0f));
                    } else {
                    /* Update the run time of the process. Update the process
                     * in the ArrayList. Reassign it to aProcess.
                     */
                        processesStarted.put(entryKey, iteration.decrementRunTimeBy(1.0f));
                        processes.set(index, entry.getValue());
                        aProcess = processes.get(index);
                    }
                }
                
                /* Very first time process is run */
                if (!processesStarted.containsKey(name)) {
                    
                    currentWait = (float) runTimeSum - arrivalTime;
                    responseTime += currentWait;
                    aProcess.incrementWaitTimeBy(currentWait);
                    aProcess.decrementRunTimeBy(1);
                    processesStarted.put(name, aProcess);
                    processes.set(index, aProcess);
                    
                }
                
                timechart.append("[" + name + "]");
                
                if (count % 10 == 0) {
                    timestampSnippet = "0   ";
                } else {
                    timestampSnippet = "    ";
                }
                timestamp.append(timestampSnippet);
                count++;
            }
            
            /* When a process finishes, add it to the finished List, remove it from
             * the list of running processes, and add the turnaround/waiting time of the process. 
             */
            if (aProcess.getRunTimer() <= 0) {
                processesRan.add(aProcess);
                processes.remove(index);
                processesStarted.remove(name); // Remove from Map
                turnaroundTime += aProcess.getRunTime() + aProcess.getWaitTime();
                waitingTime += aProcess.getWaitTime();
            }
            
            runTimeSum++;
            
            /* See whether there is a new process that has arrival time 
             * <= runTimeSum 
             */
            if (processes.size() > 1) {
                reorderReadyProcesses(index, runTimeSum);
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
     * Grab enough processes to run within TOTAL_QUANTA.
     * @param allProcesses the List with all the generated processes
     * @return the subList
     */
    private List<Process> subListProcesses(List<Process> allProcesses, int quanta) {
        int sum = 0, i = 0, size = allProcesses.size();
        
        while (sum < quanta && i < size) {
            Process aProcess = allProcesses.get(i);
            sum += aProcess.getRunTime();
            i++;
        }
        
        return allProcesses.subList(0, i);
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
        int start = index;
        
        if (processes.get(start + 1).getArrivalTime() <= (float) runTimeSum) {
            processes.add(processes.remove(start));
        }
    }
}
