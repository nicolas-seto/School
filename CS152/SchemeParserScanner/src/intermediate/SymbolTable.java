package intermediate;

import java.util.Map;
import java.util.TreeMap;

public class SymbolTable {
    private TreeMap<String, SymbolTableEntry> table;
    
    public SymbolTable() {
        table = new TreeMap<String, SymbolTableEntry>();
    }
    
    public SymbolTableEntry addSymbol(String theSymbol) {
        if (!table.containsKey(theSymbol)) {
            return table.put(theSymbol, new SymbolTableEntry(theSymbol));
        }
        
        return null;
    }

    public void printSymbolTable() {
        for (Map.Entry<String, SymbolTableEntry> entry : table.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public void clear() {
        table.clear();
    }
    
    public int size() {
        return table.size();
    }
}
