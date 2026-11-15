import java.util.Arrays;

public class HeapSort {

    // Sorts arr in ascending order using a max heap. In place, not stable.
    public static void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {  // @build
            heapify(arr, n, i);
        }
        for (int end = n - 1; end > 0; end--) {
            swap(arr, 0, end);                  // @extractSwap
            heapify(arr, end, 0);               // @extractDone
        }
    }

    // Sifts arr[root] down so the subtree rooted at root is a max heap (size = heap length).
    private static void heapify(int[] arr, int size, int root) {
        int largest = root;
        int left = 2 * root + 1;
        int right = 2 * root + 2;
        if (left < size && arr[left] > arr[largest]) {      // @compareLeft
            largest = left;
        }
        if (right < size && arr[right] > arr[largest]) {    // @compareRight
            largest = right;
        }
        if (largest != root) {
            swap(arr, root, largest);                       // @heapSwap
            heapify(arr, size, largest);
        }
    }

    private static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    public static void main(String[] args) {
        int[] data = {8, 3, 6, 2, 9, 1, 5};
        System.out.println("Before: " + Arrays.toString(data));
        heapSort(data);
        System.out.println("After:  " + Arrays.toString(data));
    }
}
