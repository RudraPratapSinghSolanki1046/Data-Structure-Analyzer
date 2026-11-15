import java.util.*;
import java.util.function.Consumer;

// Test harness (not shown in the app): checks every sort and search against java.util.Arrays.
public class Check {
    static int failures = 0;

    static void fail(String msg) { failures++; System.out.println("FAIL: " + msg); }

    public static void main(String[] args) {
        Map<String, Consumer<int[]>> sorts = new LinkedHashMap<>();
        sorts.put("BubbleSort", BubbleSort::bubbleSort);
        sorts.put("SelectionSort", SelectionSort::selectionSort);
        sorts.put("InsertionSort", InsertionSort::insertionSort);
        sorts.put("MergeSort", MergeSort::mergeSort);
        sorts.put("QuickSort", QuickSort::quickSort);
        sorts.put("HeapSort", HeapSort::heapSort);
        Random rnd = new Random(7);
        List<int[]> cases = new ArrayList<>(List.of(new int[]{}, new int[]{5}, new int[]{2, 1}, new int[]{1, 2},
                new int[]{3, 3, 3}, new int[]{5, -2, 9, 3, 3, 0, 7, -8, 1}, new int[]{1, 2, 3, 4, 5}, new int[]{5, 4, 3, 2, 1},
                new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, 0}));
        for (int t = 0; t < 300; t++) {
            int[] a = new int[rnd.nextInt(60)];
            for (int i = 0; i < a.length; i++) a[i] = rnd.nextInt(41) - 20;
            cases.add(a);
        }
        for (Map.Entry<String, Consumer<int[]>> s : sorts.entrySet()) {
            try { s.getValue().accept(null); } catch (Exception e) { fail(s.getKey() + " threw on null"); }
            for (int[] c : cases) {
                int[] mine = c.clone(), ref = c.clone();
                s.getValue().accept(mine);
                Arrays.sort(ref);
                if (!Arrays.equals(mine, ref)) fail(s.getKey() + " " + Arrays.toString(c) + " -> " + Arrays.toString(mine));
            }
        }
        if (LinearSearch.linearSearch(null, 1) != -1 || BinarySearch.binarySearch(null, 1) != -1) fail("null search");
        if (LinearSearch.linearSearch(new int[]{}, 1) != -1 || BinarySearch.binarySearch(new int[]{}, 1) != -1) fail("empty search");
        for (int t = 0; t < 500; t++) {
            int[] a = new int[rnd.nextInt(30)];
            for (int i = 0; i < a.length; i++) a[i] = rnd.nextInt(25) - 10;
            int target = rnd.nextInt(25) - 10;
            int first = -1;
            for (int i = 0; i < a.length; i++) if (a[i] == target) { first = i; break; }
            if (LinearSearch.linearSearch(a, target) != first) fail("linear " + Arrays.toString(a) + " " + target);
            Arrays.sort(a);
            int idx = BinarySearch.binarySearch(a, target);
            boolean present = Arrays.binarySearch(a, target) >= 0;
            if (present ? (idx < 0 || a[idx] != target) : idx != -1) fail("binary " + Arrays.toString(a) + " " + target + " -> " + idx);
        }
        System.out.println(failures == 0 ? "ALL JAVA TESTS PASSED (" + cases.size() + " sort cases x 6 algorithms, 500 searches)" : failures + " FAILURES");
        if (failures > 0) System.exit(1);
    }
}
