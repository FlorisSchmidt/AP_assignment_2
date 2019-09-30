package nl.vu.labs.phoenix.ap;

public class LinkedList<E extends Comparable<E>> implements ListInterface<E> {

    private class Node {

        E data;
        Node prior, next;

        public Node(E data) {
            this(data, null, null);
        }

        public Node(E data, Node prior, Node next) {
            this.data = data == null ? null : data;
            this.prior = prior;
            this.next = next;
        }
    }

    private Node current;
    private int size;

    public LinkedList() {
        init();
    }

    @Override
    public boolean isEmpty() {
        return current == null;
    }

    @Override
    public ListInterface<E> init() {
        current = null;
        size = 0;
        return this;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ListInterface<E> insert(E d) {
        if (isEmpty()) {
            current = new Node(d);
            size++;
            return this;
        }
        goToFirst();
        if (current.data.compareTo(d) >= 0) insertHead(d);
        else {
            iterate(d);
            if (isTail() || size == 1) insertTail(d);
            else {
                current.next = new Node(d, current, current.next);
                current = current.next;
                current.next.prior = current;

            }
        }


        size++;
        return this;
    }

    // lets current point to node preceding the insertion position
    private void iterate(E d) {
        while(!(current.next == null)) {
            if (current.next.data.compareTo(d) <= 0) {
                goToNext();
            }
            else {
                break;
            }
        }
    }


    private void insertHead(E d) {
        current.prior = new Node(d,null,current);
        current = current.prior;
    }

    private void insertTail(E d) {
        current.next = new Node(d,current, null);
        current = current.next;
    }

    private boolean isHead() {
        return !isEmpty() && current.prior == null;
    }

    private boolean isTail() {
        return !isEmpty() && !isHead() && current.next == null;
    }

    @Override
    public E retrieve() {
        return current.data;
    }

    @Override
    public ListInterface<E> remove() {
        if (size == 1) {
            init();
            return this;
        }
        else if (current.next == null) {
            current = current.prior;
        }
        else {
            Node currentPrior = current.prior;
            current = current.next;
            current.prior = currentPrior;
            }
        size--;
        return this;
    }

    @Override
    public boolean find(E d) {
        goToFirst();
        while(current.next != null) {
           if(current.data.compareTo(d) == 0) return true;
           goToNext();
        }
        return false;
    }

    @Override
    public boolean goToFirst() {
        if (isEmpty()) return false;
        while(goToPrevious()) {
        }
        return true;
    }

    @Override
    public boolean goToLast() {
        if (isEmpty()) return false;
        while(goToNext()) {}
        return true;
    }

    @Override
    public boolean goToNext() {
        if (isEmpty()) return false;
        if (current.next == null) return false;
        current = current.next;
        return true;
    }

    @Override
    public boolean goToPrevious() {
        if (isEmpty()) return false;
        if (current.prior == null) return false;
        current = current.prior;
        return true;
    }

    @Override
    public ListInterface<E> copy() {
        return null;
    }
}