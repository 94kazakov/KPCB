package com.company;

/**
 * HASHING FUNCTION:
 * In this implementation, I used Java's native hashCode() function, which
 * is: s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]                          [2]
 * [2]:http://docs.oracle.com/javase/6/docs/api/java/lang/String.html#hashCode()
 *
 * INTERPRETATION:
 * 1) "Fixed-size hash map" was interpreted as that it holds a fixed number of
 * elements, however internal structure of the map can be modified. Therefore,
 * I will implement doubling the size function and rehashing.
 *
 * COLLISION RESOLUTION:
 * In this implementation, I am using Chained Hashing. At a given key, Chained
 * hashing stores a head reference to a linked list of keys (a "bucket").
 *
 * NOTE:(choice of collision resolution)
 *     There are two main ways to resolve collision conflicts in the hash map:
 *     1) Sampling methods:
 *            If collision occurs, find a bin that is not taken. This approach
 *            suffers immensely when our load factor becomes substantially large.
 *            Example: linear probing performance
 *                ? = occupied_Cells/ARRAY_SIZE = load_Factor
 *                average lookup time is: (1 + 1/(1-?)) / 2                  [1]
 *     2) Chaining method:
 *             If a collision occurs on a given key, we just add the conflicting
 *             value onto the linked list
 *             that exists at a given key
 *             Example: chaining hashing performance
 *                 average lookup time is: 1 + ?/2                           [1]
 *     Therefore, we conclude that we are better off using Chaining method,
 *     because it will maintain decent performance even when the load factor
 *     becomes large.
 *     Reference:
 *     [1]:slide 9, https://www.cs.cmu.edu/~tcortina/15-121sp10/Unit08B.pdf
 *
 * NOTE:(Java's garbage collection)
 *     Since Java automatically collects and deallocates elements that are not
 *     reachable, we don't have to worry about malloc and dealloc as long as we
 *     properly delete elements.
 *
 * NOTE:(importance of evenly distributed hash function)
 *     When hashing function is evenly distributed across the length of the
 *     arrray, runtime performance doesn't suffer much, because we are protected
 *     probabilistically from the "clustering effect" (when we keep storing
 *     values at specific "cluster" of buckets, ignoring the empty buckets).
 *
 * NOTE:(importance of rehashing)
 *     We need to "rehash" existing elements of the hash table
 *     into the bigger sized hash map to keep hash map evenly distributed.
 * @author Denis Kazakov <http://94kazakov.github.io/>
 */
public class HashMap {
    private int ARRAY_SIZE;
    private Node buckets[];
    private int size; //counter for number of elements in the hashmap

    public HashMap(int size){
        this.size = 0;
        if (size >= 0){
            this.ARRAY_SIZE = size;
            this.buckets = new Node[this.ARRAY_SIZE];
        } else{
            throw new IllegalArgumentException("Please set hash map size to be nonnegative" + size);
        }
    }

    /**
     * Insert the key-value pair into the hash map
     * @param key - used to uniquely identify the key-value pair
     * @param value - reference to an object that we want to map
     */
    public boolean set(String key, String value){
        if (value == null || key == null){
            return false;
        }

        //creating input that we will insert into our hash map
        int hash = hash(key);
        Node input = new Node(key,value);

        //insert input if bucket is empty
        if(buckets[hash] == null){
            buckets[hash] = input;
            size ++;
        }else{
            //collision occurred, therefore we append input variable to linked list
            Node runner = buckets[hash];
            while(runner.getNext() != null){
                //if given key already exists in the hash map, then we need to replace it to ensure no keys are repeated
                if(runner.getKey().equals(input.getKey())){
                    runner.setValue(input.getValue());
                    return true;
                }
                runner = runner.getNext();
            }
            //given key is new and is appended onto the end of the bucket's linked list
            runner.setNext(input);
            size ++;
        }
        return true;
    }

    /**
     * Returns the value that is mapped to the given key.
     * @param key - used to search for a value in hash map
     * @return value - reference to an object that we mapped
     */
    public String get(String key){
        int hash = hash(key);
        //get the head of linked list
        Node runner = buckets[hash];
        //traverse linked list
        while(runner != null){
            if(runner.getKey().equals(key)){
                return runner.getValue();
            }
            runner = runner.getNext();
        }
        //if key wasn't set:
        return null;
    }

    /**
     * Deletes the element for a given key and returns it.
     * @param key - used to search for a value in hash map
     * @return value - reference to an object that we mapped, null - if element didn't exist in the hash map
     */
    public String delete(String key){
        int hash = hash(key);
        //get the head of linked list
        Node runner = buckets[hash];
        //if head is the key we are looking for, assign buckets[key] to a new head that is next element
        if(runner.getKey().equals(key)){
            buckets[hash] = runner.getNext();
            size --;
            return runner.getValue();
        }
        //traverse linked list
        while(runner.getNext() != null){
            //if next element is what we are looking for, then we have to reassign the current pointer to the element after next
            Node next = runner.getNext();
            if(next.getKey().equals(key)){
                runner.setNext(next.getNext());
                size --;
                return next.getValue();
            }
            runner = runner.getNext();
        }
        //if key wasn't set:
        return null;
    }

    /**
     * @return float value representing the load factor (`(items in hash map)/(size of hash map)`) of the data structure.
     */
    public float load(){
        return (float) (size * 1.0 / buckets.length); 
    }

    /**
     * @param key - used to uniquely identify the key-value pair
     * @return hash value for a given key
     */
    private int hash(String key){
        return Math.abs(key.hashCode() % ARRAY_SIZE);
    }

    /**
     * Node is a key-value object that we store in our hash map, where value is
     * just a reference to an object. Node object has key, value, pointer to
     * the next element of bucket's linked list.
     */
    private class Node{
        private String key;
        private String value;
        private Node next;

        public Node(){
        }

        public Node(String key, String value){
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }
}