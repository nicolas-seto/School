
public class Hole {
    int startIndex, endIndex;
    int size;
    public Hole(int startIndex, int endIndex)
    {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        size = (endIndex - startIndex) +1;
    }
    
    public int getEndIndex()    {return endIndex;}
    public int getStartIndex()  {return startIndex;}
    public int getSize()    {return size;}
    
    
    public String toString()    {return "start" + startIndex + "end"+ endIndex + "size"+size;}
}
