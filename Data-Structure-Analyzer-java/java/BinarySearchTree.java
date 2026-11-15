import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BinarySearchTree {
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) { this.value = value; }
    }

    private Node root;

    public boolean isEmpty() { return root == null; }

    public int height() { return height(root); }

    private int height(Node node) {
        return node == null ? 0 : 1 + Math.max(height(node.left), height(node.right));
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
        }
        return node;
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

    // Deletes value if present. A node with two children is replaced by its inorder successor.
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
        return node;
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
        BinarySearchTree tree = new BinarySearchTree();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.insert(value);
        }
        System.out.println("Inorder:   " + tree.inorder());
        System.out.println("Preorder:  " + tree.preorder());
        System.out.println("Postorder: " + tree.postorder());
        System.out.println("Level:     " + tree.levelOrder());
        tree.delete(50);
        System.out.println("After deleting 50: " + tree.inorder() + ", contains 60: " + tree.contains(60) + ", height " + tree.height());
    }
}
