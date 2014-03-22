
import java.util.Random;

public class SwapProcess extends Proc {
    private int duration;
    private static final int SIZE0 = 5;
    private static final int SIZE1 = 11;
    private static final int SIZE2 = 17;
    private static final int SIZE3 = 31;
    private static final int[] sizes = {SIZE0, SIZE1, SIZE2, SIZE3};

    public SwapProcess(String name)
    {
        super(name, sizes[(int) Math.floor(Math.random()*4)]);
        duration = (int) Math.floor(Math.random()*5) + 1;
    }
    
    public int getDuration()
    {
        return duration;
    }
    
    public void decrementDuration()
    {
        duration--;
    }
}
