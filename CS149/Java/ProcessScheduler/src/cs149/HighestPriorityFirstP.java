package cs149;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighestPriorityFirstP implements Algorithm {
    
    private List<Process> processes;
    private ProcessGenerator generator;
    private int runTimeSum, count;
    private StringBuilder timechart, timestamp;
    private Map<String, Float> runOutput;
    private static final int TOTAL_QUANTA = 100;
    private float turnaroundTime, waitingTime, responseTime;
    
    public HighestPriorityFirstP() {
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
        int index = 0, remainingTime = 0;
        List<Process> processesRan = new ArrayList<Process>();
        Map<String, Process> processesStarted = new HashMap<String, Process>();
        Map<String, Float> outputs = new HashMap<String, Float>();
        
        for (int i = 0; i < TOTAL_QUANTA; i++) {
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
                        /* Increase wait time for a process by 1 unit. Decrease
                         * the age. If the age reaches a multiple of 5, increase
                         * priority.
                         */
                        iteration.incrementWaitTimeBy(1.0f);
                        int newAge = iteration.decrementAge();
                        if (newAge % 5 == 0 && newAge / 5 >= 1) {
                            iteration.increasePriority();
                        }
                        processesStarted.put(entryKey, iteration);
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
             * all the processes, and add the turnaround/waiting time of the process. 
             */
            if (aProcess.getRunTimer() <= 0) {
                processesRan.add(aProcess);
                processes.remove(index); // Remove from overall List
                processesStarted.remove(name); // Remove from Map
                turnaroundTime += aProcess.getRunTime() + aProcess.getWaitTime();
                waitingTime += aProcess.getWaitTime();
            }
            
            runTimeSum++;
            remainingTime = remainingRunTime(processesStarted);
            
            if (remainingTime >= (TOTAL_QUANTA - runTimeSum)) {
                // iterate through rest of Map
                processesRan = runMap(processesRan, processesStarted);
                break;
            } else {
            /* See whether there is a new process that has higher priority.
             */
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
     * Complete the remaining processes that have already started.
     * @param processesRan the List of all the completed processes
     * @param processesStarted the Map of the started processes
     * @return
     */
    private List<Process> runMap(List<Process> processesRan, Map<String, Process> processesStarted) {
        List<Process> completedProcesses = processesRan;
        Map<String, Process> startedProcesses = processesStarted;
        String name = "";
        
        while (!startedProcesses.isEmpty()) {
            /* We get the process with the highest priorities.
             */
            name = highestPriority(startedProcesses);
            Process aProcess = startedProcesses.get(name);
            String timestampSnippet = "";
            
            /* The arrival time of the process was either before or equal to
             * the quantum. The process runs for one block.
             */
            for (Map.Entry<String, Process> entry : startedProcesses.entrySet())
            {
                /* Need to have a Map to increment the wait times of every
                 * running process.
                 */
                String entryKey = entry.getKey();
                Process iteration = entry.getValue();
                if (!name.equals(entry.getKey())) {
                    /* Increase wait time for a process by 1 unit. Decrease
                     * the age. If the age reaches a multiple of 5, increase
                     * priority.
                     */
                    iteration.incrementWaitTimeBy(1.0f);
                    int newAge = iteration.decrementAge();
                    if (newAge % 5 == 0 && newAge / 5 >= 1) {
                        iteration.increasePriority();
                    }
                    processesStarted.put(entryKey, iteration);
                } else {
                /* Update the run time of the process. Reassign it to aProcess.
                 */
                    startedProcesses.put(entryKey, iteration.decrementRunTimeBy(1.0f));
                    aProcess = startedProcesses.get(entryKey);
                }
            }
            
            timechart.append("[" + name + "]");
            
            if (count % 10 == 0) {
                timestampSnippet = "0   ";
            } else {
                timestampSnippet = "    ";
            }
            timestamp.append(timestampSnippet);
            count++;
            
            /* When a process finishes, add it to the finished List, remove it from
             * all the processes, and add the turnaround/waiting time of the process. 
             */
            if (aProcess.getRunTimer() <= 0) {
                completedProcesses.add(aProcess);
                startedProcesses.remove(name); // Remove from Map
                turnaroundTime += aProcess.getRunTime() + aProcess.getWaitTime();
                waitingTime += aProcess.getWaitTime();
            }
        }
        
        return completedProcesses;
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
     * Calculate the sum of the run times and return it.
     * @param startedProcesses the processes that have started already
     * @return
     */
    private int remainingRunTime(Map<String, Process> startedProcesses) {
        int time = 0;
        for (Map.Entry<String, Process> entry : startedProcesses.entrySet())
        {
            time += (int) Math.ceil(entry.getValue().getRunTimer());
        }
        return time;
    }
    
    private String highestPriority(Map<String, Process> startedProcesses) {
        float minimumArrival = 10.0f, arrivalTime = 10.0f;
        int minimum = 5, priority = 0;
        String name = "";
        
        for (Map.Entry<String, Process> entry : startedProcesses.entrySet()) {
            priority = entry.getValue().getPriority();
            arrivalTime = entry.getValue().getArrivalTime();
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
                name = entry.getValue().getName();
            }
        }
        
        return name;
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
     * Finds processes that are ready and moves the process with the highest
     * priority and earliest arrival time to the front of List.
     * @param index the index of the process that just ran
     * @param blocksUsed the number of blocks that have been used
     */
    private void reorderReadyProcesses(int index, int blocksUsed) {
        int start = index, size = processes.size(), savedIndex = start;
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
