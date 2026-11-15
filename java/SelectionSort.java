import java.util.Arrays;

public class SelectionSort {

    // Sorts arr in ascending order using at most n - 1 swaps.
    public static void selectionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {       // @passEnd
            int minIdx = i; // index of the smallest value seen in arr[i..n-1]
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {     // @compare
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                int temp = arr[i];              // @swap
                arr[i] = arr[minIdx];           // @swap
                arr[minIdx] = temp;             // @swap
            }
        }
    }

    public static void main(String[] args) {
        int[] data = {8, 3, 6, 2, 9, 1, 5};
        System.out.println("Before: " + Arrays.toString(data));
        selectionSort(data);
        System.out.println("After:  " + Arrays.toString(data));
    }
}
