import java.util.*;

// Test harness (not shown in the app): BST and AVL against java.util.TreeSet, plus rotation and traversal checks.
public class Check3 {
    static int failures = 0;
    static void fail(String m) { failures++; if (failures < 15) System.out.println("FAIL: " + m); }

    static BinarySearchTree rebuild(List<Integer> insertionOrder) {
        BinarySearchTree t = new BinarySearchTree();
        for (int v : insertionOrder) t.insert(v);
        return t;
    }

    public static void main(String[] args) {
        Random rnd = new Random(5);
        for (int round = 0; round < 60; round++) {
            BinarySearchTree bst = new BinarySearchTree();
            AVLTree avl = new AVLTree();
            TreeSet<Integer> model = new TreeSet<>();
            for (int step = 0; step < 300; step++) {
                int v = rnd.nextInt(60);
                int op = rnd.nextInt(10);
                if (op < 5) { bst.insert(v); avl.insert(v); model.add(v); }
                else if (op < 8) { bst.delete(v); avl.delete(v); model.remove(v); }
                else {
                    if (bst.contains(v) != model.contains(v) || avl.contains(v) != model.contains(v)) fail("contains " + v);
                }
                List<Integer> expected = new ArrayList<>(model);
                if (!bst.inorder().equals(expected)) fail("BST inorder");
                if (!avl.inorder().equals(expected)) fail("AVL inorder");
                if (!avl.isBalanced()) fail("AVL unbalanced after step " + step);
                if (avl.height() > 1.45 * (Math.log(model.size() + 2) / Math.log(2))) fail("AVL too tall");
                // preorder, level-order and reversed postorder each rebuild the identical tree in a plain BST
                for (BinarySearchTree check : new BinarySearchTree[]{bst}) {
                    List<Integer> pre = check.preorder();
                    if (!rebuild(pre).preorder().equals(pre)) fail("BST preorder rebuild");
                    if (!rebuild(check.levelOrder()).preorder().equals(pre)) fail("BST level-order rebuild");
                    List<Integer> post = new ArrayList<>(check.postorder());
                    Collections.reverse(post);
                    if (!rebuild(post).preorder().equals(pre)) fail("BST postorder rebuild");
                }
                List<Integer> apre = avl.preorder();
                if (!rebuild(apre).preorder().equals(apre)) fail("AVL preorder rebuild");
                if (!rebuild(avl.levelOrder()).preorder().equals(apre)) fail("AVL level-order rebuild");
                if (avl.postorder().size() != model.size() || bst.postorder().size() != model.size()) fail("postorder size");
            }
        }
        // the four rotation cases each produce root 2 with children 1 and 3
        int[][] cases = {{3, 2, 1}, {1, 2, 3}, {3, 1, 2}, {1, 3, 2}};
        for (int[] c : cases) {
            AVLTree t = new AVLTree();
            for (int v : c) t.insert(v);
            if (!t.preorder().equals(List.of(2, 1, 3))) fail("rotation case " + Arrays.toString(c) + " -> " + t.preorder());
        }
        BinarySearchTree empty = new BinarySearchTree();
        if (!empty.inorder().isEmpty() || !empty.levelOrder().isEmpty() || empty.contains(1) || empty.height() != 0) fail("empty BST");
        empty.delete(5);
        AVLTree emptyAvl = new AVLTree();
        emptyAvl.delete(5);
        if (!emptyAvl.isEmpty() || emptyAvl.height() != 0 || !emptyAvl.isBalanced()) fail("empty AVL");
        System.out.println(failures == 0 ? "ALL TREE TESTS PASSED (60 rounds x 300 random operations, 4 rotation cases, empty trees)" : failures + " FAILURES");
        if (failures > 0) System.exit(1);
    }
}
