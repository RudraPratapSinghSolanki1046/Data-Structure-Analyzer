import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// Self-balancing BST: after every insert or delete, subtree heights differ by at most 1.
public class AVLTree {
    private static class Node {
        int value;
        int height = 1;
        Node left;
        Node right;

        Node(int value) { this.value = value; }
    }

    private Node root;

    public boolean isEmpty() { return root == null; }

    public int height() { return height(root); }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    // Positive: left-heavy, negative: right-heavy. Balanced trees stay in [-1, 1].
    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    // Right rotation around y: its left child x becomes the subtree root.
    private Node rotateRight(Node y) {
        Node x = y.left;                                        // @rotR
        Node moved = x.right;                                   // @rotR
        x.right = y;                                            // @rotR
        y.left = moved;                                         // @rotR
        updateHeight(y);                                        // @rotR
        updateHeight(x);                                        // @rotR
        return x;                                               // @rotR
    }

    // Left rotation around x: its right child y becomes the subtree root.
    private Node rotateLeft(Node x) {
        Node y = x.right;                                       // @rotL
        Node moved = y.left;                                    // @rotL
        y.left = x;                                             // @rotL
        x.right = moved;                                        // @rotL
        updateHeight(x);                                        // @rotL
        updateHeight(y);                                        // @rotL
        return y;                                               // @rotL
    }

    // Restores the AVL property at node: LL, LR, RR or RL case.
    private Node rebalance(Node node) {
        updateHeight(node);                                     // @rebalance
        int balance = balanceFactor(node);                      // @rebalance
        if (balance > 1 && balanceFactor(node.left) >= 0) {     // @rotLL
            return rotateRight(node);                           // @rotLL
        }
        if (balance > 1) {                                      // @rotLR
            node.left = rotateLeft(node.left);                  // @rotLR
            return rotateRight(node);                           // @rotLR
        }
        if (balance < -1 && balanceFactor(node.right) <= 0) {   // @rotRR
            return rotateLeft(node);                            // @rotRR
        }
        if (balance < -1) {                                     // @rotRL
            node.right = rotateRight(node.right);               // @rotRL
            return rotateLeft(node);                            // @rotRL
        }
        return node;
    }

    // Inserts value; duplicates are ignored.
    public void insert(int value) {
        root = insert(root, value);
    }

    private Node insert(Node node, int value) {
        if (node == null) {
            return new Node(value);                             // @insert
        }
        if (value < node.value) {                               // @insert
            node.left = insert(node.left, value);               // @insert
        } else if (value > node.value) {                        // @insert
            node.right = insert(node.right, value);             // @insert
        } else {
            return node; // duplicate
        }
        return rebalance(node);                                 // @insert
    }

    public boolean contains(int value) {
        Node current = root;
        while (current != null) {                               // @search
            if (value == current.value) {                       // @search
                return true;                                    // @search
            }
            current = value < current.value ? current.left : current.right;   // @search
        }
        return false;
    }

    // Deletes value if present; a node with two children is replaced by its inorder successor.
    public void delete(int value) {
        root = delete(root, value);
    }

    private Node delete(Node node, int value) {
        if (node == null) {
            return null; // value not found
        }
        if (value < node.value) {                               // @delete
            node.left = delete(node.left, value);               // @delete
        } else if (value > node.value) {                        // @delete
            node.right = delete(node.right, value);             // @delete
        } else {
            if (node.left == null) {                            // @delete
                return node.right;                              // @delete
            }
            if (node.right == null) {                           // @delete
                return node.left;                               // @delete
            }
            Node successor = node.right;                        // @delete
            while (successor.left != null) {                    // @delete
                successor = successor.left;                     // @delete
            }
            node.value = successor.value;                       // @delete
            node.right = delete(node.right, successor.value);   // @delete
        }
        return rebalance(node);                                 // @delete
    }

    // True when every node satisfies |balance factor| <= 1 (used to check the implementation).
    public boolean isBalanced() { return isBalanced(root); }

    private boolean isBalanced(Node node) {
        if (node == null) {
            return true;
        }
        int actual = 1 + Math.max(heightOf(node.left), heightOf(node.right));
        return actual == node.height && Math.abs(balanceFactor(node)) <= 1 && isBalanced(node.left) && isBalanced(node.right);
    }

    private int heightOf(Node node) {
        return node == null ? 0 : 1 + Math.max(heightOf(node.left), heightOf(node.right));
    }

    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(Node node, List<Integer> out) {
        if (node == null) {
            return;
        }
        inorder(node.left, out);                                // @inorder
        out.add(node.value);                                    // @inorder
        inorder(node.right, out);                               // @inorder
    }

    public List<Integer> preorder() {
        List<Integer> result = new ArrayList<>();
        preorder(root, result);
        return result;
    }

    private void preorder(Node node, List<Integer> out) {
        if (node == null) {
            return;
        }
        out.add(node.value);                                    // @preorder
        preorder(node.left, out);                               // @preorder
        preorder(node.right, out);                              // @preorder
    }

    public List<Integer> postorder() {
        List<Integer> result = new ArrayList<>();
        postorder(root, result);
        return result;
    }

    private void postorder(Node node, List<Integer> out) {
        if (node == null) {
            return;
        }
        postorder(node.left, out);                              // @postorder
        postorder(node.right, out);                             // @postorder
        out.add(node.value);                                    // @postorder
    }

    public List<Integer> levelOrder() {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);                                        // @levelOrder
        while (!queue.isEmpty()) {                              // @levelOrder
            Node node = queue.remove();                         // @levelOrder
            result.add(node.value);                             // @levelOrder
            if (node.left != null) { queue.add(node.left); }    // @levelOrder
            if (node.right != null) { queue.add(node.right); }  // @levelOrder
        }
        return result;
    }

    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        for (int value : new int[]{10, 20, 30, 40, 50, 25}) { // 10, 20, 30 triggers an RR rotation
            tree.insert(value);
        }
        System.out.println("Preorder: " + tree.preorder() + ", height " + tree.height() + ", balanced " + tree.isBalanced());
        tree.delete(40);
        System.out.println("After deleting 40: " + tree.inorder() + ", height " + tree.height());
    }
}
