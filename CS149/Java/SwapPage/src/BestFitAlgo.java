import java.util.ArrayList;


public class BestFitAlgo {

    String[] memorySpace = new String[100];
    ArrayList<SwapProcess> activeProcess = new ArrayList<SwapProcess>();
    ArrayList<Hole> holeList = new ArrayList<Hole>();
    
    public BestFitAlgo()
    {
        for(int i = 0 ; i < 100;i++)
        {
            memorySpace[i] = ".";
        }
    }

    public int nextHoleIndex(int start)
    {
        int index = 102;
        int count = start;
        boolean hole = false;
        while(count <100 && !hole)
        {
            if(memorySpace[count].equalsIgnoreCase("."))
            {
                
                index = count;
                hole = true;
            }
            count++;
        }
        
        return index;
    }

    public boolean addProcess(SwapProcess proc)
    {
        boolean holeFound = false;
        SwapProcess tempProc = proc;
        int size = tempProc.getSize();
        newHoleList();
        int maxSize = 102;
        Hole chosenHole = new Hole(0,0);
        
        for (int i = 0; i < holeList.size();i++)
        {
            holeFound = false;
            Hole hole = holeList.get(i);
            if(hole.getSize() >= proc.getSize() && hole.getSize() < maxSize)
            {
                maxSize = hole.getSize();
                        chosenHole = hole;
                        holeFound = true;
            }
        }
        
        if(holeFound)
        {
            int startPoint = chosenHole.getStartIndex();
            int endPoint = (startPoint+tempProc.getSize());
            for(int i = startPoint; i < endPoint;i++)
            {
                memorySpace[i] = tempProc.toString();
                
            }
            activeProcess.add(tempProc);
        }
        
        return holeFound;
    }

    public void newHoleList()
    {
        holeList = new ArrayList<Hole>();
        int indexHole;
        
        for (int i = 0 ;i<100;i++)
        {
            indexHole = nextHoleIndex(i);

            if(indexHole <100)
            {
                Boolean endOfHole = false;
                int startHole = indexHole;
                
                while(!endOfHole && startHole < 100)
                {
                    
                    String value = memorySpace[startHole];
                    if(!value.equalsIgnoreCase("."))
                    {
                        endOfHole = true;
                        i = startHole;
                    }else if(startHole == 99)
                    {
                        i = startHole;
                        startHole++;
                    }else
                    {
                        startHole++;
                    }
                }
                
                int endIndex = startHole-1;             
                Hole newHole = new Hole(indexHole,endIndex);
                holeList.add(newHole);
            }
            
        }
        
        
        
    }
    public boolean cleanProcess()
    {
        Boolean removed = false;
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
                removed = true;
            }
        }
        return removed;
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