package main.bqs;

import java.util.NoSuchElementException;

public class Queue_Node<Item> {

    private static class Node<Item> {
        private Item item;
        private Queue_Node.Node<Item> next;

        private Node() {
        }
    }

    private Queue_Node.Node<Item> first = null;
    private Queue_Node.Node<Item> last = null;
    private int n = 0;

    public Queue_Node() {
    }

    public boolean isEmpty() {
        return this.first == null;
    }

    public int size() {
        return this.n;
    }

    public Item peek() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        } else {
            return this.first.item;
        }
    }

    public void enqueue(Item item) {
        Queue_Node.Node<Item> oldlast = this.last;
        this.last = new Queue_Node.Node();
        this.last.item = item;
        this.last.next = null;
        if (this.isEmpty()) {
            this.first = this.last;
        } else {
            oldlast.next = this.last;
        }

        ++this.n;
    }

    public Item dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        } else {
            Item item = this.first.item;
            this.first = this.first.next;
            --this.n;
            if (this.isEmpty()) {
                this.last = null;
            }

            return item;
        }
    }



}
