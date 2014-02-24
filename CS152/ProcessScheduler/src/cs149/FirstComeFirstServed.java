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
    
    /**
     * runTimeSum is the number of blocks that have been used so far.
     */
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
        int counter = 0;
        float turnaroundTime = 0, waitingTime = 0, responseTime = 0;
        List<Process> processesRan;
        /* TOTAL_QUANTA is the total running time, in this case 100 units */
        while (runTimeSum <= TOTAL_QUANTA) {
            Process aProcess = processes.get(counter);
            float arrivalTime = aProcess.getArrivalTime();
            float runTime = aProcess.getRunTime();
            /* Must find the exact endTime because process can start in middle of quantum.
             * This endTime is the assumption that the process can start right when
             * it arrives.
              */
            float endTime = arrivalTime + runTime;
            
            if (counter == 0) {
                /* First process is run. No waiting for it. */
                runTimeSum = (int) Math.ceil(endTime);
                turnaroundTime += runTime;
            } else {
                /* There's an idle period >= 0 between the last process and this
                 * process. The process did not have to wait and fast response.
                 */
                if (arrivalTime >= (float) runTimeSum) {
                    runTimeSum = (int) Math.ceil(endTime);
                    turnaroundTime += runTime;
                } else {
                /* This process starts right after the end of the quantum of
                 * the last process
                 */
                    float currentWait = (float) runTimeSum - arrivalTime;
                    turnaroundTime += currentWait + runTime;
                    waitingTime += currentWait;
                    responseTime += currentWait;
                    runTimeSum += aProcess.getRunBlockSize();
                }
            }
            counter++;
            if (runTimeSum > TOTAL_QUANTA) {
                processesRan = processes.subList(0, counter);
            }
        }
        return new HashMap<String, Float>();
    }
    
    private String listTimechart() {
        return timechart.toString();
    }
}
