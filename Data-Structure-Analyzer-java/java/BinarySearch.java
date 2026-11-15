public class BinarySearch {

    // arr must be sorted in ascending order. Returns an index of target, or -1 if absent.
    public static int binarySearch(int[] arr, int target) {
        if (arr == null) {
            return -1;
        }
        int low = 0;
        int high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;   // @probe
            if (arr[mid] == target) {           // @probe
                return mid;                     // @found
            } else if (arr[mid] < target) {     // @probe
                low = mid + 1;                  // @goRight
            } else {
                high = mid - 1;                 // @goLeft
            }
        }
        return -1;                              // @notFound
    }

    public static void main(String[] args) {
        int[] data = {4, 7, 9, 12, 16, 21};
        System.out.println("Index of 16: " + binarySearch(data, 16));
        System.out.println("Index of 5:  " + binarySearch(data, 5));
    }
}
