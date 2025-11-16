package nonogram.datastructures;

public class MyQueue<E> {
    private Node<E> front;
    private Node<E> rear;
    private int size;
    
    private static class Node<E> {
        E data;
        Node<E> next;
        
        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }
    
    public MyQueue() {
        front = null;
        rear = null;
        size = 0;
    }
    
    public boolean enqueue(E element) {
        Node<E> newNode = new Node<>(element);
        
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
        return true;
    }
    
    public E dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        
        E data = front.data;
        front = front.next;
        
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }
    
    public E peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        return front.data;
    }
    
    public boolean isEmpty() {
        return front == null;
    }
    
    public int size() {
        return size;
    }
    
    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }
}
