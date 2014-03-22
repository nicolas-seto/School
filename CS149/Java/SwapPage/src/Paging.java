
public class Paging {
    
    private static FIFO a;
    private static LRU b;
    private static LFU c;
    private static MFU d;
    private static RandomPick e;
    private static PageProcess aProcess;
    private static double total;
    
    private static void runFIFO() {
        total = 0;
        for (int i = 1; i <= 5; i++) {
            aProcess = new PageProcess('A');
            a = new FIFO(aProcess);
            System.out.printf("Run %d of FIFO:\n", i);
            a.run();
            System.out.printf("Hit ratio: %.2f%%\n\n", a.hitRatio());
            total += a.hitRatio();
        }
        System.out.printf("Average hit ratio over 5 runs: %.2f%%\n\n", (total / 500.0) * 100);
    }
    
    private static void runLRU() {
        total = 0;
        for (int i = 1; i <= 5; i++) {
            aProcess = new PageProcess('A');
            b = new LRU(aProcess);
            System.out.printf("Run %d of LRU:\n", i);
            b.run();
            System.out.printf("Hit ratio: %.2f%%\n\n", b.hitRatio());
            total += b.hitRatio();
        }
        System.out.printf("Average hit ratio over 5 runs: %.2f%%\n\n", (total / 500.0) * 100);
    }
    
    private static void runLFU() {
        total = 0;
        for (int i = 1; i <= 5; i++) {
            aProcess = new PageProcess('A');
            c = new LFU(aProcess);
            System.out.printf("Run %d of LFU:\n", i);
            c.run();
            System.out.printf("Hit ratio: %.2f%%\n\n", c.hitRatio());
            total += c.hitRatio();
        }
        System.out.printf("Average hit ratio over 5 runs: %.2f%%\n\n", (total / 500.0) * 100);
    }
    
    private static void runMFU() {
        total = 0;
        for (int i = 1; i <= 5; i++) {
            aProcess = new PageProcess('A');
            d = new MFU(aProcess);
            System.out.printf("Run %d of MFU:\n", i);
            d.run();
            System.out.printf("Hit ratio: %.2f%%\n\n", d.hitRatio());
            total += d.hitRatio();
        }
        System.out.printf("Average hit ratio over 5 runs: %.2f%%\n\n", (total / 500.0) * 100);
    }
    
    private static void runRandomPick() {
        total = 0;
        for (int i = 1; i <= 5; i++) {
            aProcess = new PageProcess('A');
            e = new RandomPick(aProcess);
            System.out.printf("Run %d of Random Pick:\n", i);
            e.run();
            System.out.printf("Hit ratio: %.2f%%\n\n", e.hitRatio());
            total += e.hitRatio();
        }
        System.out.printf("Average hit ratio over 5 runs: %.2f%%\n\n", (total / 500.0) * 100);
    }
    
    public static void main(String[] args) {  
        runFIFO();
        runLRU();
        runLFU();
        runMFU();
        runRandomPick();
    }
}
