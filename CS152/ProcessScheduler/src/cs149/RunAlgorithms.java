package cs149;

import java.util.Map;

public class RunAlgorithms {
    public static void main(String[] args) {
        FirstComeFirstServed algorithm1 = new FirstComeFirstServed();
        System.out.println("FCFS Run1");
        Map<String, Float> output = algorithm1.run();
    }
}
