import nonogram.datastructures.MyHashMap;
import nonogram.datastructures.MyArrayList;

public class TestMyHashMap {
    public static void main(String[] args) {
        System.out.println("Testing MyHashMap...");
        
        testBasicOperations();
        testCollisionHandling();
        testResizing();
        testCollectionOperations();
        testEdgeCases();
        
        System.out.println("All MyHashMap tests passed!");
    }
    
    private static void testBasicOperations() {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        
        // Test empty map
        assert map.isEmpty() : "New map should be empty";
        assert map.size() == 0 : "Empty map size should be 0";
        
        // Test put and get
        map.put("one", 1);
        map.put("two", 2);
        assert map.size() == 2 : "Map size should be 2";
        assert !map.isEmpty() : "Map should not be empty";
        
        assert map.get("one").equals(1) : "Value for 'one' should be 1";
        assert map.get("two").equals(2) : "Value for 'two' should be 2";
        assert map.get("three") == null : "Value for non-existent key should be null";
        
        // Test update existing key
        Integer oldValue = map.put("one", 10);
        assert oldValue.equals(1) : "Old value should be 1";
        assert map.get("one").equals(10) : "Updated value should be 10";
        assert map.size() == 2 : "Size should remain 2 after update";
        
        System.out.println("Basic operations test passed");
    }
    
    private static void testCollisionHandling() {
        MyHashMap<Integer, String> map = new MyHashMap<>(4); // Small capacity to force collisions
        
        // Add multiple elements that may collide
        for (int i = 0; i < 10; i++) {
            map.put(i, "value" + i);
        }
        
        // Verify all elements are retrievable
        for (int i = 0; i < 10; i++) {
            assert ("value" + i).equals(map.get(i)) : "Value for key " + i + " should be value" + i;
        }
        
        assert map.size() == 10 : "Map should contain 10 elements";
        
        System.out.println("Collision handling test passed");
    }
    
    private static void testResizing() {
        MyHashMap<String, Integer> map = new MyHashMap<>(2); // Very small initial capacity
        
        // Add many elements to trigger resizing
        for (int i = 0; i < 20; i++) {
            map.put("key" + i, i);
        }
        
        // Verify all elements are still accessible after resizing
        for (int i = 0; i < 20; i++) {
            assert map.get("key" + i).equals(i) : "Value for key" + i + " should be " + i;
        }
        
        assert map.size() == 20 : "Map should contain 20 elements";
        
        System.out.println("Resizing test passed");
    }
    
    private static void testCollectionOperations() {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        
        // Test containsKey
        assert map.containsKey("a") : "Map should contain key 'a'";
        assert !map.containsKey("d") : "Map should not contain key 'd'";
        
        // Test containsValue
        assert map.containsValue(2) : "Map should contain value 2";
        assert !map.containsValue(4) : "Map should not contain value 4";
        
        // Test keySet
        MyArrayList<String> keys = map.keySet();
        assert keys.size() == 3 : "KeySet should have 3 elements";
        assert keys.contains("a") && keys.contains("b") && keys.contains("c") : "KeySet should contain all keys";
        
        // Test values
        MyArrayList<Integer> values = map.values();
        assert values.size() == 3 : "Values should have 3 elements";
        assert values.contains(1) && values.contains(2) && values.contains(3) : "Values should contain all values";
        
        System.out.println("Collection operations test passed");
    }
    
    private static void testEdgeCases() {
        MyHashMap<String, String> map = new MyHashMap<>();
        
        // Test remove
        map.put("test", "value");
        String removed = map.remove("test");
        assert "value".equals(removed) : "Removed value should be 'value'";
        assert map.size() == 0 : "Size should be 0 after removal";
        assert map.get("test") == null : "Removed key should return null";
        
        // Test remove non-existent key
        String notRemoved = map.remove("nonexistent");
        assert notRemoved == null : "Removing non-existent key should return null";
        
        // Test clear
        map.put("a", "1");
        map.put("b", "2");
        map.clear();
        assert map.isEmpty() : "Map should be empty after clear";
        assert map.size() == 0 : "Size should be 0 after clear";
        
        System.out.println("Edge cases test passed");
    }
}
