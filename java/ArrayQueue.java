import java.util.Arrays;

// Simple array queue: the front is always index 0, so dequeue shifts the remaining values left (O(n)).
public class ArrayQueue {
    private final int[] data;
    private int size;

    public ArrayQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        data = new int[capacity];
    }

    public boolean isEmpty() { return size == 0; }

    public boolean isFull() { return size == data.length; }

    public int size() { return size; }

    public void enqueue(int value) {
        if (isFull()) {
            throw new IllegalStateException("Queue overflow");      // @overflow
        }
        data[size++] = value;                                        // @enqueue
    }

    public int dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue underflow");     // @underflow
        }
        int value = data[0];
        for (int i = 1; i < size; i++) {                             // @dequeue
            data[i - 1] = data[i];                                   // @dequeue
        }
        size--;                                                      // @dequeue
        return value;
    }

    public int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return data[0];                                              // @peek
    }

    public int[] toArray() { return Arrays.copyOf(data, size); } // front to rear

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue(4);
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        System.out.println("Queue: " + Arrays.toString(queue.toArray()) + ", front = " + queue.peek());
        System.out.println("Dequeued " + queue.dequeue() + ", size = " + queue.size());
    }
}
