
import java.util.ArrayList;

public class Swapping {
public static int totalBFArun;
public static int totalFFArun;
public static int totalNFArun;

    
    public static void main(String[] args)
    {
        for (int i = 0; i < 5; i++) {
            System.out.printf("Run %d of First Fit:\n", i);
            testFFA();
            System.out.printf("\n");
        }
        System.out.printf("Average number of process run for FFA is %.2f.\n\n", (float) totalFFArun / 5.0);
        
        for (int i = 0; i < 5; i++) {
            System.out.printf("Run %d of Next Fit:\n", i);
            testNFA();
            System.out.printf("\n");
        }
        System.out.printf("Average number of process run for NFA is %.2f.\n\n", (float) totalNFArun / 5.0);
        
        for (int i = 0; i < 5; i++) {
            System.out.printf("Run %d of Best Fit:\n", i);
            testBFA();
            System.out.printf("\n");
        }
        System.out.printf("Average number of process run for BFA is %.2f.\n\n", (float) totalBFArun / 5.0);
    }
    
    public static void testBFA()
    {
        BestFitAlgo BFA = new BestFitAlgo();
        ArrayList<SwapProcess> processList = generateProcess();
        int second = 0;
        do
        {   
            boolean remove = false;
            boolean full = false;
            do
            {
                SwapProcess tempProc = processList.get(0);
                boolean added = BFA.addProcess(tempProc);
                if(added)
                {
                    System.out.println("proc Add"+BFA.toString());
                    processList.remove(0);
                }else{
                    full = true;
                }
            }while(!full);
            
            BFA.decrementProcess();
            BFA.cleanProcess();
            if(remove)
            {
                System.out.println("proc remove"+BFA.toString());
            }
            second++;
            
        }while(second < 60);
        int numberOfRun = 150-processList.size();
        totalBFArun = totalBFArun + numberOfRun;
    }
    
    public static void testFFA()
    {
        
        FirstFitAlgo FFA = new FirstFitAlgo();
        ArrayList<SwapProcess> processList = generateProcess();
        int second = 0;
        do
        {   
            boolean added = false;
            boolean remove = false;
            boolean full = false;
            do
            {
                SwapProcess tempProc = processList.get(0);
                added = FFA.addProcess(tempProc);
                if(added)
                {
                    System.out.println("proc Add"+FFA.toString());
                    processList.remove(0);
                }else{
                    full = true;
                }
            }while(!full);
            
            FFA.decrementProcess();
            remove = FFA.cleanProcess();

            if(remove)
            {
                System.out.println("proc remove"+FFA.toString());
            }
            second++;
        }while(second < 60);
        int numberOfRun = 150-processList.size();
        totalFFArun = totalFFArun + numberOfRun;
    }
    
    public static void testNFA()
    {
        
        NextFitAlgo NFA = new NextFitAlgo();
        ArrayList<SwapProcess> processList = generateProcess();
        int second = 0;
        do
        {   
            boolean remove = false;
            boolean full = false;
            do
            {
                SwapProcess tempProc = processList.get(0);
                boolean added = NFA.addProcess(tempProc);
                if(added)
                {
                    System.out.println("proc Add"+NFA.toString());
                    processList.remove(0);
                }else{
                    full = true;
                }
            }while(!full);
            
            NFA.decrementProcess();
            remove = NFA.cleanProcess();
            if(remove)
            {
                System.out.println("proc remove"+NFA.toString());
            }
            second++;
            
        }while(second < 60);
        int numberOfRun = 150-processList.size();
        totalNFArun = totalNFArun + numberOfRun;
    }
    
    public static ArrayList<SwapProcess> generateProcess()
    {
        ArrayList<SwapProcess> list = new ArrayList<SwapProcess>();
        for(int i = 0; i < 150;i++)
        {
            String name = "*"+i+"*";
            SwapProcess tempProc = new SwapProcess(name);
            list.add(tempProc);
        }
        
        return list;
    }
}
