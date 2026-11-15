import java.util.Arrays;

// Priority queue backed by a binary min-heap stored in an array: the smallest value is always at index 0.
public class MinPriorityQueue {
    private int[] heap = new int[8];
    private int size;

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    public void insert(int value) {
        if (size == heap.length) {
            heap = Arrays.copyOf(heap, size * 2);       // @grow
        }
        heap[size] = value;                             // @insert
        size++;                                         // @insert
        siftUp(size - 1);                               // @insert
    }

    public int peek() {
        if (size == 0) {
            throw new IllegalStateException("Priority queue is empty");
        }
        return heap[0];                                 // @peek
    }

    // Removes and returns the smallest value.
    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Priority queue underflow");    // @underflow
        }
        int min = heap[0];                              // @extractMin
        size--;                                         // @extractMin
        heap[0] = heap[size];                           // @extractMin
        siftDown(0);                                    // @extractMin
        return min;
    }

    // Moves the value at index i up while it is smaller than its parent.
    private void siftUp(int i) {
        while (i > 0) {                                 // @siftUp
            int parent = (i - 1) / 2;                   // @siftUp
            if (heap[parent] <= heap[i]) {              // @siftUp
                break;
            }
            swap(i, parent);                            // @siftUp
            i = parent;                                 // @siftUp
        }
    }

    // Moves the value at index i down while a child is smaller.
    private void siftDown(int i) {
        while (true) {                                  // @siftDown
            int left = 2 * i + 1;                       // @siftDown
            int right = left + 1;                       // @siftDown
            int smallest = i;                           // @siftDown
            if (left < size && heap[left] < heap[smallest]) {       // @siftDown
                smallest = left;                        // @siftDown
            }
            if (right < size && heap[right] < heap[smallest]) {     // @siftDown
                smallest = right;                       // @siftDown
            }
            if (smallest == i) {                        // @siftDown
                break;
            }
            swap(i, smallest);                          // @siftDown
            i = smallest;                               // @siftDown
        }
    }

    private void swap(int a, int b) {
        int temp = heap[a];
        heap[a] = heap[b];
        heap[b] = temp;
    }

    public int[] toArray() { return Arrays.copyOf(heap, size); } // heap order, not sorted

    public static void main(String[] args) {
        MinPriorityQueue queue = new MinPriorityQueue();
        for (int value : new int[]{50, 20, 40, 10, 30}) {
            queue.insert(value);
        }
        System.out.println("Heap array: " + Arrays.toString(queue.toArray()) + ", min = " + queue.peek());
        StringBuilder order = new StringBuilder();
        while (!queue.isEmpty()) {
            order.append(queue.extractMin()).append(' ');
        }
        System.out.println("Extracted in order: " + order.toString().trim());
    }
}
