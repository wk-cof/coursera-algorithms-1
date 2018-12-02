import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node<Item> {
        public Node(Item val) {
            this.val = val;
            this.next = null;
            this.prev = null;
        }

        public Node<Item> next;
        public Node<Item> prev;
        private Item val;
    }

    private int size;
    private Node<Item> head;
    private Node<Item> tail;


    // construct an empty deque
    public Deque() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> newHead = new Node<Item>(item);
        Node<Item> oldHead = this.head;
        newHead.next = oldHead;
        if (this.size > 0) {
            oldHead.prev = newHead;
        }
        else {
            this.tail = newHead;
        }
        this.head = newHead;
        this.size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.size == 0) {
            this.addFirst(item);
            return;
        }
        Node<Item> newTail = new Node<Item>(item);
        Node<Item> oldTail = this.tail;
        oldTail.next = newTail;
        newTail.prev = oldTail;

        this.tail = newTail;
        this.size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        Node<Item> nextHead = this.head.next;
        Node<Item> currentHead = this.head;
        this.size--;
        if (nextHead != null) {
            this.head = this.head.next;
        }
        else {
            this.head = null;
            this.tail = null;
        }
        return currentHead.val;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        if (size == 1) {
            return removeFirst();
        }

        Node<Item> oldTail = this.tail;
        Node<Item> nextTail = this.tail.prev;
        this.tail = this.tail.prev;
        this.size--;
        return oldTail.val;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new NodeIterator();
    }

    private class NodeIterator implements Iterator<Item> {
        private Node<Item> current;

        public NodeIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            Item previousVal = current.val;
            current = current.next;
            return previousVal;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<Integer> intLinkedList = new Deque<>();
        intLinkedList.addFirst(1);
        intLinkedList.addFirst(2);
        intLinkedList.addFirst(3);
        intLinkedList.addFirst(4);
        intLinkedList.addLast(5);
        intLinkedList.addLast(6);
        intLinkedList.addLast(7);
        intLinkedList.addLast(8);

        // [4 3 2 1 5 6 7 8]

        // System.out.println("Size " + intLinkedList.size());
        // System.out.println(intLinkedList.removeFirst());
        // System.out.println(intLinkedList.removeFirst());
        // System.out.println(intLinkedList.removeFirst());
        // System.out.println(intLinkedList.removeFirst());
        // System.out.println(intLinkedList.removeFirst());
        // System.out.println(intLinkedList.removeFirst());
        // System.out.println(intLinkedList.removeFirst());
        // System.out.println(intLinkedList.removeFirst());

        // System.out.println(intLinkedList.removeLast());
        // System.out.println(intLinkedList.removeLast());
        // System.out.println(intLinkedList.removeLast());
        // System.out.println(intLinkedList.removeLast());
        // System.out.println(intLinkedList.removeLast());
        // System.out.println(intLinkedList.removeLast());
        // System.out.println(intLinkedList.removeLast());
        // System.out.println(intLinkedList.removeLast());

        for (Integer i : intLinkedList) {
            System.out.println(i);
        }
    }
}
