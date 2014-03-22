
import java.util.ArrayList;
import java.util.LinkedList;

public class Swapping {

    public static void main(String[] args)
    {
        
        testFFA();
        
        //Proc A = new SwapProcess("*1*");
        //System.out.println(A.toString());
        //FFA.addProcess(A);
        //FFA.addProcess(new SwapProcess("*2*"));
        
        //FFA.printResult();
        //LinkedList<Integer> test = new LinkedList<Integer>();
        //test.add(1);
        //test.add(2);
        //test.add(0,3);
        //test.add(0,4);
        //System.out.println(test);
    }
    
    
    public static void testFFA()
    {
        
        FirstFitAlgo FFA = new FirstFitAlgo();
        ArrayList<SwapProcess> processList = generateProcess();
        int second = 0;
        //SwapProcess tempProc = processList.get(0);
        //FFA.addProcess(tempProc);
        //System.out.println("test"+FFA);
        do
        {   
            //System.out.println("test"+FFA);System.out.println("test"+FFA);
            System.out.println("second"+ second);
            
            boolean full = false;
            do
            {
                //System.out.println(processList + "test");
                SwapProcess tempProc = processList.get(0);
                //System.out.println(tempProc.getName() + "--" +tempProc.getSize());
                boolean added = FFA.addProcess(tempProc);
                //System.out.println(tempProc.getName() + "after");
                if(added)
                {
                    processList.remove(0);
                }else{
                    full = true;
                }
            }while(!full);
            System.out.println(FFA.toString());
            FFA.decrementProcess();
            FFA.cleanProcess();
            second++;
            
        }while(second < 60);
        
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
