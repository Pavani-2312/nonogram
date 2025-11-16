import nonogram.datastructures.MyQueue;

public class TestMyQueue {
    public static void main(String[] args) {
        System.out.println("Testing MyQueue...");
        
        testBasicOperations();
        testFIFOBehavior();
        testEdgeCases();
        
        System.out.println("All MyQueue tests passed!");
    }
    
    private static void testBasicOperations() {
        MyQueue<String> queue = new MyQueue<>();
        
        // Test empty queue
        assert queue.isEmpty() : "New queue should be empty";
        assert queue.size() == 0 : "Empty queue size should be 0";
        
        // Test enqueue
        queue.enqueue("first");
        queue.enqueue("second");
        assert !queue.isEmpty() : "Queue should not be empty after enqueue";
        assert queue.size() == 2 : "Queue size should be 2";
        
        // Test peek
        assert "first".equals(queue.peek()) : "Peek should return first element";
        assert queue.size() == 2 : "Peek should not change size";
        
        System.out.println("Basic operations test passed");
    }
    
    private static void testFIFOBehavior() {
        MyQueue<Integer> queue = new MyQueue<>();
        
        // Add elements
        for (int i = 1; i <= 5; i++) {
            queue.enqueue(i);
        }
        
        // Remove elements and verify FIFO order
        for (int i = 1; i <= 5; i++) {
            Integer dequeued = queue.dequeue();
            assert dequeued.equals(i) : "Dequeued element should be " + i + " but was " + dequeued;
        }
        
        assert queue.isEmpty() : "Queue should be empty after dequeuing all elements";
        
        System.out.println("FIFO behavior test passed");
    }
    
    private static void testEdgeCases() {
        MyQueue<String> queue = new MyQueue<>();
        
        // Test single element
        queue.enqueue("only");
        assert "only".equals(queue.peek()) : "Peek should return the only element";
        assert "only".equals(queue.dequeue()) : "Dequeue should return the only element";
        assert queue.isEmpty() : "Queue should be empty after dequeuing only element";
        
        // Test clear
        queue.enqueue("a");
        queue.enqueue("b");
        queue.clear();
        assert queue.isEmpty() : "Queue should be empty after clear";
        assert queue.size() == 0 : "Size should be 0 after clear";
        
        // Test exception handling
        try {
            queue.dequeue();
            assert false : "Should throw exception when dequeuing from empty queue";
        } catch (RuntimeException e) {
            // Expected
        }
        
        try {
            queue.peek();
            assert false : "Should throw exception when peeking empty queue";
        } catch (RuntimeException e) {
            // Expected
        }
        
        System.out.println("Edge cases test passed");
    }
}
