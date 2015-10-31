package com.company;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Denis Kazakov <http://94kazakov.github.io/>
 */
public class HashMapTest {

    private HashMap hashMap;
    private final int NUM_ELEMENTS = 200000;

    @Before
    public void setUp(){
        //hash map size is deliberately 10x times less than number of elements to test hash map's performance with linked lists
        hashMap = new HashMap(20000);
    }

    @Test
    public void testHashMapSet(){
        hashMap.set("Test1", "Test1Value");
    }

    @Test
    public void testsetGet(){
        String k = "TestsetGet";
        String v = "TestsetGetValue";
        hashMap.set(k, v);
        String valueResult = hashMap.get(k);
        assertEquals(v, valueResult);
    }

    @Test
    public void testHashMapCollisions() {
        /* Initialise hashmap */
        for(int i = 0; i < NUM_ELEMENTS; i++){
            hashMap.set(Integer.toString(i), Integer.toString(i));
        }
        /* Test all values of the get method */
        for(int i = 0; i < NUM_ELEMENTS; i++){
            String value = hashMap.get(Integer.toString(i));
            assertEquals(Integer.toString(i), value);
            System.out.println(value);
        }
    }

    @Test
    public void testDeletion() {
        String k = "TestsetGet";
        String v = "TestsetGetValue";
        hashMap.set(k, v);
        String valueResult = hashMap.delete(k);
        String deleted = hashMap.get(k); //supposed to be null
        assertEquals(v, valueResult);
        assertEquals(null , deleted);
    }

    @After
    public void tearDown(){
        hashMap = null;
    }
}


