import java.util.Arrays;

public class LinkedStack {
    private static class Node {
        final int value;
        final Node next;

        Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node head; // the top of the stack
    private int size;

    public boolean isEmpty() { return head == null; }

    public int size() { return size; }

    public void push(int value) {
        head = new Node(value, head);                           // @push
        size++;                                                  // @push
    }

    public int pop() {
        if (head == null) {
            throw new IllegalStateException("Stack underflow"); // @underflow
        }
        int value = head.value;
        head = head.next;                                        // @pop
        size--;                                                  // @pop
        return value;
    }

    public int peek() {
        if (head == null) {
            throw new IllegalStateException("Stack is empty");
        }
        return head.value;                                       // @peek
    }

    public int[] toArray() { // bottom to top
        int[] result = new int[size];
        int i = size - 1;
        for (Node current = head; current != null; current = current.next) {
            result[i--] = current.value;
        }
        return result;
    }

    public static void main(String[] args) {
        LinkedStack stack = new LinkedStack();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println("Stack: " + Arrays.toString(stack.toArray()) + ", top = " + stack.peek());
        System.out.println("Popped " + stack.pop() + ", size = " + stack.size());
    }
}
