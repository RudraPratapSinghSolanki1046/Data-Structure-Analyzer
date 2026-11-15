import java.util.*;

// Test harness (not shown in the app): random operation sequences checked against java.util models.
public class Check2 {
    static int failures = 0;
    static void fail(String m) { failures++; if (failures < 15) System.out.println("FAIL: " + m); }

    interface L { void ih(int v); void it(int v); void ia(int i, int v); int dh(); int dt(); int da(int i); int s(int v); void r(); int[] arr(); int size(); }

    static boolean sameArr(int[] a, List<Integer> m) {
        if (a.length != m.size()) return false;
        for (int i = 0; i < a.length; i++) if (a[i] != m.get(i)) return false;
        return true;
    }

    static void testList(String name, L l, Random rnd) {
        List<Integer> m = new ArrayList<>();
        for (int step = 0; step < 4000; step++) {
            int op = rnd.nextInt(10), v = rnd.nextInt(20), idx = rnd.nextInt(m.size() + 3) - 1;
            String what = name + " step " + step + " op " + op;
            try {
                switch (op) {
                    case 0: l.ih(v); m.add(0, v); break;
                    case 1: l.it(v); m.add(v); break;
                    case 2: { boolean ok = idx >= 0 && idx <= m.size(); try { l.ia(idx, v); if (!ok) fail(what + " insertAt should throw"); else m.add(idx, v); } catch (IndexOutOfBoundsException e) { if (ok) fail(what + " insertAt threw"); } break; }
                    case 3: { boolean ok = !m.isEmpty(); try { int x = l.dh(); if (!ok || x != m.remove(0)) fail(what + " deleteHead"); } catch (NoSuchElementException e) { if (ok) fail(what + " deleteHead threw"); } break; }
                    case 4: { boolean ok = !m.isEmpty(); try { int x = l.dt(); if (!ok || x != m.remove(m.size() - 1)) fail(what + " deleteTail"); } catch (NoSuchElementException e) { if (ok) fail(what + " deleteTail threw"); } break; }
                    case 5: { boolean ok = idx >= 0 && idx < m.size(); try { int x = l.da(idx); if (!ok || x != m.remove(idx)) fail(what + " deleteAt"); } catch (IndexOutOfBoundsException e) { if (ok) fail(what + " deleteAt threw"); } break; }
                    case 6: if (l.s(v) != m.indexOf(v)) fail(what + " search"); break;
                    case 7: case 8: l.r(); Collections.reverse(m); break;
                    default: break;
                }
            } catch (RuntimeException e) { fail(what + " unexpected " + e); }
            if (!sameArr(l.arr(), m) || l.size() != m.size()) { fail(what + " state " + Arrays.toString(l.arr()) + " vs " + m); return; }
        }
    }

