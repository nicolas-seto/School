package cs149;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Scheduling {

    public static void main (String[] args)
    {
        FCFSrun(generateProcess(0));
    }


    public static void FCFSrun(ArrayList<Process> pList)
    {
        float tempBurstTime = 0;
        float burstOffset = 0;
        float responseTime = 0;
        for(int i = 0 ; i < pList.size();i++ )
        {
            System.out.println(pList.get(i).toString());
        }
        
        int processId = 0,processingTime = 0;
        for (int quanta =0; quanta < 101; quanta++)
        {
            System.out.println(quanta + "   Process Id: "+ pList.get(processId).getID());
            if(processingTime >= pList.get(processId).getRunTime())
            {
                tempBurstTime = tempBurstTime + pList.get(processId).getRunTime();
                burstOffset = burstOffset + (tempBurstTime-(float)(Math.round(pList.get(processId).getArrivalTime()*10f)/10f));
                responseTime = responseTime + (quanta-pList.get(processId).getArrivalTime());
                processId++;
                processingTime = 0;
            }
            processingTime++;
        }
        
        System.out.println("\nAverage Turn Around Time :"+(float)(Math.round(burstOffset/(float)processId)*10f)/10f);
        System.out.println("Average response Time :"+(float)(Math.round(responseTime/(float)processId)*10f)/10f);
    }
    
    public static ArrayList<Process> generateProcess(int seed)
    {
        
        ArrayList<Process> list = new ArrayList<Process>();
        float totalRunTime=0;
        float arrivalTime = 0;
        Random randNumbGen = new Random(seed);
        
        do
        {
            Process newProcess = new Process(randNumbGen);
            newProcess.setArrivalTime(arrivalTime);
            arrivalTime = arrivalTime + (randNumbGen.nextFloat()*(newProcess.getTotalRunTime()+2.0f));
            totalRunTime = totalRunTime + newProcess.getTotalRunTime();
            list.add(newProcess);
        }while(totalRunTime < 99); //create process making sure they will last the entire runtime?
        
        //Collections.sort(list);//sort in increasing time of arrival time order
        for(int i = 0 ; i < list.size();i++ )//set the properprocessId
        {
            list.get(i).setName(i+1);
        }
        
        /*for(int i = 0 ; i < list.size();i++ )
        {
            System.out.println(list.get(i).toString());
        }*/ //testing.
        
        
        return list;
    }
}
