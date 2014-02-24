package cs149;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Scheduling {

    public static void main (String[] args)
    {


        generateProcess();

    }


    public void FCFSrun()
    {

    }
    
    public static void generateProcess()
    {
        
        ArrayList<Process> list = new ArrayList<Process>();
        float totalRunTime=0;
        Random randNumbGen = new Random(0);
        
        do
        {
            Process newProcess = new Process(randNumbGen);
            totalRunTime = totalRunTime + newProcess.getTotalRunTime();
            list.add(newProcess);
        }while(totalRunTime < 99); //create process making sure they will last the entire runtime?
        
        Collections.sort(list);//sort in increasing time of arrival time order
        System.out.println(totalRunTime);
        for(int i = 0 ; i < list.size();i++ )//set the properprocessId
        {
            list.get(i).setName(i+1);
        }
        
        /*for(int i = 0 ; i < list.size();i++ )
        {
            System.out.println(list.get(i).toString());
        }*/ //testing.
    }
}