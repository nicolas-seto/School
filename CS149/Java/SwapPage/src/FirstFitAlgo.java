
import java.util.LinkedList;

public class FirstFitAlgo {
	LinkedList<Proc> memoryList = new LinkedList<Proc>();

	public FirstFitAlgo()
	{
		Hole initialHole = new Hole();
		initialHole.setSize(100);
		memoryList.add(initialHole);
	}

	public boolean addProcess(Proc process)
	{
		Hole tempHole = new Hole();
		int counter = 0;
		Boolean added = false;
		while(counter < memoryList.size() &&  !added)
		{

			if(memoryList.get(counter).getClass() == tempHole.getClass())
			{
				tempHole = (Hole) memoryList.get(counter);
				if(process.getSize()<=memoryList.get(counter).getSize())
				{
					int remainingSize = tempHole.getSize()-process.getSize();
					if(remainingSize != 0)
					{
						Hole newHole = new Hole();
						newHole.setSize(remainingSize);
						
						memoryList.remove(counter);
						memoryList.add(process);
						memoryList.add(newHole);
						
					}else
					{
						memoryList.remove(counter);
						memoryList.add(process);
					}
					added = true;
				}



			}



			counter++;
		}


		return added;
	}



	public void setMemoryList()
	{

	}


	public void printResult()
	{
		String result = "Time map ";
		for(int i = 0; i < memoryList.size();i++)
		{
			result.concat(memoryList.get(i).toString());
			System.out.println(memoryList.get(i).toString());
			System.out.println(memoryList.get(i).getSize());
		}
		System.out.println(result);
		
	}







}
