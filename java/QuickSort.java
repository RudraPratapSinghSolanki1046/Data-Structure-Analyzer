import java.util.Arrays;

public class QuickSort {

    // Sorts arr in ascending order (in place, not stable).
    public static void quickSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        sort(arr, 0, arr.length - 1);
    }

    private static void sort(int[] arr, int low, int high) {
        if (low >= high) {
            return;
        }
        int p = partition(arr, low, high);
        sort(arr, low, p - 1);
        sort(arr, p + 1, high);
    }

    // Lomuto partition: values smaller than the pivot end up left of it.
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];                  // @pivot
        int i = low; // next slot for a value smaller than the pivot
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {               // @compare
                swap(arr, i, j);                // @swapLeft
                i++;
            }
        }
        swap(arr, i, high);                     // @placePivot
        return i;
    }

    private static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    public static void main(String[] args) {
        int[] data = {8, 3, 6, 2, 9, 1, 5};
        System.out.println("Before: " + Arrays.toString(data));
        quickSort(data);
        System.out.println("After:  " + Arrays.toString(data));
    }
}
