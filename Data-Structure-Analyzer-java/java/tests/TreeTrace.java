// Test helper: applies operations like "i5 i3 d5" to a BST and an AVL tree and prints preorder and height of each.
public class TreeTrace {
    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();
        AVLTree avl = new AVLTree();
        for (String op : args) {
            int v = Integer.parseInt(op.substring(1));
            if (op.charAt(0) == 'i') { bst.insert(v); avl.insert(v); } else { bst.delete(v); avl.delete(v); }
        }
        System.out.println("bst " + bst.height() + " " + bst.preorder());
        System.out.println("avl " + avl.height() + " " + avl.preorder());
    }
}
