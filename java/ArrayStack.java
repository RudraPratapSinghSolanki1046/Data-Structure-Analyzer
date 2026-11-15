import java.util.Arrays;

public class ArrayStack {
    private final int[] data;
    private int top = -1; // index of the top element, -1 when the stack is empty

    public ArrayStack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        data = new int[capacity];
    }

    public boolean isEmpty() { return top == -1; }

    public boolean isFull() { return top == data.length - 1; }

    public int size() { return top + 1; }

    public void push(int value) {
        if (isFull()) {
            throw new IllegalStateException("Stack overflow");      // @overflow
        }
        data[++top] = value;                                         // @push
    }

    public int pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack underflow");     // @underflow
        }
        return data[top--];                                          // @pop
    }

    public int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return data[top];                                            // @peek
    }

    public int[] toArray() { return Arrays.copyOf(data, size()); } // bottom to top

    public static void main(String[] args) {
        ArrayStack stack = new ArrayStack(3);
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println("Stack: " + Arrays.toString(stack.toArray()) + ", top = " + stack.peek());
        System.out.println("Popped " + stack.pop() + ", size = " + stack.size());
        try {
            stack.push(1);
            stack.push(2); // exceeds the capacity of 3
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
