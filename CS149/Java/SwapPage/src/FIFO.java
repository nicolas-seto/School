
import java.util.ArrayDeque;
import java.util.Queue;


public class FIFO {

    private PageProcess process;
    private static final int FOUR = 4;
    private static final int TOTAL_REF = 100;
    private static StringBuffer buffer;
    private int[] pageFrame;
    private int hits;
    private int pagedIn;
    private int pagedOut;
    private Queue<Integer> pageIndex;
    
    public FIFO(PageProcess aProcess) {
        process = aProcess;
        pageFrame = new int[FOUR];
        this.initializePageFrame();
        hits = 0;
        pagedIn = pagedOut = -1;
        pageIndex = new ArrayDeque<Integer>();
    }
    
    // Set values to -1 so that we know it's empty.
    private void initializePageFrame() {
        for (int i = 0; i < FOUR; i++) {
            pageFrame[i] = -1;
        }
    }
    
    public void run() {
        
    }
    
    private void runProcess() {
        
        for (int i = 0; i < TOTAL_REF; i++) {
            int page = process.run();
           
            if (containsPage(page)) {
                hits++;
                pagedIn = pagedOut = -1;
            } else {
                this.insertToPageFrame(page);
            }
        }
    }
    
    private boolean containsPage(int j) {
        for (int i = 0; i < FOUR; i++) {
            if (pageFrame[i] == j) {
                return true;
            }
        }
        return false;
    }
    
    private void insertToPageFrame(int page) {
        if (!isPageFrameFilled()) {
            for (int i = 0; i < FOUR; i++) {
                if (pageFrame[i] == -1) {
                    pageFrame[i] = page;
                    pageIndex.add(i);
                    break;
                }
            }
        } else {
            int oldIndex = pageIndex.remove();
            pagedOut = pageFrame[oldIndex];
            
            pageFrame[oldIndex] = page;
            pageIndex.add(oldIndex);
        }
        
        pagedIn = page;
    }
    
    private boolean isPageFrameFilled() {
        return (pageIndex.size() < FOUR) ? false : true;
    }
    
    private void displayPageFrameContents() {
        buffer = new StringBuffer();
        buffer.append('[');
        for (int i = 0; i < FOUR; i++) {
            if (i != FOUR - 1) {
                buffer.append(pageFrame[i] + ",");
            } else {
                buffer.append(pageFrame[i] + "] ");
            }
        }
        if (pagedIn != -1) {
            
            buffer.append(" ")
        }
    }
}
