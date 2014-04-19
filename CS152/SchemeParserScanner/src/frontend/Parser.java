package frontend;

import backend.*;
import intermediate.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Parser
{
    private String file;
    private List<Token> tokens;
    private int tokenIndex;
    private BinaryParseTree tree;
    private Printer scheme;
    private SymbolTable symTable;

    public Parser(String file) {
        this.file = file;
        tokens = new ArrayList<Token>();
        tokenIndex = 0;
        tree = new BinaryParseTree();
        scheme = new Printer();
        symTable = new SymbolTable();
    }
    
    public void start() {
        scheme.printHeader("BEGINNING FILE SCAN");
        tokenIndex = 0;
        this.getTokens();
        
        while (tokenIndex < tokens.size()) {
            if (isCurrentToken("(")) {
                /* Iteration will begin at the top-level list and work its 
                 * way inward.
                 */
                tree.insert(parseList());
            }
            
            scheme.printSymbolTable(symTable);
            scheme.printBinaryParseTree(tree);
            symTable.clear();
            tree = new BinaryParseTree();
            /* The next token will be the start of the next top-level list if
             * it exists.
             */
            tokenIndex++;
        }
    }

    private void getTokens() {
        try {
            Scanner scan = new Scanner();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            ArrayList<Token> tempTokens = new ArrayList<Token>();
            String line = null;
            
            while ((line = reader.readLine()) != null) {
                tempTokens.clear();
                tempTokens = scan.tokenizeLine(line);
                tokens.addAll(tempTokens);
            }
            reader.close();
        } catch (IOException e) {
            System.err.print(e.getMessage());
            System.exit(-1);
        }
    }

    private Node parseList() {
        Node node = new Node(true);
        this.skipToken("(");
        
        // Empty list will not contain any tokens
        if (this.isCurrentToken(")")) {
            node.isEmpty = true;
        }
        
        while (!isEOF() && !isCurrentToken(")")) {    
            if (this.isCurrentToken("(")) {
                // Begin parsing the sublist
                node.attach(parseList());
            } else { 
                node.attach(parseElement());
            }
            tokenIndex++;
        }
        
        // See if this sublist is the last element of any list
        if (tokenIndex < (tokens.size() - 1) && tokens.get(tokenIndex + 1).toString().equals(")")) { 
            node.isLastElement = true;
        }
        return node;
    }
    
    private Node parseElement() {
        Node node = new Node(false);
        Token token = this.currentToken();
        
        if (!token.getType().equals("NUMBER")) {
            // Add to symbol table if symbol
            if (token.getType().equals("SYMBOL")) {
                symTable.addSymbol(token.toString());
            }
            node.token = token.toString();
        } else {
            node.token = new BigDecimal(token.toString()).toString();
        }
        
        // See if this element is the last element of any list
        if (tokens.get(tokenIndex + 1).toString().equals(")")) {
            node.isLastElement = true;
        }
        
        return node;
    }

    /**
     * Continue iteration if current token is equal to token.
     * @param token the token to compare
     */
    private void skipToken(String token) {
        if (tokens.get(tokenIndex).toString().equals(token)) {
            tokenIndex++;
        }
    }
    
    /**
     * Determines whether the current token is equal to token.
     * @param token the token to compare
     * @return true if equal, false if not
     */
    private boolean isCurrentToken(String token) {
        return tokens.get(tokenIndex).toString().equals(token);
    }
    
    /**
     * Determines whether the end of the file has been reached.
     * @return true if EOF
     */
    private boolean isEOF() {
        return tokenIndex == tokens.size();
    }
    
    private Token currentToken() {
        return tokens.get(tokenIndex);
    }
}
