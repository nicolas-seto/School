import java.util.Random;

public class RandomPick {

    private PageProcess process;
    private static final int FOUR = 4;
    private static final int TOTAL_REF = 100;
    private static StringBuffer buffer;
    private int[] pageFrame;
    private int hits;
    private int hitPage;
    private int pagedIn;
    private int pagedOut;
    private boolean isFilled;
    private Random rand;
    
    public RandomPick(PageProcess aProcess) {
        process = aProcess;
        pageFrame = new int[FOUR];
        this.initializePageFrame();
        hits = 0;
        hitPage = pagedIn = pagedOut = -1;
        isFilled = false;
        rand = new Random();
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
                    
                    if (i == FOUR - 1) {
                        isFilled = true;
                    }
                    
                    break;
                }
            }
        } else {
            int oldIndex = rand.nextInt(FOUR); // Pick a random page
            pagedOut = pageFrame[oldIndex];
            
            pageFrame[oldIndex] = page;
        }
        
        pagedIn = page;
        hitPage = -1;
    }
    
    private boolean isPageFrameFilled() {
        return isFilled;
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