    public static void main(String[] args) {
        Random rnd = new Random(11);
        testList("Singly", new L() { SinglyLinkedList x = new SinglyLinkedList();
            public void ih(int v) { x.insertHead(v); } public void it(int v) { x.insertTail(v); } public void ia(int i, int v) { x.insertAt(i, v); }
            public int dh() { return x.deleteHead(); } public int dt() { return x.deleteTail(); } public int da(int i) { return x.deleteAt(i); }
            public int s(int v) { return x.search(v); } public void r() { x.reverse(); } public int[] arr() { return x.toArray(); } public int size() { return x.size(); } }, rnd);
        testList("Doubly", new L() { DoublyLinkedList x = new DoublyLinkedList();
            public void ih(int v) { x.insertHead(v); } public void it(int v) { x.insertTail(v); } public void ia(int i, int v) { x.insertAt(i, v); }
            public int dh() { return x.deleteHead(); } public int dt() { return x.deleteTail(); } public int da(int i) { return x.deleteAt(i); }
            public int s(int v) { return x.search(v); } public void r() { x.reverse(); } public int[] arr() { return x.toArray(); } public int size() { return x.size(); } }, rnd);
        testList("Circular", new L() { CircularLinkedList x = new CircularLinkedList();
            public void ih(int v) { x.insertHead(v); } public void it(int v) { x.insertTail(v); } public void ia(int i, int v) { x.insertAt(i, v); }
            public int dh() { return x.deleteHead(); } public int dt() { return x.deleteTail(); } public int da(int i) { return x.deleteAt(i); }
            public int s(int v) { return x.search(v); } public void r() { x.reverse(); } public int[] arr() { return x.toArray(); } public int size() { return x.size(); } }, rnd);
        // stacks: push/pop/peek against ArrayDeque; capacity 8 for the array version
        ArrayStack as = new ArrayStack(8); LinkedStack ls = new LinkedStack(); Deque<Integer> ms = new ArrayDeque<>(), ml = new ArrayDeque<>();
        for (int i = 0; i < 5000; i++) {
            int v = rnd.nextInt(100);
            if (rnd.nextBoolean()) {
                try { as.push(v); if (ms.size() >= 8) fail("ArrayStack overflow expected"); else ms.push(v); } catch (IllegalStateException e) { if (ms.size() < 8) fail("ArrayStack overflow early"); }
                ls.push(v); ml.push(v);
            } else {
                try { int x = as.pop(); if (ms.isEmpty() || x != ms.pop()) fail("ArrayStack pop"); } catch (IllegalStateException e) { if (!ms.isEmpty()) fail("ArrayStack underflow early"); }
                try { int x = ls.pop(); if (ml.isEmpty() || x != ml.pop()) fail("LinkedStack pop"); } catch (IllegalStateException e) { if (!ml.isEmpty()) fail("LinkedStack underflow early"); }
            }
            if (!ms.isEmpty() && as.peek() != ms.peek()) fail("ArrayStack peek");
            if (!ml.isEmpty() && ls.peek() != ml.peek()) fail("LinkedStack peek");
            if (as.size() != ms.size() || ls.size() != ml.size()) fail("stack size");
        }
        // queues: three implementations against ArrayDeque, capacity 8 for the array ones
        ArrayQueue aq = new ArrayQueue(8); CircularQueue cq = new CircularQueue(8); LinkedQueue lq = new LinkedQueue();
        Deque<Integer> ma = new ArrayDeque<>(), mc = new ArrayDeque<>(), mq = new ArrayDeque<>();
        for (int i = 0; i < 8000; i++) {
            int v = rnd.nextInt(100);
            if (rnd.nextInt(5) < 3) {
                try { aq.enqueue(v); if (ma.size() >= 8) fail("ArrayQueue overflow expected"); else ma.add(v); } catch (IllegalStateException e) { if (ma.size() < 8) fail("ArrayQueue overflow early"); }
                try { cq.enqueue(v); if (mc.size() >= 8) fail("CircularQueue overflow expected"); else mc.add(v); } catch (IllegalStateException e) { if (mc.size() < 8) fail("CircularQueue overflow early"); }
                lq.enqueue(v); mq.add(v);
            } else {
                try { int x = aq.dequeue(); if (ma.isEmpty() || x != ma.remove()) fail("ArrayQueue dequeue"); } catch (IllegalStateException e) { if (!ma.isEmpty()) fail("ArrayQueue underflow early"); }
                try { int x = cq.dequeue(); if (mc.isEmpty() || x != mc.remove()) fail("CircularQueue dequeue"); } catch (IllegalStateException e) { if (!mc.isEmpty()) fail("CircularQueue underflow early"); }
                try { int x = lq.dequeue(); if (mq.isEmpty() || x != mq.remove()) fail("LinkedQueue dequeue"); } catch (IllegalStateException e) { if (!mq.isEmpty()) fail("LinkedQueue underflow early"); }
            }
            if (!Arrays.equals(aq.toArray(), ma.stream().mapToInt(Integer::intValue).toArray())) fail("ArrayQueue contents");
            if (!Arrays.equals(cq.toArray(), mc.stream().mapToInt(Integer::intValue).toArray())) fail("CircularQueue contents");
            if (!Arrays.equals(lq.toArray(), mq.stream().mapToInt(Integer::intValue).toArray())) fail("LinkedQueue contents");
        }
        try { new ArrayStack(0); fail("capacity 0 should throw"); } catch (IllegalArgumentException e) { /* expected */ }
        System.out.println(failures == 0 ? "ALL DATA STRUCTURE TESTS PASSED (3 list types x 4000 ops, 2 stacks x 5000 ops, 3 queues x 8000 ops)" : failures + " FAILURES");
        if (failures > 0) System.exit(1);
    }
}
