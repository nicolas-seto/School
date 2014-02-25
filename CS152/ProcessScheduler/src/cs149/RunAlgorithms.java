package cs149;

import java.util.Map;

public class RunAlgorithms {
    public static void main(String[] args) {
        System.out.println("FCFS Run1\n");
        FirstComeFirstServed algorithm1 = new FirstComeFirstServed();
        //Map<String, Float> output = algorithm1.getOutput();
        
        System.out.println("SJF Run1\n");
        ShortestJobFirst algorithm2 = new ShortestJobFirst();
        //Map<String, Float> output2 = algorithm2.getOutput();
        
        System.out.println("HPF-NP Run1\n");
        HighestPriorityFirstNP algorithm3 = new HighestPriorityFirstNP();
        
        System.out.println("SRT Run1\n");
        ShortestRemainingTime algorithm4 = new ShortestRemainingTime();
    }
}
