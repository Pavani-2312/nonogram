package nonogram.datastructures;

public class MyHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    
    private Node<K, V>[] buckets;
    private int size;
    private int capacity;
    
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;
        
        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new Node[capacity];
        this.size = 0;
    }
    
    @SuppressWarnings("unchecked")
    public MyHashMap(int capacity) {
        this.capacity = capacity;
        this.buckets = new Node[capacity];
        this.size = 0;
    }
    
    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
        
        int index = getIndex(key);
        Node<K, V> current = buckets[index];
        
        // Search for existing key
        while (current != null) {
            if (current.key.equals(key)) {
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }
            current = current.next;
        }
        
        // Add new node at beginning of bucket
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;
        return null;
    }
    
    public V get(K key) {
        if (key == null) {
            return null;
        }
        
        int index = getIndex(key);
        Node<K, V> current = buckets[index];
        
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }
    
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        
        int index = getIndex(key);
        Node<K, V> current = buckets[index];
        Node<K, V> prev = null;
        
        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }
    
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    public boolean containsValue(V value) {
        for (Node<K, V> head : buckets) {
            Node<K, V> current = head;
            while (current != null) {
                if ((value == null && current.value == null) || 
                    (value != null && value.equals(current.value))) {
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            buckets[i] = null;
        }
        size = 0;
    }
    
    public MyArrayList<K> keySet() {
        MyArrayList<K> keys = new MyArrayList<>();
        for (Node<K, V> head : buckets) {
            Node<K, V> current = head;
            while (current != null) {
                keys.add(current.key);
                current = current.next;
            }
        }
        return keys;
    }
    
    public MyArrayList<V> values() {
        MyArrayList<V> vals = new MyArrayList<>();
        for (Node<K, V> head : buckets) {
            Node<K, V> current = head;
            while (current != null) {
                vals.add(current.value);
                current = current.next;
            }
        }
        return vals;
    }
    
    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }
    
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        capacity *= 2;
        buckets = new Node[capacity];
        size = 0;
        
        // Rehash all existing entries
        for (Node<K, V> head : oldBuckets) {
            Node<K, V> current = head;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }
}
