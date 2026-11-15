import java.util.NoSuchElementException;

public class SinglyLinkedList {
    private static class Node {
        int value;
        Node next;

        Node(int value) { this.value = value; }
    }

    private Node head;
    private int size;

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    public void insertHead(int value) {
        Node node = new Node(value);
        node.next = head;                                   // @insertHead
        head = node;                                        // @insertHead
        size++;                                             // @insertHead
    }

    public void insertTail(int value) {
        Node node = new Node(value);
        if (head == null) {
            head = node;                                    // @insertTail
        } else {
            Node current = head;
            while (current.next != null) {                  // @insertTail
                current = current.next;                     // @insertTail
            }
            current.next = node;                            // @insertTail
        }
        size++;                                             // @insertTail
    }

    // Inserts so that the new node ends up at position index (0 = head, size = tail).
    public void insertAt(int index, int value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
        if (index == 0) {
            insertHead(value);
            return;
        }
        Node previous = head;
        for (int i = 0; i < index - 1; i++) {               // @insertAt
            previous = previous.next;                       // @insertAt
        }
        Node node = new Node(value);
        node.next = previous.next;                          // @insertAt
        previous.next = node;                               // @insertAt
        size++;                                             // @insertAt
    }

    public int deleteHead() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");  // @underflow
        }
        int value = head.value;
        head = head.next;                                   // @deleteHead
        size--;                                             // @deleteHead
        return value;
    }

    public int deleteTail() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");  // @underflow
        }
        if (head.next == null) {
            return deleteHead(); // only one node
        }
        Node previous = head;
        while (previous.next.next != null) {                // @deleteTail
            previous = previous.next;                       // @deleteTail
        }
        int value = previous.next.value;
        previous.next = null;                               // @deleteTail
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
        Node previous = head;
        for (int i = 0; i < index - 1; i++) {               // @deleteAt
            previous = previous.next;                       // @deleteAt
        }
        int value = previous.next.value;
        previous.next = previous.next.next;                 // @deleteAt
        size--;                                             // @deleteAt
        return value;
    }

    // Returns the index of the first node holding value, or -1.
    public int search(int value) {
        int index = 0;
        for (Node current = head; current != null; current = current.next) {   // @search
            if (current.value == value) {                   // @search
                return index;                               // @search
            }
            index++;
        }
        return -1;
    }

    public void reverse() {
        Node previous = null;
        Node current = head;
        while (current != null) {                           // @reverse
            Node next = current.next;                       // @reverse
            current.next = previous;                        // @reverse
            previous = current;                             // @reverse
            current = next;                                 // @reverse
        }
        head = previous;                                    // @reverse
    }

    public void traverse() {
        for (Node current = head; current != null; current = current.next) {   // @traverse
            System.out.print(current.value + " -> ");       // @traverse
        }
        System.out.println("null");
    }

    public int[] toArray() {
        int[] result = new int[size];
        int i = 0;
        for (Node current = head; current != null; current = current.next) {
            result[i++] = current.value;
        }
        return result;
    }

    public static void main(String[] args) {
        SinglyLinkedList list = new SinglyLinkedList();
        list.insertTail(20);
        list.insertTail(30);
        list.insertHead(10);
        list.insertAt(2, 25);
        list.traverse();                       // 10 -> 20 -> 25 -> 30 -> null
        list.reverse();
        list.traverse();                       // 30 -> 25 -> 20 -> 10 -> null
        System.out.println("Index of 20: " + list.search(20) + ", deleted tail: " + list.deleteTail());
    }
}
