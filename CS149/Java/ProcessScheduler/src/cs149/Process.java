package cs149;

import java.util.Random;

public class Process {
    private float arrivalTime, arrivalScale = 99.0f;
    private float runTime, runTimer, waitTime, runTimeScale = 9.9f, runTimeOffset = 0.1f;
    private int priority, age;
    private String name;
    private Random randGen = new Random();

    public Process(String name, long seed)
    {
        this.name = name;
        randGen.setSeed(seed);
        waitTime = 0;
        age = 20;
        generate();
    }

    public Process(String name)
    {
        this.name = name;
        waitTime = 0;
        generate();
    }

    private void generate()
    {
        arrivalTime = randGen.nextFloat() * arrivalScale;
        runTime = (randGen.nextFloat() * runTimeScale) + runTimeOffset;
        runTimer = runTime;
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
    
    public float getRunTimer() {
        return runTimer;
    }
    
    public float getWaitTime() {
        return waitTime;
    }
    
    public void increasePriority() {
        priority--;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public int getRunBlockSize() {
        return (int) Math.ceil(runTime);
    }
    
    public int decrementAge() {
        if (age > 5) {
            return --age;
        } else {
            return 0;
        }
    }
    
    public Process decrementRunTimeBy(float runTime) {
        this.runTimer -= runTime;
        return this;
    }
    
    public Process incrementWaitTimeBy(float waitTime) {
        this.waitTime += waitTime;
        return this;
    }
}