import java.util.NoSuchElementException;

public class DoublyLinkedList {
    private static class Node {
        int value;
        Node prev;
        Node next;

        Node(int value) { this.value = value; }
    }

    private Node head;
    private Node tail;
    private int size;

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    public void insertHead(int value) {
        Node node = new Node(value);
        node.next = head;                                   // @insertHead
        if (head != null) {
            head.prev = node;                               // @insertHead
        } else {
            tail = node;                                    // @insertHead
        }
        head = node;                                        // @insertHead
        size++;                                             // @insertHead
    }

    public void insertTail(int value) {
        Node node = new Node(value);
        node.prev = tail;                                   // @insertTail
        if (tail != null) {
            tail.next = node;                               // @insertTail
        } else {
            head = node;                                    // @insertTail
        }
        tail = node;                                        // @insertTail
        size++;                                             // @insertTail
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
            Node current = head;
            for (int i = 0; i < index; i++) {               // @insertAt
                current = current.next;                     // @insertAt
            }
            Node node = new Node(value);
            node.prev = current.prev;                       // @insertAt
            node.next = current;                            // @insertAt
            current.prev.next = node;                       // @insertAt
            current.prev = node;                            // @insertAt
            size++;                                         // @insertAt
        }
    }

    public int deleteHead() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");  // @underflow
        }
        int value = head.value;
        head = head.next;                                   // @deleteHead
        if (head != null) {
            head.prev = null;                               // @deleteHead
        } else {
            tail = null;                                    // @deleteHead
        }
        size--;                                             // @deleteHead
        return value;
    }

    public int deleteTail() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");  // @underflow
        }
        int value = tail.value;
        tail = tail.prev;                                   // @deleteTail
        if (tail != null) {
            tail.next = null;                               // @deleteTail
        } else {
            head = null;                                    // @deleteTail
        }
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
        Node current = head;
        for (int i = 0; i < index; i++) {                   // @deleteAt
            current = current.next;                         // @deleteAt
        }
        current.prev.next = current.next;                   // @deleteAt
        current.next.prev = current.prev;                   // @deleteAt
        size--;                                             // @deleteAt
        return current.value;
    }

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

    // Swaps prev and next in every node, then swaps head and tail.
    public void reverse() {
        Node current = head;
        while (current != null) {                           // @reverse
            Node next = current.next;                       // @reverse
            current.next = current.prev;                    // @reverse
            current.prev = next;                            // @reverse
            current = next;                                 // @reverse
        }
        Node temp = head;                                   // @reverse
        head = tail;                                        // @reverse
        tail = temp;                                        // @reverse
    }

    public void traverse() {
        System.out.print("null <-> ");
        for (Node current = head; current != null; current = current.next) {   // @traverse
            System.out.print(current.value + " <-> ");      // @traverse
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
        DoublyLinkedList list = new DoublyLinkedList();
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
