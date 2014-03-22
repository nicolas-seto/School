
public class Proc {

    private char name;
    private int size;
    public static final int PAGE_SIZE = 10;
    public static int DEFAULT_SIZE = 0;
    private StringBuffer buffer;

    public Proc(char name, int size) {
        this.name = name;
        this.size = size;
        buffer = new StringBuffer();
        makeString();
    }

    private void makeString() {
        for (int i = 0; i < size; i++) {
            buffer.append(name);
        }
    }
    
    public String toString()
    {
        return "-"+buffer.toString();
    }

    public int getSize() {
        return size;
    }
    
    public int getPageSize() {
        return PAGE_SIZE;
    }
    
    public void setSize(int size) {
        this.size = size;
        buffer = new StringBuffer();
        makeString();
    }
}
