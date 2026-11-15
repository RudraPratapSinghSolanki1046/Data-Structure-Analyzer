import java.util.Arrays;

public class BubbleSort {

    // Sorts arr in ascending order. A null or very short array is left unchanged.
    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            // After pass i, the i + 1 largest values are in their final place at the right end.
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {      // @compare
                    int temp = arr[j];          // @swap
                    arr[j] = arr[j + 1];        // @swap
                    arr[j + 1] = temp;          // @swap
                    swapped = true;
                }
            }
            if (!swapped) {                     // @passEnd
                break; // no swaps in a full pass: the array is already sorted
            }
        }
    }

    public static void main(String[] args) {
        int[] data = {8, 3, 6, 2, 9, 1, 5};
        System.out.println("Before: " + Arrays.toString(data));
        bubbleSort(data);
        System.out.println("After:  " + Arrays.toString(data));
    }
}
