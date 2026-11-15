import java.util.Arrays;

public class MergeSort {

    // Sorts arr in ascending order. Stable; uses one auxiliary array of size n.
    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int[] temp = new int[arr.length];
        sort(arr, temp, 0, arr.length - 1);
    }

    private static void sort(int[] arr, int[] temp, int left, int right) {
        if (left >= right) {
            return; // zero or one element is already sorted
        }
        int mid = left + (right - left) / 2;    // @divide
        sort(arr, temp, left, mid);
        sort(arr, temp, mid + 1, right);
        merge(arr, temp, left, mid, right);
    }

    // Merges the sorted ranges arr[left..mid] and arr[mid+1..right].
    private static void merge(int[] arr, int[] temp, int left, int mid, int right) {
        int i = left;
        int j = mid + 1;
        int k = left;
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {             // @compare
                temp[k++] = arr[i++];           // @writeLeft
            } else {
                temp[k++] = arr[j++];           // @writeRight
            }
        }
        while (i <= mid) {
            temp[k++] = arr[i++];               // @copyLeft
        }
        while (j <= right) {
            temp[k++] = arr[j++];               // @copyRight
        }
        for (int p = left; p <= right; p++) {   // @merged
            arr[p] = temp[p];
        }
    }

    public static void main(String[] args) {
        int[] data = {8, 3, 6, 2, 9, 1, 5};
        System.out.println("Before: " + Arrays.toString(data));
        mergeSort(data);
        System.out.println("After:  " + Arrays.toString(data));
    }
}
