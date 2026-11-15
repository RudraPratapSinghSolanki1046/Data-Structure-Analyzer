import java.util.Arrays;

public class InsertionSort {

    // Sorts arr in ascending order. arr[0..i-1] is always sorted before step i.
    public static void insertionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {  // @passEnd
            // Move arr[i] left, one swap at a time, until it reaches its place.
            for (int j = i; j > 0; j--) {
                if (arr[j - 1] > arr[j]) {      // @compare
                    int temp = arr[j];          // @swap
                    arr[j] = arr[j - 1];        // @swap
                    arr[j - 1] = temp;          // @swap
                } else {
                    break; // the left neighbour is not larger: stop
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] data = {8, 3, 6, 2, 9, 1, 5};
        System.out.println("Before: " + Arrays.toString(data));
        insertionSort(data);
        System.out.println("After:  " + Arrays.toString(data));
    }
}
