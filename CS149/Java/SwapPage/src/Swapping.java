
import java.util.LinkedList;

public class Swapping {

	public static void main(String[] args)
	{
		FirstFitAlgo FFA = new FirstFitAlgo();
		Proc A = new SwapProcess('a');
		System.out.println(A.toString());
		FFA.addProcess(A);
		FFA.addProcess(new SwapProcess('b'));
		
		//FFA.printResult();
		LinkedList<Integer> test = new LinkedList<Integer>();
		//test.add(1);
		//test.add(2);
		//test.add(0,3);
		//test.add(0,4);
		//System.out.println(test);
	}
}
