import nonogram.datastructures.MyArrayList;

public class TestMyArrayList {
    public static void main(String[] args) {
        System.out.println("Testing MyArrayList...");
        
        testBasicOperations();
        testCapacityGrowth();
        testIndexOperations();
        testRemoveOperations();
        testEdgeCases();
        
        System.out.println("All MyArrayList tests passed!");
    }
    
    private static void testBasicOperations() {
        MyArrayList<String> list = new MyArrayList<>();
        
        // Test empty list
        assert list.size() == 0 : "Empty list size should be 0";
        assert list.isEmpty() : "Empty list should return true for isEmpty()";
        
        // Test add operations
        list.add("first");
        list.add("second");
        assert list.size() == 2 : "Size should be 2 after adding 2 elements";
        assert !list.isEmpty() : "List should not be empty after adding elements";
        
        // Test get operations
        assert "first".equals(list.get(0)) : "First element should be 'first'";
        assert "second".equals(list.get(1)) : "Second element should be 'second'";
        
        System.out.println("Basic operations test passed");
    }
    
    private static void testCapacityGrowth() {
        MyArrayList<Integer> list = new MyArrayList<>(2);
        
        // Add more elements than initial capacity
        for (int i = 0; i < 15; i++) {
            list.add(i);
        }
        
        assert list.size() == 15 : "Size should be 15 after adding 15 elements";
        
        // Verify all elements are correct
        for (int i = 0; i < 15; i++) {
            assert list.get(i).equals(i) : "Element at index " + i + " should be " + i;
        }
        
        System.out.println("Capacity growth test passed");
    }
    
    private static void testIndexOperations() {
        MyArrayList<String> list = new MyArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        
        // Test insert at index
        list.add(1, "inserted");
        assert list.size() == 4 : "Size should be 4 after insertion";
        assert "inserted".equals(list.get(1)) : "Element at index 1 should be 'inserted'";
        assert "b".equals(list.get(2)) : "Element at index 2 should be 'b'";
        
        // Test set operation
        String old = list.set(0, "new_a");
        assert "a".equals(old) : "Old value should be 'a'";
        assert "new_a".equals(list.get(0)) : "New value should be 'new_a'";
        
        System.out.println("Index operations test passed");
    }
    
    private static void testRemoveOperations() {
        MyArrayList<String> list = new MyArrayList<>();
        list.add("x");
        list.add("y");
        list.add("z");
        
        // Test remove by index
        String removed = list.remove(1);
        assert "y".equals(removed) : "Removed element should be 'y'";
        assert list.size() == 2 : "Size should be 2 after removal";
        assert "z".equals(list.get(1)) : "Element at index 1 should now be 'z'";
        
        // Test remove by object
        boolean wasRemoved = list.remove("x");
        assert wasRemoved : "Should return true when removing existing element";
        assert list.size() == 1 : "Size should be 1 after removal";
        
        System.out.println("Remove operations test passed");
    }
    
    private static void testEdgeCases() {
        MyArrayList<String> list = new MyArrayList<>();
        
        // Test contains and indexOf
        list.add("test");
        assert list.contains("test") : "Should contain 'test'";
        assert !list.contains("notfound") : "Should not contain 'notfound'";
        assert list.indexOf("test") == 0 : "Index of 'test' should be 0";
        assert list.indexOf("notfound") == -1 : "Index of 'notfound' should be -1";
        
        // Test clear
        list.clear();
        assert list.size() == 0 : "Size should be 0 after clear";
        assert list.isEmpty() : "List should be empty after clear";
        
        System.out.println("Edge cases test passed");
    }
}
