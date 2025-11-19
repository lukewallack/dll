import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * description
 * 
 * @author Luke Wallack
 * 
 * @param <T> type to store
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head, tail;
	private int size;
	private int modCount;
	
	/** Creates an empty list */
	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

    /**
     * 
     */
    @Override
    public void addToFront(T element) {
        Node<T> currentNode = head;
        Node<T> newNode = new Node<T>(element);

        if (currentNode != null) {
            newNode.setNext(currentNode);
            currentNode.setPrev(newNode);
        }
        head = newNode;
        if (isEmpty()) {
            tail = newNode;
        }

        size++;
        modCount++;
    }

    /**
     * 
     */
    @Override
    public void addToRear(T element) {
        Node<T> currentNode = tail;
        Node<T> newNode = new Node<T>(element);

        if (currentNode != null) {
            newNode.setPrev(currentNode);
            currentNode.setNext(newNode);
        }
        tail = newNode;
        if (isEmpty()) {
            head = newNode;
        }

        size++;
        modCount++;
    }

    /**
     * 
     */
    @Override
    public void add(T element) {
        addToRear(element);
    }

    /**
     * 
     */
    @Override
    public void addAfter(T element, T target) {
        Node<T> currentNode = head;
        boolean found = false;

        while (!found && currentNode != null) {
            if (currentNode.getElement().equals(target)) {
                found = true;
            } else {
                currentNode = currentNode.getNext();
            }
        }

        if (!found) {
            throw new NoSuchElementException();
        }

        Node<T> newNode = new Node<T>(element);
        // currentNode now equals the target node
        newNode.setPrev(currentNode);
        newNode.setNext(currentNode.getNext());
        currentNode.setNext(newNode);

        if (currentNode == tail) {
            tail = newNode;
        }

        size++;
        modCount++;
    }

    /**
     * 
     */
    @Override
    public void add(int index, T element) {
        // Check if index is valid
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }

        // Special case: index == 0
        if (index == 0) {
            addToFront(element);
            return;
        }
        // Special case: index == size
        if (index == size()) {
            addToRear(element);
            return;
        }
        // Common case: index in middle of list
        Node<T> currentNode = head;
        boolean found = false;
        int i = 0;
        while (!found && currentNode != null) {
            if (i == (index-1)) {
                found = true;
            } else {
                currentNode = currentNode.getNext();
            }
            i++;
        }

        Node<T> newNode = new Node<T>(element);
        newNode.setNext(currentNode.getNext());
        newNode.setPrev(currentNode);
        currentNode.setNext(newNode);

        size++;
        modCount++;
    }

    /**
     * 
     */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T retVal = remove(head.getElement());

        return retVal;
    }

    /**
     * 
     */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T retVal = remove(tail.getElement());

        return retVal;
    }

    /**
     * 
     */
    @Override
    public T remove(T element) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        boolean found = false;
        Node<T> currentNode = head;

        while (!found && currentNode != null) {
            if (element.equals(currentNode.getElement())) {
                found = true;
            } else {
                currentNode = currentNode.getNext();
            }
        }

        if (!found) {
            throw new NoSuchElementException();
        }

        if (size() == 1) {
            head = tail = null;
        } else if (currentNode == head) {
            head = currentNode.getNext();
            head.setPrev(null);
        } else if (currentNode == tail) {
            tail = currentNode.getPrev();
            tail.setNext(null);
        } else {
            (currentNode.getPrev()).setNext(currentNode.getNext());
            (currentNode.getNext()).setPrev(currentNode.getPrev());
        }

        size--;
        modCount++;

        return currentNode.getElement();
    }

    /**
     * 
     */
    @Override
    public T remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        boolean found = false;
        Node<T> currentNode = head;
        int i = 0;
        while (i < size() && !found) {
            if (i == index) {
                found = true;
            } else {
                currentNode = currentNode.getNext();
            }
            i++;
        }
        T retVal = currentNode.getElement();

        if (size() == 1) {
            head = tail = null;
        } else if (currentNode == head) {
            head = currentNode.getNext();
            head.setPrev(null);
        } else if (currentNode == tail) {
            tail = currentNode.getPrev();
            tail.setNext(null);
        } else {
            (currentNode.getPrev()).setNext(currentNode.getNext());
            (currentNode.getNext()).setPrev(currentNode.getPrev());
        }

        size--;
        modCount++;

        return retVal;
    }

    /**
     * 
     */
    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> currentNode = head;
        boolean found = false;

        int i = 0;
        while (i < size() && !found) {
            if (i == index) {
                found = true;
            } else {
                currentNode = currentNode.getNext();
            }
            i++;
        }
        currentNode.setElement(element);
        modCount++;
    }

    /**
     * 
     */
    @Override
    public T get(int index) {
		if (index >= size() || index < 0) {
			throw new IndexOutOfBoundsException();
		}

        Node<T> currentNode = head;
        boolean found = false;
        int i = 0;

        T retVal = null;
        while (i < size() && !found) {
            if (i == index) {
                found = true;
                retVal = currentNode.getElement();
            } else {
                currentNode = currentNode.getNext();
            }
            i++;
        }

        return retVal;
    }

    /**
     * 
     */
    @Override
    public int indexOf(T element) {
        int index = -1;

        Node<T> current = head;
        int i = 0;
        while (current != null && index == -1) {
            if (current.getElement().equals(element)) {
                index = i;
            }
            current = current.getNext();
            i++;
        }

        return index;
    }

    /**
     * 
     */
    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return head.getElement();
    }

    /**
     * 
     */
    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return tail.getElement();
    }

    /**
     * 
     */
    @Override
    public boolean contains(T target) {
        return (indexOf(target) != -1);
    }

    /**
     * 
     */
    @Override
    public boolean isEmpty() {
        return (size() == 0);
    }

    /**
     * 
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * 
     */
    @Override
    public String toString() {
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            return "[]";
        }

        StringBuilder returnStringBuilder = new StringBuilder("[");
        while (it.hasNext()) {
            returnStringBuilder.append(it.next());
            if (it.hasNext()) {
                returnStringBuilder.append(", ");
            }
        }
        returnStringBuilder.append("]");

        return returnStringBuilder.toString();
    }

    /**
     * 
     */
    @Override
    public Iterator<T> iterator() {
        return new DLLIterator();
    }

    /**
     * 
     */
    @Override
    public ListIterator<T> listIterator() {
        return new DLLIterator();
    }

    /**
     * 
     */
    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        return new DLLIterator(startingIndex);
    }

    /** Iterator for double linked list */
    private class DLLIterator implements ListIterator<T> {
        private Node<T> nextNode, prevNode, returnedNode;
        private int nextIndex, prevIndex, returnedIndex;
        private int iterModCount;

		/** Creates a new iterator for the list starting in front of index 0 */
		public DLLIterator() {
			nextNode = head;
            prevNode = returnedNode = null;
            nextIndex = 0;
            prevIndex = returnedIndex = -1; // NoSuchElement
			iterModCount = modCount;
		}
        /** Creates a new iterator for the list starting in front of startingIndex */
        public DLLIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex > size()) {
                throw new IndexOutOfBoundsException();
            }

            returnedNode = null;
            returnedIndex = -1;

            int i = 0;
            nextNode = head;
            boolean found = false;
            while (nextNode != null && !found) {
                if (startingIndex == i) {
                    found = true;
                } else {
                    prevNode = nextNode;
                    nextNode = nextNode.getNext();
                    i++;
                }
            }

            nextIndex = startingIndex;
            prevIndex = startingIndex - 1; // if startingIndex == 0: NoSuchElement
            iterModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            failFastCheck();
            return (nextNode != null);
        }

        @Override
        public boolean hasPrevious() {
            failFastCheck();
            return (prevNode != null);
        }

        @Override
        public T next() {
            failFastCheck();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T next = nextNode.getElement();
            prevNode = returnedNode = nextNode;
            returnedIndex = nextIndex;

            nextNode = nextNode.getNext();
            nextIndex++;
            prevIndex++;

            return next;
        }

        @Override
        public int nextIndex() {
            failFastCheck();
            return nextIndex;
        }

        @Override
        public T previous() {
            failFastCheck();
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            T prev = prevNode.getElement();
            nextNode = returnedNode = prevNode;
            returnedIndex = prevIndex;

            prevNode = prevNode.getPrev();
            prevIndex--;
            nextIndex--;

            return prev;
        }

        @Override
        public int previousIndex() {
            failFastCheck();
            return prevIndex;
        }

        @Override
        public void remove() {
            failFastCheck();
            if (returnedNode == null) {
                throw new IllegalStateException();
            }
            IUDoubleLinkedList.this.remove(returnedNode.getElement());
            returnedNode = null;
            returnedIndex = -1;
            iterModCount = modCount;
        }

        @Override
        public void set(T element) {
            failFastCheck();
            if (returnedNode == null) {
                throw new IllegalStateException();
            }
            IUDoubleLinkedList.this.set(returnedIndex, element);
            iterModCount = modCount;
        }

        @Override
        public void add(T element) {
            failFastCheck();
            IUDoubleLinkedList.this.add(nextIndex, element);
            iterModCount = modCount;
        }

        /**
         * Defines rules for fail-fast behavior
         * @throws ConcurrentModificationException if the iterator's modification count is not in sync with modCount
         */
        public void failFastCheck() {
            if (modCount != iterModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }
}