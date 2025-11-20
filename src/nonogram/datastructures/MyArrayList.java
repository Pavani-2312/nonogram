package nonogram.datastructures;
public class MyArrayList<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final int GROWTH_FACTOR = 2;
    private Object[] data;
    private int size;
    private int capacity;
    public MyArrayList() {
        this(DEFAULT_CAPACITY);
    }
    public MyArrayList(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.data = new Object[capacity];
        this.size = 0;
    }
    public boolean add(E element) {
        ensureCapacity();
        data[size++] = element;
        return true;
    }
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity();
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (E) data[index];
    }
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E oldValue = (E) data[index];
        data[index] = element;
        return oldValue;
    }
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E oldValue = (E) data[index];
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        data[--size] = null;
        return oldValue;
    }
    public boolean remove(Object obj) {
        for (int i = 0; i < size; i++) {
            if ((obj == null && data[i] == null) || (obj != null && obj.equals(data[i]))) {
                remove(i);
                return true;
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
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }
    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }
    public int indexOf(Object obj) {
        for (int i = 0; i < size; i++) {
            if ((obj == null && data[i] == null) || (obj != null && obj.equals(data[i]))) {
                return i;
            }
        }
        return -1;
    }
    private void ensureCapacity() {
        if (size >= capacity) {
            capacity *= GROWTH_FACTOR;
            Object[] newData = new Object[capacity];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
    }
}
