# Data Structures Implementation Guide

## Overview

This guide provides detailed implementation specifications for the four custom data structures required for the Nonogram game. These structures form the foundation of the entire application and must be implemented without using Java Collections Framework.

## Implementation Priority

1. **MyArrayList** - Implement first (used by other structures)
2. **MyStack** - Implement second (uses MyArrayList internally)
3. **MyQueue** - Implement third (independent implementation)
4. **MyHashMap** - Implement last (most complex)

## MyArrayList<E> Implementation

### Core Functionality
Dynamic array that automatically resizes when capacity is exceeded.

### Essential Fields
- **data**: Generic array to store elements
- **size**: Current number of elements
- **capacity**: Current array capacity
- **DEFAULT_CAPACITY**: Initial capacity constant (recommend 10)
- **GROWTH_FACTOR**: Capacity multiplier for resizing (recommend 2)

### Constructor Requirements
- **Default Constructor**: Initialize with default capacity
- **Capacity Constructor**: Initialize with specified capacity
- **Validation**: Ensure capacity is positive

### Core Methods Implementation

#### Add Operations
- **add(E element)**: Append element to end
  - Check capacity, resize if needed
  - Add element at size index
  - Increment size
  - Return true

- **add(int index, E element)**: Insert at specific position
  - Validate index bounds (0 to size inclusive)
  - Check capacity, resize if needed
  - Shift elements right from index
  - Insert element at index
  - Increment size

#### Access Operations
- **get(int index)**: Retrieve element at index
  - Validate index bounds (0 to size-1)
  - Return element at index

- **set(int index, E element)**: Replace element at index
  - Validate index bounds
  - Store old element for return
  - Replace element at index
  - Return old element

#### Remove Operations
- **remove(int index)**: Remove by position
  - Validate index bounds
  - Store element for return
  - Shift elements left from index+1
  - Decrement size
  - Return removed element

- **remove(E element)**: Remove by value
  - Find element using indexOf
  - If found, call remove(index)
  - Return boolean indicating success

#### Search Operations
- **indexOf(E element)**: Find first occurrence
  - Iterate through array
  - Use equals() for comparison (handle null)
  - Return index or -1 if not found

- **contains(E element)**: Check existence
  - Use indexOf and check if result >= 0

