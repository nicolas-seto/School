
public class Paging {
    
    private static FIFO a;
    private static LRU b;
    private static LFU c;
    private static MFU d;
    private static PageProcess aProcess;
    
    public static void main(String[] args) {
        
        /*for (int i = 1; i <= 5; i++) {
            aProcess = new PageProcess('A');
            a = new FIFO(aProcess);
            System.out.printf("Run %d of FIFO:\n", i);
            a.run();
            System.out.printf("Hit ratio: %.2f%%\n\n", a.hitRatio());
        }
        for (int i = 1; i <= 5; i++) {
            aProcess = new PageProcess('A');
            b = new LRU(aProcess);
            System.out.printf("Run %d of LRU:\n", i);
            b.run();
            System.out.printf("Hit ratio: %.2f%%\n\n", b.hitRatio());
        }
        for (int i = 1; i <= 5; i++) {
            aProcess = new PageProcess('A');
            c = new LFU(aProcess);
            System.out.printf("Run %d of LFU:\n", i);
            c.run();
            System.out.printf("Hit ratio: %.2f%%\n\n", c.hitRatio());
        }*/
        for (int i = 1; i <= 5; i++) {
            aProcess = new PageProcess('A');
            d = new MFU(aProcess);
            System.out.printf("Run %d of MFU:\n", i);
            d.run();
            System.out.printf("Hit ratio: %.2f%%\n\n", d.hitRatio());
        }
    }
}
