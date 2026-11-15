import java.util.Arrays;

// Double-ended queue in a circular array: add and remove at both ends in O(1).
public class CircularDeque {
    private final int[] data;
    private int front; // index of the first element
    private int size;

    public CircularDeque(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        data = new int[capacity];
    }

    public boolean isEmpty() { return size == 0; }

    public boolean isFull() { return size == data.length; }

    public int size() { return size; }

    public void insertFront(int value) {
        if (isFull()) {
            throw new IllegalStateException("Deque overflow");              // @overflow
        }
        front = (front - 1 + data.length) % data.length;                    // @insertFront
        data[front] = value;                                                // @insertFront
        size++;                                                             // @insertFront
    }

    public void insertBack(int value) {
        if (isFull()) {
            throw new IllegalStateException("Deque overflow");              // @overflow
        }
        data[(front + size) % data.length] = value;                         // @insertBack
        size++;                                                             // @insertBack
    }

    public int removeFront() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque underflow");             // @underflow
        }
        int value = data[front];
        front = (front + 1) % data.length;                                  // @removeFront
        size--;                                                             // @removeFront
        return value;
    }

    public int removeBack() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque underflow");             // @underflow
        }
        int last = (front + size - 1) % data.length;                        // @removeBack
        size--;                                                             // @removeBack
        return data[last];
    }

    public int peekFront() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty");
        }
        return data[front];                                                 // @peekFront
    }

    public int peekBack() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty");
        }
        return data[(front + size - 1) % data.length];                      // @peekBack
    }

    public int[] toArray() { // front to back
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = data[(front + i) % data.length];
        }
        return result;
    }

    public static void main(String[] args) {
        CircularDeque deque = new CircularDeque(4);
        deque.insertBack(20);
        deque.insertBack(30);
        deque.insertFront(10);
        System.out.println("Deque: " + Arrays.toString(deque.toArray()) + ", front = " + deque.peekFront() + ", back = " + deque.peekBack());
        System.out.println("Removed back " + deque.removeBack() + " and front " + deque.removeFront() + ": " + Arrays.toString(deque.toArray()));
    }
}
