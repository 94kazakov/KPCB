package com.company;

public class Main {
    public static void main(String[] args) {
        System.out.println("hello world");
        HashMapTest test = new HashMapTest();
        test.setUp();
        test.testHashMapSet();
        test.testDeletion();
        test.testReplacement();
        test.testHashMapCollisions();

    }
}
