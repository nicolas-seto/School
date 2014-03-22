
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Least-recently used algorithm for paging.
 * @author Home
 *
 */
public class LRU {

    private PageProcess process;
    private static final int FOUR = 4;
    private static final int TOTAL_REF = 100;
    private static StringBuffer buffer;
    private int[] pageFrame;
    private List<Integer> recentlyUsed;
    private int hits;
    private int hitPage;
    private int pagedIn;
    private int pagedOut;
    
    public LRU(PageProcess aProcess) {
        process = aProcess;
        pageFrame = new int[FOUR];
        recentlyUsed = new ArrayList<Integer>();
        this.initializePageFrame();
        hits = 0;
        hitPage = pagedIn = pagedOut = -1;
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
     * Use an arraylist to reorder pages in terms of recently used. If there's
     * a hit, then the page is removed and sent to the head of the list. If there's
     * a fault, then the least-frequently used page is removed and the new page
     * is inserted at the head of the list.
     */
    private void runProcess() {
        
        for (int i = 0; i < TOTAL_REF; i++) {
            int page = process.run();
            int index = containsPage(page);
            if (index != -1) {
                hits++;
                pagedIn = pagedOut = -1;
                hitPage = page;
                
                // Move the page that is hit to the front.
                for (int j = 0; j < FOUR; j++) {
                    if (recentlyUsed.get(j) == index) {
                        recentlyUsed.add(0, recentlyUsed.remove(j));
                        break;
                    }
                }
            } else {
                this.insertToPageFrame(page);
            }
            
            this.displayPageFrameContents();
        }
    }
    
    /**
     * Returns the index of the page in the page frame in physical memory.
     * @param j the page
     * @return the index of the page
     */
    private int containsPage(int j) {
        for (int i = 0; i < FOUR; i++) {
            if (pageFrame[i] == j) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * If the page frame is not full, add the page to an empty spot and insert
     * its index to the head of the arraylist. If full, then remove the page that
     * is stored at the index of the tail in the arraylist.
     * @param page
     */
    private void insertToPageFrame(int page) {
        if (!isPageFrameFilled()) {
            for (int i = 0; i < FOUR; i++) {
                if (pageFrame[i] == -1) {
                    pageFrame[i] = page;
                    recentlyUsed.add(0, i); // Add the index of most recently used page to front
                    break;
                }
            }
        } else {
            int oldIndex = recentlyUsed.remove(3); // The last element is the index of the least recently used page
            pagedOut = pageFrame[oldIndex];
            
            pageFrame[oldIndex] = page;
            recentlyUsed.add(0, oldIndex);
        }
        
        pagedIn = page;
        hitPage = -1;
    }
    
    private boolean isPageFrameFilled() {
        return (recentlyUsed.size() < FOUR) ? false : true;
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
    
    public double hitRatio() {
        return ((double) hits / (double) TOTAL_REF) * 100;
    }
}
