package cs149;

import java.util.Map;

public class RunAlgorithms {
    public static void main(String[] args) {
        runFCFS();
        runSJF();
        runSRT();
        runRR();
        runHPFNP();
        runHPFP();
    }
    
    private static void runFCFS() {
        float totalThroughput, totalTurnaround, totalWaiting, totalResponse;
        totalThroughput = totalTurnaround = totalWaiting = totalResponse = 0;
        float throughput, turnaround, waiting, response;
        
        for (int i = 1; i < 6; i++) {
            System.out.println("First Come First Served Run " + i + "\n");
            FirstComeFirstServed run = new FirstComeFirstServed();
            Map<String, Float> output = run.getOutput();
            throughput = output.get("throughput");
            turnaround = output.get("avgTurnaround");
            waiting = output.get("avgWaiting");
            response = output.get("avgResponse");
            
            totalThroughput += throughput;
            totalTurnaround += (throughput * turnaround);
            totalWaiting += (throughput * waiting);
            totalResponse += (throughput * response);
        }
        System.out.println("First Come First Served 5 Run Summary\n");
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurnaround / totalThroughput);
        System.out.printf("Average Waiting Time: %.2f\n", totalWaiting / totalThroughput);
        System.out.printf("Average Response Time: %.2f\n", totalResponse / totalThroughput);
        System.out.printf("Average Throughput: %.2f\n\n", totalThroughput / 5.0f);
    }
    
    private static void runSJF() {
        float totalThroughput, totalTurnaround, totalWaiting, totalResponse;
        totalThroughput = totalTurnaround = totalWaiting = totalResponse = 0;
        float throughput, turnaround, waiting, response;
        
        for (int i = 1; i < 6; i++) {
            System.out.println("Shortest Job First Run " + i + "\n");
            ShortestJobFirst run = new ShortestJobFirst();
            Map<String, Float> output = run.getOutput();
            throughput = output.get("throughput");
            turnaround = output.get("avgTurnaround");
            waiting = output.get("avgWaiting");
            response = output.get("avgResponse");
            
            totalThroughput += throughput;
            totalTurnaround += (throughput * turnaround);
            totalWaiting += (throughput * waiting);
            totalResponse += (throughput * response);
        }
        System.out.println("Shortest Job First 5 Run Summary\n");
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurnaround / totalThroughput);
        System.out.printf("Average Waiting Time: %.2f\n", totalWaiting / totalThroughput);
        System.out.printf("Average Response Time: %.2f\n", totalResponse / totalThroughput);
        System.out.printf("Average Throughput: %.2f\n\n", totalThroughput / 5.0f);
    }
    
    private static void runSRT() {
        float totalThroughput, totalTurnaround, totalWaiting, totalResponse;
        totalThroughput = totalTurnaround = totalWaiting = totalResponse = 0;
        float throughput, turnaround, waiting, response;
        
        for (int i = 1; i < 6; i++) {
            System.out.println("Shortest Remaining Time Run " + i + "\n");
            ShortestRemainingTime run = new ShortestRemainingTime();
            Map<String, Float> output = run.getOutput();
            throughput = output.get("throughput");
            turnaround = output.get("avgTurnaround");
            waiting = output.get("avgWaiting");
            response = output.get("avgResponse");
            
            totalThroughput += throughput;
            totalTurnaround += (throughput * turnaround);
            totalWaiting += (throughput * waiting);
            totalResponse += (throughput * response);
        }
        System.out.println("Shortest Remaining Time 5 Run Summary\n");
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurnaround / totalThroughput);
        System.out.printf("Average Waiting Time: %.2f\n", totalWaiting / totalThroughput);
        System.out.printf("Average Response Time: %.2f\n", totalResponse / totalThroughput);
        System.out.printf("Average Throughput: %.2f\n\n", totalThroughput / 5.0f);
    }
    
    private static void runRR() {
        float totalThroughput, totalTurnaround, totalWaiting, totalResponse;
        totalThroughput = totalTurnaround = totalWaiting = totalResponse = 0;
        float throughput, turnaround, waiting, response;
        
        for (int i = 1; i < 6; i++) {
            System.out.println("Round Robin Run " + i + "\n");
            RoundRobin run = new RoundRobin();
            Map<String, Float> output = run.getOutput();
            throughput = output.get("throughput");
            turnaround = output.get("avgTurnaround");
            waiting = output.get("avgWaiting");
            response = output.get("avgResponse");
            
            totalThroughput += throughput;
            totalTurnaround += (throughput * turnaround);
            totalWaiting += (throughput * waiting);
            totalResponse += (throughput * response);
        }
        System.out.println("Round Robin 5 Run Summary\n");
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurnaround / totalThroughput);
        System.out.printf("Average Waiting Time: %.2f\n", totalWaiting / totalThroughput);
        System.out.printf("Average Response Time: %.2f\n", totalResponse / totalThroughput);
        System.out.printf("Average Throughput: %.2f\n\n", totalThroughput / 5.0f);
    }
    
    private static void runHPFNP() {
        float totalThroughput, totalTurnaround, totalWaiting, totalResponse;
        totalThroughput = totalTurnaround = totalWaiting = totalResponse = 0;
        float throughput, turnaround, waiting, response;
        
        for (int i = 1; i < 6; i++) {
            System.out.println("Highest Priority First NonPreemptive Run " + i + "\n");
            HighestPriorityFirstNP run = new HighestPriorityFirstNP();
            Map<String, Float> output = run.getOutput();
            throughput = output.get("throughput");
            turnaround = output.get("avgTurnaround");
            waiting = output.get("avgWaiting");
            response = output.get("avgResponse");
            
            totalThroughput += throughput;
            totalTurnaround += (throughput * turnaround);
            totalWaiting += (throughput * waiting);
            totalResponse += (throughput * response);
        }
        System.out.println("Highest Priority First NonPreemptive 5 Run Summary\n");
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurnaround / totalThroughput);
        System.out.printf("Average Waiting Time: %.2f\n", totalWaiting / totalThroughput);
        System.out.printf("Average Response Time: %.2f\n", totalResponse / totalThroughput);
        System.out.printf("Average Throughput: %.2f\n\n", totalThroughput / 5.0f);
    }
    
    private static void runHPFP() {
        float totalThroughput, totalTurnaround, totalWaiting, totalResponse;
        totalThroughput = totalTurnaround = totalWaiting = totalResponse = 0;
        float throughput, turnaround, waiting, response;
        
        for (int i = 1; i < 6; i++) {
            System.out.println("Highest Priority First Preemptive Run " + i + "\n");
            HighestPriorityFirstP run = new HighestPriorityFirstP();
            Map<String, Float> output = run.getOutput();
            throughput = output.get("throughput");
            turnaround = output.get("avgTurnaround");
            waiting = output.get("avgWaiting");
            response = output.get("avgResponse");
            
            totalThroughput += throughput;
            totalTurnaround += (throughput * turnaround);
            totalWaiting += (throughput * waiting);
            totalResponse += (throughput * response);
        }
        System.out.println("Highest Priority First Preemptive 5 Run Summary\n");
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurnaround / totalThroughput);
        System.out.printf("Average Waiting Time: %.2f\n", totalWaiting / totalThroughput);
        System.out.printf("Average Response Time: %.2f\n", totalResponse / totalThroughput);
        System.out.printf("Average Throughput: %.2f\n\n", totalThroughput / 5.0f);
    }
}
