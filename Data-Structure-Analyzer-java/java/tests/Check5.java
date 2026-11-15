import java.util.*;

// Test harness (not shown in the app): arrays, deque, priority queue and binary tree against java.util models.
public class Check5 {
    static int failures = 0;
    static void fail(String m) { failures++; if (failures < 15) System.out.println("FAIL: " + m); }

    static void arrays(Random rnd) {
        for (int round = 0; round < 200; round++) {
            List<Integer> model = new ArrayList<>();
            int[] arr = new int[0];
            for (int step = 0; step < 60; step++) {
                int op = rnd.nextInt(5), v = rnd.nextInt(20), idx = rnd.nextInt(model.size() + 3) - 1;
                boolean okIns = idx >= 0 && idx <= model.size(), okAt = idx >= 0 && idx < model.size();
                try {
                    if (op == 0) { arr = ArrayOperations.insertAt(arr, idx, v); if (!okIns) fail("insert should throw"); else model.add(idx, v); }
                    else if (op == 1) { arr = ArrayOperations.deleteAt(arr, idx); if (!okAt) fail("delete should throw"); else model.remove(idx); }
                    else if (op == 2) { int old = ArrayOperations.update(arr, idx, v); if (!okAt || old != model.set(idx, v)) fail("update"); }
                    else if (op == 3) { if (ArrayOperations.search(arr, v) != model.indexOf(v)) fail("search"); }
                    else { String expected = model.toString().replaceAll("[\\[\\],]", "").trim(); if (!ArrayOperations.traverse(arr).equals(expected)) fail("traverse"); }
                } catch (IndexOutOfBoundsException e) {
                    if ((op == 0 && okIns) || ((op == 1 || op == 2) && okAt)) fail("unexpected index exception");
                }
                if (arr.length != model.size()) { fail("length"); return; }
                for (int i = 0; i < arr.length; i++) if (arr[i] != model.get(i)) { fail("contents"); return; }
            }
        }
        try { ArrayOperations.traverse(null); fail("null array"); } catch (IllegalArgumentException e) { /* expected */ }
    }

    static void deque(Random rnd) {
        CircularDeque d = new CircularDeque(6);
        Deque<Integer> m = new ArrayDeque<>();
        for (int i = 0; i < 20000; i++) {
            int op = rnd.nextInt(4), v = rnd.nextInt(100);
            try {
                if (op == 0) { d.insertFront(v); if (m.size() >= 6) fail("deque overflow expected"); else m.addFirst(v); }
                else if (op == 1) { d.insertBack(v); if (m.size() >= 6) fail("deque overflow expected"); else m.addLast(v); }
                else if (op == 2) { int x = d.removeFront(); if (m.isEmpty() || x != m.removeFirst()) fail("removeFront"); }
                else { int x = d.removeBack(); if (m.isEmpty() || x != m.removeLast()) fail("removeBack"); }
            } catch (IllegalStateException e) {
                boolean expectedFull = (op < 2) && m.size() >= 6, expectedEmpty = (op >= 2) && m.isEmpty();
                if (!expectedFull && !expectedEmpty) fail("unexpected deque exception op " + op);
            }
            int[] expected = m.stream().mapToInt(Integer::intValue).toArray();
            if (!Arrays.equals(d.toArray(), expected)) { fail("deque contents"); return; }
            if (!m.isEmpty() && (d.peekFront() != m.peekFirst() || d.peekBack() != m.peekLast())) fail("deque peek");
        }
    }

    static boolean heapOk(int[] h) {
        for (int i = 1; i < h.length; i++) if (h[(i - 1) / 2] > h[i]) return false;
        return true;
    }

    static void priority(Random rnd) {
        MinPriorityQueue q = new MinPriorityQueue();
        PriorityQueue<Integer> m = new PriorityQueue<>();
        for (int i = 0; i < 20000; i++) {
            if (rnd.nextInt(5) < 3) { int v = rnd.nextInt(1000) - 500; q.insert(v); m.add(v); }
            else if (m.isEmpty()) { try { q.extractMin(); fail("underflow expected"); } catch (IllegalStateException e) { /* expected */ } }
            else if (q.extractMin() != m.poll()) fail("extractMin");
            if (!heapOk(q.toArray()) || q.size() != m.size()) { fail("heap invariant"); return; }
            if (!m.isEmpty() && q.peek() != m.peek()) fail("peek");
        }
    }

    static void inorder(List<Integer> a, int i, List<Integer> out) { if (i >= a.size()) return; inorder(a, 2 * i + 1, out); out.add(a.get(i)); inorder(a, 2 * i + 2, out); }
    static void preorder(List<Integer> a, int i, List<Integer> out) { if (i >= a.size()) return; out.add(a.get(i)); preorder(a, 2 * i + 1, out); preorder(a, 2 * i + 2, out); }
    static void postorder(List<Integer> a, int i, List<Integer> out) { if (i >= a.size()) return; postorder(a, 2 * i + 1, out); postorder(a, 2 * i + 2, out); out.add(a.get(i)); }

    static void tree(Random rnd) {
        BinaryTree t = new BinaryTree();
        List<Integer> m = new ArrayList<>(); // level-order array of the complete tree
        if (t.contains(1) || t.delete(1) || !t.levelOrder().isEmpty() || t.height() != 0) fail("empty tree");
        for (int i = 0; i < 5000; i++) {
            int v = rnd.nextInt(15);
            if (rnd.nextInt(10) < 6) { t.insert(v); m.add(v); }
            else {
                int idx = m.indexOf(v);
                boolean removed = t.delete(v);
                if (removed != (idx >= 0)) fail("delete result");
                if (idx >= 0) { m.set(idx, m.get(m.size() - 1)); m.remove(m.size() - 1); }
            }
            if (t.contains(v) != m.contains(v)) fail("contains");
            if (!t.levelOrder().equals(m) || t.size() != m.size()) { fail("levelOrder " + t.levelOrder() + " vs " + m); return; }
            List<Integer> in = new ArrayList<>(), pre = new ArrayList<>(), post = new ArrayList<>();
            inorder(m, 0, in); preorder(m, 0, pre); postorder(m, 0, post);
            if (!t.inorder().equals(in) || !t.preorder().equals(pre) || !t.postorder().equals(post)) { fail("traversal"); return; }
            int h = m.isEmpty() ? 0 : 32 - Integer.numberOfLeadingZeros(m.size());
            if (t.height() != h) fail("height");
        }
    }

    public static void main(String[] args) {
        Random rnd = new Random(33);
        arrays(rnd); deque(rnd); priority(rnd); tree(rnd);
        try { new CircularDeque(0); fail("deque capacity"); } catch (IllegalArgumentException e) { /* expected */ }
        System.out.println(failures == 0 ? "ALL REFERENCE STRUCTURE TESTS PASSED (arrays, deque, min-heap, binary tree against java.util models)" : failures + " FAILURES");
        if (failures > 0) System.exit(1);
    }
}
