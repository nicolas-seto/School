package cs149;

import java.util.Random;

public class Process {
    private float arrivalTime, arrivalScale = 99.0f;
    private float runTime, runTimeScale = 9.9f, runTimeOffset = 0.1f;
    private int priority;
    private char name;
    private Random randGen = new Random();

    public Process(char name, long seed)
    {
        this.name = name;
        randGen.setSeed(seed);
        generate();
    }

    public Process(char name)
    {
        this.name = name;
        generate();
    }

    public void generate()
    {
        arrivalTime = randGen.nextFloat() * arrivalScale;
        runTime = (randGen.nextFloat() * runTimeScale) + runTimeOffset;
        priority = randGen.nextInt(4) + 1;
    }
    
    public char getName() {
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
}