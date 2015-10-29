package com.company;

/**
 * HASHING FUNCTION:
 * In this implementation, I just used Java's native hashCode() function, which 
 * is: s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]                          [2]
 * [2]:http://docs.oracle.com/javase/6/docs/api/java/lang/String.html#hashCode() 
 *
 * COLLISION RESOLUTION:
 * In this implementation, I am using Chained Hashing. Chained Hashing maximizes
 * the space efficiency at the cost of sacrificing runtime efficiency. This
 * benefits us, since our hash map has to be "fixed size".
 * At a given key, Chained hashing stores a head reference to a linked list of
 * keys (a "bucket").
 *
 * NOTE:(choice of collision resolution)
 *     There are two main ways to resolve collision conflicts in the hash map:
 *     1) Sampling methods:
 *            If collision occurs, find a bin that is not taken. This approach
 *            suffers immensly when our load factor becomes substantually large.
 *            Example: linear probing performance
 *                ? = occupied_Cells/ARRAY_SIZE = load_Factor
 *                average lookup time is: (1 + 1/(1-?)) / 2                  [1]
 *     2) Chaining method:
 *             If a collision occurs on a given key, we just add the conflicting
 *             value onto the linked list
 *             that exists at a given key
 *             Example: chaining hashing performance
 *                 average lookup time is: 1 + ?/2                           [1]
 *     Therefore, we conclude that because of fixed size constraint, we are
 *     better off using Chaining method, because it will maintain decent
 *     performance even when the load factor becomes large.
 *     Reference:
 *     [1]:slide 9, https://www.cs.cmu.edu/~tcortina/15-121sp10/Unit08B.pdf
 *
 *
 * NOTE:(importance of evenly distributed hash function)
 *     When hashing function is evenly distributed across the length of the
 *     arrray, runtime performance doesn't suffer much, because we are protected
 *     probabilistically from the "clustering effect" (when we keep storing
 *     values at specific "cluster" of buckets, ignoring the empty buckets).
 *
 * NOTE:(importance of rehashing)
 *     Due to fixed size constraint, we can't use the load factor to tell us
 *     when we should increase the size of the hash table. However, if we had
 *     that opportunity, we would "rehash" existing elements of the hash table
 *     into the bigger sized hashmap to keep hash map evenly distributed.
 * @author Denis Kazakov <http://94kazakov.github.io/>
 */
public class HashMap {

    /* The initial size of the bucket array */
    private int ARRAY_SIZE = 256;
    private Node buckets[] = new Node[ARRAY_SIZE];

    /* HashMap initialization */
    public HashMap(){}

    public HashMap(int size){
        if (size >= 0){
            this.ARRAY_SIZE = size;
        } else{
            throw new IllegalArgumentException("Please set hash map size to be nonnegative" + size);
        }
    }

    /**
     * Insert the key-value pair into the hash map
     * @param key - used to uniquely identify the key-value pair
     * @param value - reference to an object that we want to map
     */
    public void set(String key, String value){
        if (key == null){
            throw new IllegalArgumentException("You entered null key" + key);
        }
        if (value == null){
            throw new IllegalArgumentException("You entered null value" + value);
        }

        //creating input that we will insert into our hash map
        int hash = Math.abs(key.hashCode() % ARRAY_SIZE);
        //System.out.println("key:" + key + ", keyHash:" + key.hashCode());
        Node input = new Node(key,value);

        //insert input if bucket is empty
        if(buckets[hash] == null){
            buckets[hash] = input;
        }
        //collision occured, therefore we append input variable to linked list
        else{
            Node current = buckets[hash];
            while(current.next != null){
                /* Check if the key already exists */
                if(current.getKey().equals(input.getKey())){
                    /* Replace the keys value with the new one */
                    current.setValue(input.getValue());
                    return;
                }
                current = current.next;
            }
            /* When the code gets here current.next == null */
            /* Insert the node */
            current.next = input;
        }
    }

    /**
     * Returns the value that is mapped to the given key.
     * @param key - used to search for a value in hash map
     * @return value - reference to an object that we maped
     */
    public String get(String key){
        int hash = Math.abs(key.hashCode() % ARRAY_SIZE);
        //get the head of linked list
        Node n = buckets[hash];
        //traverse linked list
        while(n != null){
            if(n.getKey().equals(key)){
                return n.getValue();
            }
            n = n.getNext();
        }
        //if key wasn't set:
        return null;
    }


    /**
     * Node is a key-value object that we store in our hash map, where value is
     * just a reference to an object. Node object has key, value, pointer to
     * the next element of bucket's linked list.
     */
    class Node{
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