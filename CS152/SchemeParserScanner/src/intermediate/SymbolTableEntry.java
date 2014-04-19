package intermediate;

public class SymbolTableEntry {
    private String name;
    
    public SymbolTableEntry(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}