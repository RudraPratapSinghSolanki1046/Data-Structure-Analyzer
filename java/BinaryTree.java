import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// General binary tree (no ordering rule). New values fill the first free position in level order,
// so the tree stays complete; deleting replaces the value with the last node's value.
public class BinaryTree {
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) { this.value = value; }
    }

    private Node root;
    private int size;

    public boolean isEmpty() { return root == null; }

    public int size() { return size; }

    public int height() { return height(root); }

    private int height(Node node) {
        return node == null ? 0 : 1 + Math.max(height(node.left), height(node.right));
    }

    public void insert(int value) {
        Node node = new Node(value);
        size++;
        if (root == null) {
            root = node;                                        // @insert
            return;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);                                        // @insert
        while (!queue.isEmpty()) {                              // @insert
            Node current = queue.remove();                      // @insert
            if (current.left == null) {                         // @insert
                current.left = node;                            // @insert
                return;
            }
            queue.add(current.left);                            // @insert
            if (current.right == null) {                        // @insert
                current.right = node;                           // @insert
                return;
            }
            queue.add(current.right);                           // @insert
        }
    }

    // Searches level by level (a general binary tree has no order to exploit).
    public boolean contains(int value) {
        if (root == null) {
            return false;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);                                        // @search
        while (!queue.isEmpty()) {                              // @search
            Node current = queue.remove();                      // @search
            if (current.value == value) {                       // @search
                return true;                                    // @search
            }
            if (current.left != null) { queue.add(current.left); }      // @search
            if (current.right != null) { queue.add(current.right); }    // @search
        }
        return false;
    }

    // Deletes the first node (in level order) holding value; returns false when it is absent.
    public boolean delete(int value) {
        if (root == null) {
            return false;
        }
        Node target = null;
        Node last = null;
        Node lastParent = null;
        Queue<Node> nodes = new LinkedList<>();
        Queue<Node> parents = new LinkedList<>();
        nodes.add(root);                                        // @delete
        parents.add(null);                                      // @delete
        while (!nodes.isEmpty()) {                              // @delete
            Node current = nodes.remove();                      // @delete
            Node parent = parents.remove();                     // @delete
            if (target == null && current.value == value) {     // @delete
                target = current;                               // @delete
            }
            last = current;                                     // @delete
            lastParent = parent;                                // @delete
            if (current.left != null) {
                nodes.add(current.left);
                parents.add(current);
            }
            if (current.right != null) {
                nodes.add(current.right);
                parents.add(current);
            }
        }
        if (target == null) {
            return false;
        }
        target.value = last.value;                              // @delete
        if (last == root) {
            root = null;                                        // @delete
        } else if (lastParent.right == last) {
            lastParent.right = null;                            // @delete
        } else {
            lastParent.left = null;                             // @delete
        }
        size--;
        return true;
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
        BinaryTree tree = new BinaryTree();
        for (int value : new int[]{1, 2, 3, 4, 5, 6}) {
            tree.insert(value);
        }
        System.out.println("Level order: " + tree.levelOrder());
        System.out.println("Inorder:     " + tree.inorder());
        System.out.println("Preorder:    " + tree.preorder());
        System.out.println("Postorder:   " + tree.postorder());
        tree.delete(2);
        System.out.println("After deleting 2: " + tree.levelOrder() + ", height " + tree.height() + ", contains 6: " + tree.contains(6));
    }
}
