import java.util.ArrayList;


public class NextFitAlgo {
    String[] memorySpace = new String[100];
    ArrayList<SwapProcess> activeProcess = new ArrayList<SwapProcess>();

    public NextFitAlgo()
    {
        for(int i = 0 ; i < 100;i++)
        {
            memorySpace[i] = ".";
        }
    }

    //looks for next available hole starting from the start (index)
    public int nextHoleIndex(int start)
    {
        int index = 102;
        int count = start;
        boolean hole = false;
        //System.out.println("new in" + count);
        while(count <100 && !hole)
        {
            //System.out.println("new in" + count);
            if(memorySpace[count].equalsIgnoreCase("."))
            {

                index = count;
                hole = true;
                //System.out.println(hole);
            }
            count++;
        }

        return index;
    }


    public boolean addProcess(SwapProcess proc)
    {
        SwapProcess tempProc = proc;
        int size = tempProc.getSize();
        int startIndex = 0,index = 0;
        boolean added = false;

        while(index < 100 && !added)
        {
            startIndex = nextHoleIndex(index);

            //check if the process will get out of bound
            if(startIndex + size > 100)
            {
                index = 101;
            }
            else
            {
                int endPoint = (startIndex + size);
                //check if the process is too big for the hole
                for(int i = startIndex ; i < endPoint; i++)
                {
                    if(!memorySpace[i].equalsIgnoreCase("."))
                    {
                        index = 200;
                        startIndex = i;
                        added = false;
                    }
                    if(index != 200)
                    {

                        added= true;

                    }
                }
            }
            index++;
        }


        if(added == true)
        {
            int endPoint = (startIndex+size);
            for(int count = startIndex; count < endPoint; count++)
            {
                memorySpace[count] = tempProc.toString();
            }   
        }

        activeProcess.add(tempProc);

        return added;
    }

    public void cleanProcess()
    {
        ArrayList<SwapProcess> tempProcess = activeProcess;
        for(int i =0 ;i < tempProcess.size();i++)
        {
            if(tempProcess.get(i).getDuration() <=0)
            {
                SwapProcess tempProc = tempProcess.get(i);
                for(int m = 0; m <100;m++)
                {
                    if(memorySpace[m].equalsIgnoreCase(tempProc.getName()))
                    {
                        memorySpace[m] = ".";
                    }
                }
                activeProcess.remove(i);
            }
        }
    }

    public void decrementProcess()
    {
        for(int i =0 ;i < activeProcess.size();i++)
        {
            activeProcess.get(i).decrementDuration();
        }
    }

    public String toString()
    {
        String output = "-";
        for (int i = 0 ; i < 100; i ++)
        {
            output = output + memorySpace[i];
        }
        //System.out.println(output);
        return output;
    }
}
