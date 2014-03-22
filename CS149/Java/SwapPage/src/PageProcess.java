
import java.util.Random;

public class PageProcess extends Proc {
    
    private Random rand;
    private static final int SEVEN = 7;
    private static final int THREE = 3;
    private int referenceCount;
    private int lastIndex;
    
    public PageProcess(char name) {
        super(name, DEFAULT_SIZE);
        rand = new Random();
        referenceCount = 0;
    }
    
    public int run() {

        int r = rand.nextInt(this.getPageSize());
        
        if (referenceCount == 0) {
            lastIndex = r;
        } else {
            if (r < SEVEN) {
                lastIndex += rand.nextInt(THREE) - 1;
                if (lastIndex == -1) {
                    lastIndex += this.getPageSize();
                } else if (lastIndex == this.getPageSize()) {
                    lastIndex = 0;
                }
            } else {
                int i;
                while (Math.abs((i = rand.nextInt(this.getPageSize())) - lastIndex) < 2) {
                    
                }
                lastIndex = i;
            }            
        }
        referenceCount++;
        return lastIndex;
    }
}
