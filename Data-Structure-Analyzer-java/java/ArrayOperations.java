import java.util.Arrays;

// Basic operations on a Java array. Arrays have a fixed length, so insertion and deletion build a new array.
public class ArrayOperations {

    private static void requireArray(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
    }

    // Visits every element in index order and returns them as text, e.g. "10 20 30".
    public static String traverse(int[] arr) {
        requireArray(arr);
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {                  // @traverse
            if (i > 0) {                                        // @traverse
                text.append(' ');                               // @traverse
            }
            text.append(arr[i]);                                // @traverse
        }
        return text.toString();
    }

    // Returns a new array with value inserted at index (0 .. arr.length); later elements shift right.
    public static int[] insertAt(int[] arr, int index, int value) {
        requireArray(arr);
        if (index < 0 || index > arr.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", length: " + arr.length);
        }
        int[] result = new int[arr.length + 1];                 // @insert
        for (int i = 0; i < index; i++) {                       // @insert
            result[i] = arr[i];                                 // @insert
        }
        result[index] = value;                                  // @insert
        for (int i = index; i < arr.length; i++) {              // @insert
            result[i + 1] = arr[i];                             // @insert
        }
        return result;
    }

    // Returns a new array without the element at index; later elements shift left.
    public static int[] deleteAt(int[] arr, int index) {
        requireArray(arr);
        if (index < 0 || index >= arr.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", length: " + arr.length);
        }
        int[] result = new int[arr.length - 1];                 // @delete
        for (int i = 0, j = 0; i < arr.length; i++) {           // @delete
            if (i != index) {                                   // @delete
                result[j++] = arr[i];                           // @delete
            }
        }
        return result;
    }

    // Linear search: index of the first element equal to target, or -1.
    public static int search(int[] arr, int target) {
        requireArray(arr);
        for (int i = 0; i < arr.length; i++) {                  // @search
            if (arr[i] == target) {                             // @search
                return i;                                       // @search
            }
        }
        return -1;
    }

    // Overwrites arr[index] in place and returns the old value.
    public static int update(int[] arr, int index, int value) {
        requireArray(arr);
        if (index < 0 || index >= arr.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", length: " + arr.length);
        }
        int old = arr[index];                                   // @update
        arr[index] = value;                                     // @update
        return old;
    }

    public static void main(String[] args) {
        int[] scores = new int[5];                              // @declare
        int[] primes = {2, 3, 5, 7, 11};                        // @declare
        int[] filled = new int[4];                              // @declare
        Arrays.fill(filled, 9);                                 // @declare
        System.out.println("Default values: " + traverse(scores) + " | filled: " + traverse(filled));
        primes = insertAt(primes, 2, 4);
        System.out.println("After insertAt(2, 4): " + traverse(primes));
        primes = deleteAt(primes, 0);
        System.out.println("After deleteAt(0):    " + traverse(primes) + ", index of 7: " + search(primes, 7));
        System.out.println("Updated index 1, old value " + update(primes, 1, 40) + ": " + traverse(primes));
    }
}
