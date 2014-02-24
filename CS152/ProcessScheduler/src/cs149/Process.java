package cs149;

import java.util.Random;

public class Process {
    private float arrivalTime, arrivalScale = 99.0f;
    private float runTime, runTimeScale = 9.9f, runTimeOffset = 0.1f;
    private int priority;
    private String name;
    private Random randGen = new Random();

    public Process(String name, long seed)
    {
        this.name = name;
        randGen.setSeed(seed);
        generate();
    }

    public Process(String name)
    {
        this.name = name;
        generate();
    }

    private void generate()
    {
        arrivalTime = randGen.nextFloat() * arrivalScale;
        runTime = (randGen.nextFloat() * runTimeScale) + runTimeOffset;
        priority = randGen.nextInt(4) + 1;
    }
    
    public String getName() {
        return name;
    }

    public float getArrivalTime() {
        return arrivalTime;
    }
    
    public float getRunTime() {
        return runTime;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public int getRunBlockSize() {
        return (int) Math.ceil(runTime);
    }
    
    public void updateRunTime(float runTime) {
        this.runTime = runTime;
    }
}