package cs149;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstComeFirstServed implements Algorithm {
    
    private List<Process> processes;
    private ProcessGenerator generator;
    private int runTimeSum, count;
    private StringBuilder timechart, timestamp;
    private Map<String, Float> runOutput;
    private static final int TOTAL_QUANTA = 100;
    
    /**
     * runTimeSum is the number of blocks that have been used so far.
     */
    public FirstComeFirstServed() {
        generator = new ProcessGenerator(TOTAL_QUANTA);
        processes = generator.getProcesses();
        runTimeSum = 0;
        count = 0;
        timechart = new StringBuilder();
        timestamp = new StringBuilder();
        runOutput = run();
    }
    
    /**
     * Runs FCFS algorithm.
     */
    private Map<String, Float> run() {
        int counter = 0, idleBlocks = 0;
        float turnaroundTime = 0, waitingTime = 0, responseTime = 0;
        List<Process> processesRan = new ArrayList<Process>();
        Map<String, Float> outputs = new HashMap<String, Float>();
        
        /* TOTAL_QUANTA is the total running time, in this case 100 units */
        while (runTimeSum <= TOTAL_QUANTA) {
            Process aProcess = processes.get(counter);
            char name = aProcess.getName();
            int processBlockSize = aProcess.getRunBlockSize();
            float arrivalTime = aProcess.getArrivalTime();
            float runTime = aProcess.getRunTime();
            float currentWait = 0.0f;
            String timestampSnippet = "";
            /* Must find the exact endTime because process can start in middle of quantum.
             * This endTime is the assumption that the process can start right when
             * it arrives.
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
                /* There's an idle period >= 0 between the last process and this
                 * process. The process may have to wait for next quantum to
                 * start.
                 */
                if (arrivalTime >= (float) runTimeSum) {
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
                timechart.append("[ ]");
                
                if (count % 10 == 0) {
                    timestampSnippet = "0  ";
                    
                } else {
                    timestampSnippet = "   ";
                }
                timestamp.append(timestampSnippet);
                count++;
            }
            for (int i = 0; i < processBlockSize; i++) {
                timechart.append("[" + name + "]");
                
                if (count % 10 == 0) {
                    timestampSnippet = "0  ";
                    
                } else {
                    timestampSnippet = "   ";
                }
                timestamp.append(timestampSnippet);
                count++;
            }
            
            counter++;
            if (runTimeSum > TOTAL_QUANTA) {
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
}
