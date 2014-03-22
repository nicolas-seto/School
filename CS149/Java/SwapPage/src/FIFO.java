
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Implements First In First Out for Paging.
 * @author Home
 *
 */
public class FIFO {

    private PageProcess process;
    private static final int FOUR = 4;
    private static final int TOTAL_REF = 100;
    private static StringBuffer buffer;
    private int[] pageFrame;
    private int hits;
    private int hitPage;
    private int pagedIn;
    private int pagedOut;
    private Queue<Integer> pageIndex;
    
    public FIFO(PageProcess aProcess) {
        process = aProcess;
        pageFrame = new int[FOUR];
        this.initializePageFrame();
        hits = 0;
        hitPage = pagedIn = pagedOut = -1;
        pageIndex = new ArrayDeque<Integer>();
    }
    
    // Set values to -1 so that we know it's empty.
    private void initializePageFrame() {
        for (int i = 0; i < FOUR; i++) {
            pageFrame[i] = -1;
        }
    }
    
    public void run() {
        this.runProcess();
    }
    
    /**
     * Runs a process for the total number of page references.
     */
    private void runProcess() {
        
        for (int i = 0; i < TOTAL_REF; i++) {
            int page = process.run();
            
            // If the page is in physical memory, it's a hit, otherwise evict the page at the head of the deque
            if (containsPage(page)) {
                hits++;
                pagedIn = pagedOut = -1;
                hitPage = page;
            } else {
                this.insertToPageFrame(page);
            }
            
            this.displayPageFrameContents();
        }
    }
    
    /**
     * Iterates through the pageFrame array to check if the page exists.
     * @param j the page
     * @return true if exists, false otherwise
     */
    private boolean containsPage(int j) {
        for (int i = 0; i < FOUR; i++) {
            if (pageFrame[i] == j) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * If this method is called, there was a page fault. Insert new page
     * after evicting old page.
     * @param page
     */
    private void insertToPageFrame(int page) {
        /* If the pageFrame isn't filled, then simply append the page to the array. 
         * pageIndex holds the indices of the pages that were added in order.
         * The head of pageIndex is the index of the page that was added first.
         * The tail of pageIndex is the index of the page that was added last.
         */
        if (!isPageFrameFilled()) {
            for (int i = 0; i < FOUR; i++) {
                if (pageFrame[i] == -1) {
                    pageFrame[i] = page;
                    pageIndex.add(i);
                    break;
                }
            }
        } else {
            // Remove the head of the deque, and append to the tail.
            int oldIndex = pageIndex.remove();
            pagedOut = pageFrame[oldIndex];
            
            pageFrame[oldIndex] = page;
            pageIndex.add(oldIndex);
        }
        
        pagedIn = page;
        hitPage = -1;
    }
    
    /**
     * Returns whether the pageFrame is full.
     * @return true if == 4, false otherwise
     */
    private boolean isPageFrameFilled() {
        return (pageIndex.size() < FOUR) ? false : true;
    }
    
    /**
     * Display the contents of the pageFrame.
     */
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
            buffer.append("Paged in: " + pagedIn + " ");
        }
        if (pagedOut != -1) {
            buffer.append("Page evicted: " + pagedOut + " ");
        }
        if (hitPage != -1) {
            buffer.append("Hit page: " + hitPage + " ");
        }
        
        System.out.printf("%s\n", buffer.toString());
    }
    
    /**
     * Calculate the hit ratio for the run.
     * @return the percentage of hits
     */
    public double hitRatio() {
        return ((double) hits / (double) TOTAL_REF) * 100;
    }
}
