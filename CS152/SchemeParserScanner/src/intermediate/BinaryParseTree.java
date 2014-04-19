package intermediate;

public class BinaryParseTree {
    private Node root;
    
    public BinaryParseTree() {
        root = null;
    }
    
    public void insert(Node node) {
        if (root == null) {
            root = node;
        } else {
            root.attach(node);
        }
    }
    
    public Node getRoot() {
        return root;
    }
}
