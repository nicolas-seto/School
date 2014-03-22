/**
 * Implements Least-frequently used algorithm for paging.
 * @author Home
 *
 */
public class LFU {

    private PageProcess process;
    private static final int FOUR = 4;
    private static final int TOTAL_REF = 100;
    private static StringBuffer buffer;
    private int[] pageFrame;
    private int[] counter;
    private int hits;
    private int hitPage;
    private int pagedIn;
    private int pagedOut;
    private boolean isFilled;
    
    public LFU(PageProcess aProcess) {
        process = aProcess;
        pageFrame = new int[FOUR];
        counter = new int[FOUR];
        this.initializePageFrame();
        hits = 0;
        hitPage = pagedIn = pagedOut = -1;
        isFilled = false;
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
     * If the page frames in physical memory contain the page, it's a hit and
     * the use count is incremented. Otherwise, it's a fault, and we evict the
     * page with the least amount of uses.
     */
    private void runProcess() {
        
        for (int i = 0; i < TOTAL_REF; i++) {
            int page = process.run();
            int index = containsPage(page);
            if (index != -1) {
                hits++;
                pagedIn = pagedOut = -1;
                hitPage = page;
                
                counter[index]++;
            } else {
                this.insertToPageFrame(page);
            }
            
            this.displayPageFrameContents();
        }
    }
    
    /**
     * Returns the index at which the element is contained.
     * @param j the page
     * @return the index number of the page in the pageFrame array
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
     * Evict a page that's been used the least.
     * @param page
     */
    private void insertToPageFrame(int page) {
        if (!isPageFrameFilled()) {
            for (int i = 0; i < FOUR; i++) {
                if (pageFrame[i] == -1) {
                    pageFrame[i] = page;
                    counter[i] = 1; // Initialize page at that index to count 1;
                    
                    if (i == FOUR - 1) {
                        isFilled = true;
                    }
                    
                    break;
                }
            }
        } else {
            int oldIndex = this.leastFrequentlyUsed(); // The element with the least hits
            pagedOut = pageFrame[oldIndex];
            
            pageFrame[oldIndex] = page;
            counter[oldIndex] = 1;
        }
        
        pagedIn = page;
        hitPage = -1;
    }
    
    private boolean isPageFrameFilled() {
        return isFilled;
    }
    
    /**
     * Returns the index of the page with the least hits.
     * @return the index i
     */
    private int leastFrequentlyUsed() {
        int max = TOTAL_REF;
        int index = 0;
        for (int i = 0; i < FOUR; i++) {
            if (counter[i] < max) {
                max = counter[i];
                index = i;
            }
        }
        return index;
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