#### Utility Operations
- **size()**: Return current size
- **isEmpty()**: Return size == 0
- **clear()**: Reset size to 0 (don't resize array)

#### Internal Operations
- **ensureCapacity()**: Resize array when needed
  - Create new array with increased capacity
  - Copy existing elements to new array
  - Update data reference and capacity

### Memory Management
- **Initial Capacity**: Start with reasonable default
- **Growth Strategy**: Double capacity when full
- **Shrinking**: Optional - shrink when size < capacity/4

### Error Handling
- **IndexOutOfBoundsException**: For invalid indices
- **IllegalArgumentException**: For invalid capacity
- **NullPointerException**: Handle null elements appropriately

## MyStack<E> Implementation

### Core Functionality
Last-In-First-Out (LIFO) data structure for undo/redo operations.

### Internal Structure Options
**Option 1**: Use MyArrayList internally
**Option 2**: Use custom array with top pointer

### Essential Fields (Array-based approach)
- **data**: Generic array for storage
- **top**: Index of top element (-1 when empty)
- **capacity**: Maximum stack size
- **DEFAULT_CAPACITY**: Initial capacity

### Core Methods Implementation

#### Stack Operations
- **push(E element)**: Add element to top
  - Check capacity, resize if needed
  - Increment top
  - Store element at top position

- **pop()**: Remove and return top element
  - Check if empty, throw exception if so
  - Store top element for return
  - Decrement top
  - Return stored element

- **peek()**: View top element without removal
  - Check if empty, throw exception if so
  - Return element at top position

#### Utility Operations
- **isEmpty()**: Return top == -1
- **size()**: Return top + 1
- **clear()**: Set top to -1

### Error Handling
- **EmptyStackException**: When pop/peek on empty stack
- **StackOverflowError**: If fixed capacity exceeded (optional)

## MyQueue<E> Implementation

### Core Functionality
First-In-First-Out (FIFO) data structure for hint management.

### Implementation Approach
**Circular Array**: Most efficient for fixed-size scenarios
**Dynamic Array**: Better for variable-size requirements

### Essential Fields (Circular Array)
- **data**: Generic array for storage
- **front**: Index of first element
- **rear**: Index of last element
- **size**: Current number of elements
- **capacity**: Maximum queue size

### Core Methods Implementation

#### Queue Operations
- **enqueue(E element)**: Add element to rear
  - Check capacity, resize if needed
  - Add element at rear position
  - Update rear index (circular)
  - Increment size

- **dequeue()**: Remove and return front element
  - Check if empty, throw exception if so
  - Store front element for return
  - Update front index (circular)
  - Decrement size
  - Return stored element

- **peek()**: View front element without removal
  - Check if empty, throw exception if so
  - Return element at front position

#### Utility Operations
- **isEmpty()**: Return size == 0
- **size()**: Return current size
- **clear()**: Reset front, rear, and size

### Circular Index Management
- **Next Index Calculation**: (index + 1) % capacity
- **Capacity Management**: Resize when full
- **Index Wrapping**: Handle circular nature properly

### Error Handling
- **NoSuchElementException**: When dequeue/peek on empty queue
- **IllegalStateException**: For capacity issues

## MyHashMap<K, V> Implementation

### Core Functionality
Key-value storage with constant-time average access.

### Hash Table Structure
- **Separate Chaining**: Use linked lists for collision resolution
- **Load Factor**: Maintain performance with automatic resizing
- **Hash Function**: Use key.hashCode() with proper distribution

### Essential Fields
- **buckets**: Array of linked list heads (or MyArrayList)
- **size**: Current number of key-value pairs
- **capacity**: Number of buckets
- **loadFactor**: Threshold for resizing (recommend 0.75)
- **DEFAULT_CAPACITY**: Initial bucket count (recommend 16)

### Internal Node Structure
Create inner class or separate class for key-value pairs:
- **key**: The key object
- **value**: The associated value
- **next**: Reference to next node (for chaining)

### Core Methods Implementation

#### Primary Operations
- **put(K key, V value)**: Insert or update entry
  - Calculate hash and bucket index
  - Search bucket for existing key
  - If found, update value and return old value
  - If not found, add new node to bucket
  - Increment size and check load factor
  - Resize if necessary

- **get(K key)**: Retrieve value by key
  - Calculate hash and bucket index
  - Search bucket for key
  - Return value if found, null otherwise

- **remove(K key)**: Delete entry by key
  - Calculate hash and bucket index
  - Search and remove from bucket
  - Decrement size if found
  - Return removed value or null

#### Utility Operations
- **containsKey(K key)**: Check key existence
- **containsValue(V value)**: Check value existence (linear search)
- **size()**: Return number of entries
- **isEmpty()**: Return size == 0
- **clear()**: Clear all buckets and reset size

#### Collection Operations
- **keySet()**: Return MyArrayList of all keys
- **values()**: Return MyArrayList of all values

### Hash Function Implementation
- **Hash Calculation**: Use key.hashCode()
- **Index Calculation**: hash % capacity
- **Null Key Handling**: Decide on null key support
- **Hash Distribution**: Ensure even distribution across buckets

### Resizing Strategy
- **Trigger**: When size > capacity * loadFactor
- **New Capacity**: Double current capacity
- **Rehashing**: Recalculate positions for all entries
- **Temporary Storage**: Use old buckets during rehashing

### Collision Resolution
- **Chaining Method**: Linked list in each bucket
- **Search Strategy**: Linear search within bucket
- **Insertion Strategy**: Add to front or back of chain
- **Deletion Strategy**: Maintain chain integrity

### Error Handling
- **NullPointerException**: For null keys (if not supported)
- **IllegalArgumentException**: For invalid parameters
- **ConcurrentModificationException**: Optional for iterator safety

## Testing Strategy

### Unit Testing Approach
Each data structure requires comprehensive testing:

#### MyArrayList Testing
- **Capacity Management**: Test resizing behavior
- **Index Validation**: Test bounds checking
- **Element Operations**: Test add, remove, get, set
- **Edge Cases**: Empty list, single element, full capacity

#### MyStack Testing
- **LIFO Behavior**: Verify last-in-first-out order
- **Empty Stack**: Test exception handling
- **Capacity**: Test growth and limits
- **State Consistency**: Verify size and isEmpty

#### MyQueue Testing
- **FIFO Behavior**: Verify first-in-first-out order
- **Circular Logic**: Test index wrapping
- **Empty Queue**: Test exception handling
- **Capacity Management**: Test resizing

#### MyHashMap Testing
- **Hash Distribution**: Test even bucket usage
- **Collision Handling**: Test chaining behavior
- **Resizing**: Test load factor and rehashing
- **Key-Value Operations**: Test all CRUD operations

### Integration Testing
- **Cross-Structure Usage**: Test structures using each other
- **Performance Testing**: Measure operation times
- **Memory Testing**: Monitor memory usage and leaks
- **Stress Testing**: Large datasets and edge cases

## Performance Considerations

### Time Complexity Goals
- **MyArrayList**: O(1) access, O(1) amortized add, O(n) insert/remove
- **MyStack**: O(1) for all operations
- **MyQueue**: O(1) for all operations
- **MyHashMap**: O(1) average for get/put/remove

### Memory Efficiency
- **Minimize Overhead**: Keep metadata minimal
- **Growth Strategy**: Balance memory usage and performance
- **Garbage Collection**: Avoid unnecessary object creation

### Optimization Techniques
- **Lazy Initialization**: Create arrays only when needed
- **Capacity Planning**: Choose good default sizes
- **Hash Function**: Ensure good distribution
- **Load Factor**: Balance space and time

## Common Implementation Pitfalls

### Array Management
- **Index Bounds**: Always validate array access
- **Null Handling**: Decide on null element support
- **Capacity vs Size**: Distinguish between allocated and used space
- **Generic Arrays**: Handle generic array creation properly

### Hash Table Issues
- **Hash Collisions**: Implement proper collision resolution
- **Load Factor**: Monitor and maintain appropriate load
- **Rehashing**: Ensure all elements are properly relocated
- **Null Keys/Values**: Define and implement null handling policy

### Memory Leaks
- **Reference Cleanup**: Clear references when removing elements
- **Array Shrinking**: Consider shrinking oversized arrays
- **Circular References**: Avoid in linked structures

## Validation Checklist

### Functionality Verification
- [ ] All required methods implemented
- [ ] Proper exception handling
- [ ] Edge cases handled correctly
- [ ] Generic type safety maintained

### Performance Verification
- [ ] Time complexity requirements met
- [ ] Memory usage reasonable
- [ ] No obvious performance bottlenecks
- [ ] Stress testing passed

### Code Quality Verification
- [ ] Clear, readable implementation
- [ ] Proper encapsulation
- [ ] Consistent naming conventions
- [ ] Adequate internal documentation

---

**Next Step**: After implementing and testing data structures, proceed to [Model Implementation](02_Model_Implementation.md) to build the game logic layer.
