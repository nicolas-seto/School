package intermediate;

public class Node 
{
    public Object token;
    public boolean isSubTree;
    public boolean isLastElement;
    public boolean isEmpty;
    public Node left;
    public Node right;

    public Node(boolean bool) {
        token = null;
        left = null;
        right = null;
        isSubTree = bool;
        isLastElement = false;
        isEmpty = false;
    }

    /**
     * Append a node.
     * @param node the node to attach to this
     */
    public void attach(Node node) {
        if (left == null) {
            left = node;
        } else if (right == null) {
            right = node;
        } else if (right != null) {
            right.attach(node);
        }
    }
}
