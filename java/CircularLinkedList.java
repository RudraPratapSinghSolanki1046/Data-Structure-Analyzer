import java.util.NoSuchElementException;

// Circular singly linked list that stores only tail; the head is tail.next, so both ends are O(1) to reach.
public class CircularLinkedList {
    private static class Node {
        int value;
        Node next;

        Node(int value) { this.value = value; }
    }

    private Node tail;
    private int size;

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    public void insertHead(int value) {
        Node node = new Node(value);
        if (tail == null) {
            node.next = node;                               // @insertHead
            tail = node;                                    // @insertHead
        } else {
            node.next = tail.next;                          // @insertHead
            tail.next = node;                               // @insertHead
        }
        size++;                                             // @insertHead
    }

    public void insertTail(int value) {
        insertHead(value);
        tail = tail.next;                                   // @insertTail
    }

    // Inserts so that the new node ends up at position index (0 = head, size = tail).
    public void insertAt(int index, int value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
        if (index == 0) {
            insertHead(value);
        } else if (index == size) {
            insertTail(value);
        } else {
            Node previous = tail.next;
            for (int i = 0; i < index - 1; i++) {           // @insertAt
                previous = previous.next;                   // @insertAt
            }
            Node node = new Node(value);
            node.next = previous.next;                      // @insertAt
            previous.next = node;                           // @insertAt
            size++;                                         // @insertAt
        }
    }

    public int deleteHead() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");  // @underflow
        }
        Node head = tail.next;
        if (head == tail) {
            tail = null;                                    // @deleteHead
        } else {
            tail.next = head.next;                          // @deleteHead
        }
        size--;                                             // @deleteHead
        return head.value;
    }

    public int deleteTail() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");  // @underflow
        }
        if (size == 1) {
            return deleteHead();
        }
        Node previous = tail.next;
        while (previous.next != tail) {                     // @deleteTail
            previous = previous.next;                       // @deleteTail
        }
        int value = tail.value;
        previous.next = tail.next;                          // @deleteTail
        tail = previous;                                    // @deleteTail
        size--;                                             // @deleteTail
        return value;
    }

    public int deleteAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
        if (index == 0) {
            return deleteHead();
        }
        if (index == size - 1) {
            return deleteTail();
        }
        Node previous = tail.next;
        for (int i = 0; i < index - 1; i++) {               // @deleteAt
            previous = previous.next;                       // @deleteAt
        }
        int value = previous.next.value;
        previous.next = previous.next.next;                 // @deleteAt
        size--;                                             // @deleteAt
        return value;
    }

    public int search(int value) {
        if (tail == null) {
            return -1;
        }
        Node current = tail.next;
        for (int index = 0; index < size; index++) {        // @search
            if (current.value == value) {                   // @search
                return index;                               // @search
            }
            current = current.next;
        }
        return -1;
    }

    public void reverse() {
        if (size < 2) {
            return;
        }
        Node head = tail.next;
        Node previous = tail;
        Node current = head;
        for (int i = 0; i < size; i++) {                    // @reverse
            Node next = current.next;                       // @reverse
            current.next = previous;                        // @reverse
            previous = current;                             // @reverse
            current = next;                                 // @reverse
        }
        tail = head; // the old head is now the tail         // @reverse
    }

    public void traverse() {
        if (tail != null) {
            Node current = tail.next;
            for (int i = 0; i < size; i++) {                // @traverse
                System.out.print(current.value + " -> ");   // @traverse
                current = current.next;
            }
        }
        System.out.println("(back to head)");
    }

    public int[] toArray() {
        int[] result = new int[size];
        Node current = tail == null ? null : tail.next;
        for (int i = 0; i < size; i++) {
            result[i] = current.value;
            current = current.next;
        }
        return result;
    }

    public static void main(String[] args) {
        CircularLinkedList list = new CircularLinkedList();
        list.insertTail(20);
        list.insertTail(30);
        list.insertHead(10);
        list.insertAt(2, 25);
        list.traverse();
        list.reverse();
        list.traverse();
        System.out.println("Index of 20: " + list.search(20) + ", deleted tail: " + list.deleteTail());
    }
}
