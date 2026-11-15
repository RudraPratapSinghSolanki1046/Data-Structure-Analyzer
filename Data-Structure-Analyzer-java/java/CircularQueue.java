import java.util.Arrays;

// Circular array queue: indices wrap around with %, so enqueue and dequeue are both O(1).
public class CircularQueue {
    private final int[] data;
    private int front; // index of the first element
    private int size;

    public CircularQueue(int capacity) {
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
        int rear = (front + size) % data.length; // first free slot, wrapping around
        data[rear] = value;                                          // @enqueue
        size++;                                                      // @enqueue
    }

    public int dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue underflow");     // @underflow
        }
        int value = data[front];
        front = (front + 1) % data.length;                           // @dequeue
        size--;                                                      // @dequeue
        return value;
    }

    public int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return data[front];                                          // @peek
    }

    public int[] toArray() { // front to rear
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = data[(front + i) % data.length];
        }
        return result;
    }

    public static void main(String[] args) {
        CircularQueue queue = new CircularQueue(3);
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.dequeue();
        queue.enqueue(40); // reuses the slot freed at index 0
        System.out.println("Queue: " + Arrays.toString(queue.toArray()) + ", front = " + queue.peek());
    }
}
