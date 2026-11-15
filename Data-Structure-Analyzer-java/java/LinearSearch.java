public class LinearSearch {

    // Returns the index of the first element equal to target, or -1 if it is absent.
    public static int linearSearch(int[] arr, int target) {
        if (arr == null) {
            return -1;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {             // @check
                return i;                       // @found
            }
        }
        return -1;                              // @notFound
    }

    public static void main(String[] args) {
        int[] data = {12, 7, 21, 4, 16, 9};
        System.out.println("Index of 16: " + linearSearch(data, 16));
        System.out.println("Index of 5:  " + linearSearch(data, 5));
    }
}
