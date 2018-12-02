import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] array;
    private int size;
    private int maxSize;

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        maxSize = 2;
        array = (Item[]) new Object[maxSize];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize(int capacity) {
        Item[] newArr = (Item[]) new Object[capacity];

        for (int i = 0; i < size; i++) {
            newArr[i] = array[i];
        }
        array = newArr;
        maxSize = newArr.length;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        array[size++] = item;
        if (size >= (maxSize - 1)) {
            resize(maxSize * 2);
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (size < 1) {
            throw new NoSuchElementException();
        }
        final int idx = size > 1 ? StdRandom.uniform(size - 1) : 0;
        final Item val = array[idx];

        // replace the val with the last val
        if (idx != (size - 1)) {
            array[idx] = array[size - 1];
        }

        array[size--] = null;
        if (size > 0 && size <= (maxSize / 4)) {
            resize(maxSize / 2);
        }
        return val;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size < 1) {
            throw new NoSuchElementException();
        }
        final int idx = StdRandom.uniform(size - 1);
        return array[idx];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Item> {
        private Item[] shuffledItems;
        private int currIdx;

        public MyIterator() {
            shuffledItems = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                shuffledItems[i] = array[i];
            }
            StdRandom.shuffle(shuffledItems);
            currIdx = 0;
        }

        @Override
        public boolean hasNext() {
            return currIdx < size;
        }

        @Override
        public Item next() {
            return shuffledItems[currIdx++];
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        rq.enqueue(5);

        System.out.println(rq.dequeue());
        System.out.println(rq.dequeue());
        System.out.println(rq.dequeue());
        // System.out.println(rq.dequeue());
        // System.out.println(rq.dequeue());

        for (Integer i : rq) {
            System.out.println(i);
        }
    }
}
