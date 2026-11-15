import java.util.Arrays;

public class LinkedQueue {
    private static class Node {
        final int value;
        Node next;

        Node(int value) { this.value = value; }
    }

    private Node head; // front of the queue
    private Node tail; // rear of the queue
    private int size;

    public boolean isEmpty() { return head == null; }

    public int size() { return size; }

    public void enqueue(int value) {
        Node node = new Node(value);
        if (tail == null) {
            head = node;                                             // @enqueue
        } else {
            tail.next = node;                                        // @enqueue
        }
        tail = node;                                                 // @enqueue
        size++;                                                      // @enqueue
    }

    public int dequeue() {
        if (head == null) {
            throw new IllegalStateException("Queue underflow");     // @underflow
        }
        int value = head.value;
        head = head.next;                                            // @dequeue
        if (head == null) {
            tail = null; // the queue became empty                   // @dequeue
        }
        size--;                                                      // @dequeue
        return value;
    }

    public int peek() {
        if (head == null) {
            throw new IllegalStateException("Queue is empty");
        }
        return head.value;                                           // @peek
    }

    public int[] toArray() { // front to rear
        int[] result = new int[size];
        int i = 0;
        for (Node current = head; current != null; current = current.next) {
            result[i++] = current.value;
        }
        return result;
    }

    public static void main(String[] args) {
        LinkedQueue queue = new LinkedQueue();
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        System.out.println("Queue: " + Arrays.toString(queue.toArray()) + ", front = " + queue.peek());
        System.out.println("Dequeued " + queue.dequeue() + ", size = " + queue.size());
    }
}
