package nonogram.datastructures;

public class MyStack<E> {
    private Node<E> top;
    private int size;
    
    private static class Node<E> {
        E data;
        Node<E> next;
        
        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }
    
    public MyStack() {
        top = null;
        size = 0;
    }
    
    public void push(E element) {
        Node<E> newNode = new Node<>(element);
        newNode.next = top;
        top = newNode;
        size++;
    }
    
    public E pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        
        E data = top.data;
        top = top.next;
        size--;
        return data;
    }
    
    public E peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return top.data;
    }
    
    public boolean isEmpty() {
        return top == null;
    }
    
    public int size() {
        return size;
    }
}
