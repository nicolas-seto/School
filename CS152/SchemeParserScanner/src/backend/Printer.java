package backend;

import intermediate.*;

public class Printer {
    private static final int CONCAT = 2;
    private static final int LINES = 3;
    private StringBuilder indent = new StringBuilder();
    
    /**
     * Prints the parse tree.
     * @param tree the tree to print
     */
    public void printBinaryParseTree(BinaryParseTree tree) {
        this.printHeader("BINARY PARSE TREE");
        System.out.println(this.getTree(tree.getRoot()));
        indent = new StringBuilder();
    }
    
    /**
     * Prints the symbol table.
     * @param symbolTable the symbol table to print
     */
    public void printSymbolTable(SymbolTable symbolTable) {
        this.printHeader("SYMBOL TABLE");
        symbolTable.printSymbolTable();
    }
    
    /**
     * Prints out a title.
     * @param header the title
     */
    public void printHeader(String header) {
        StringBuilder buffer = new StringBuilder();
        int length = header.length();
        for (int i = 0; i < LINES; i++) {
            if (i != 1) {
                for (int j = 0; j < length + CONCAT; j++) {
                    buffer.append('*');
                }
                buffer.append('\n');
            } else {
                buffer.append('*').append(header).append("*\n");
            }
        }
        
        System.out.print(buffer.toString());
    }
    
    /**
     * Use recursion to print out the tree in the correct order.
     * @param node a node to begin from
     * @return a string
     */
    private String getTree(Node node) {
        StringBuilder printString = new StringBuilder();
        
        if (node != null) {
            if (node.isSubTree) { // A sublist
                printString.append("\n").append(indent.toString()).append("("); 
                indent.append("  ");
                
                if (!node.isEmpty) {
                    printString.append(getTree(node.left));
                    printString.append(getTree(node.right));
                } else {
                    printString.append(") ");
                }
                
                if (node.isLastElement) {
                    printString.append(") ");
                }
            } else {
                printString.append(node.token);
                
                if (node.isLastElement) {
                    printString.append(") ");
                }
                
                printString.append(" ");
                printString.append(getTree(node.left));
                printString.append(getTree(node.right));
            }
        }
        
        return printString.toString();
    }
}