package frontend;

import java.util.ArrayList;
import java.util.Arrays;

public class Scanner
{
    private static ArrayList<String> keywords;
    private static ArrayList<String> specialSymbols;  
    private static ArrayList<String> delimiters;
    private ArrayList<Token> tokens;

    private static final String[] KEYWORDS_LIST = {"and", "begin", "begin0", 
        "break-var", "case", "cond", "cycle", "define", "delay", 
        "delay-list-cons", "do", "else", "extend-syntax", "for", "freeze", 
        "if", "lambda", "let", "letrec", "let*", "macro",  
        "object-maker", "or", "quote", "repeat", "safe-lectrec", "set!", 
        "stram-cons", "variable-case", "while", 
        "wrap"};
    private static final String[] SPECIALS_LIST = {"[", "]", "{", "}", ";", ",", 
        ".", "\"", "'", "#", "\\"};
    private static final String[] DELIMITERS_LIST = {")", "("};
    
    /**
     * Instantiate a scanner object that contains lists of special tokens.
     */
    public Scanner() {
        tokens = new ArrayList<Token>();
        keywords = new ArrayList<String>(Arrays.asList(KEYWORDS_LIST));
        specialSymbols = new ArrayList<String>(Arrays.asList(SPECIALS_LIST));
        delimiters = new ArrayList<String>(Arrays.asList(DELIMITERS_LIST));
    }
    
    /**
     * Parses through a line and tokenizes words/symbols.
     * @param line a line of the file
     */
    public ArrayList<Token> tokenizeLine(String line) {
        // Remove tokens from previous iteration
        tokens.clear();
        String aLine = line;
        int j = 0;
        
        // Remove any comments
        if ((j = aLine.indexOf(";")) != -1) {
            aLine = aLine.substring(0,j);
        }
        
        // Add spaces for split
        aLine = aLine.replaceAll("\\(", "( ").replaceAll("\\)", " )").replaceAll("'", "' ");
        
        String[] stringTokens = aLine.split("\\s+");
        if (stringTokens.length != 0) {
            ArrayList<String> parsedString = 
                    new ArrayList<String>(Arrays.asList(stringTokens));
            int length = parsedString.size();
            
            // Remove any empty strings that may have resulted from split
            for(int i = 0; i < length; i++) {
                if(parsedString.get(i).equals("")) {
                    parsedString.remove(i);
                    length--;
                }
            }
            
            String word = "";
            
            for(int i = 0; i < length; i++) {
                word = parsedString.get(i);
                if (isKeyword(word)) {
                    continue;
                } else if (isSpecialSymbol(word)) {
                    continue;
                } else if (isDelimiter(word)) {
                    continue;
                } else if (isNumber(word)) {
                    continue;
                } else {
                    tokens.add(new Token(word, "SYMBOL"));
                }
            }
        }
        
        this.printParsedLine(line.trim(), tokens.size());

        return tokens;
    }
    
    /**
     * Prints the line that is being parsed.
     * @param line the line that is being parsed
     */
    private void printParsedLine(String line, int tokensSize) {
        if (!line.equals("")) {
            System.out.println("LINE\n" + line + "\nTOKENS");
            for(int i = 0; i < tokensSize; i++) {
                Token token = tokens.get(i);
                System.out.printf("%-10s: \"%s\"\n", 
                        token.getType(), token.toString());
            }
            System.out.println("*****************");
        }
    }
    
    /**
     * Checks if the token is a number and adds it to the token list if true.
     * @param token the token
     */
    private boolean isNumber(String token) {  
        if(token.matches("[-+]?\\d*\\.?\\d+")) {
            tokens.add(new Token(token, "NUMBER"));
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the token is a delimiter and adds it to the token list if true.
     * @param token the token
     */
    private boolean isDelimiter(String token) {  
        if(delimiters.contains(token)) {
            tokens.add(new Token(token, "DELIMITER"));
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the token is a keyword and adds it to the token list if true.
     * @param token the token
     */
    private boolean isKeyword(String token) {  
        if (keywords.contains(token)) {
            tokens.add(new Token(token, "KEYWORD"));
            return true;    
        }
        return false;
    }
    
    /**
     * Checks if the token is a special symbol and adds it to the token list if true.
     * @param token the token
     */
    private boolean isSpecialSymbol(String token) { 
        if(specialSymbols.contains(token)) {
            tokens.add(new Token(token, "SP SYMBOL"));
            return true;
        }
        return false;
    }
}
