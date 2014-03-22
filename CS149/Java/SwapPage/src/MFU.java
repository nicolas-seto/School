
public class MFU {

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
    
    public MFU(PageProcess aProcess) {
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
    
    private int containsPage(int j) {
        for (int i = 0; i < FOUR; i++) {
            if (pageFrame[i] == j) {
                return i;
            }
        }
        return -1;
    }
    
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
            int oldIndex = this.mostFrequentlyUsed(); // The element with the least hits
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
    
    private int mostFrequentlyUsed() {
        int min = 0;
        int index = 0;
        for (int i = 0; i < FOUR; i++) {
            if (counter[i] > min) {
                min = counter[i];
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
