
/**
 * Filename:   MyProfiler.java
 * Project:    p3b-201901     
 * Authors:    Colin Lindwall, Lecture 004
 *
 * Semester:   Spring 2019
 * Course:     CS400
 * 
 * Due Date:   March 28th, 10pm
 * Version:    1.0
 * 
 * Credits:    N/A
 * 
 * Bugs:       TODO: add any known bugs, or unsolved problems here
 */

import java.util.TreeMap;

public class MyProfiler<K extends Comparable<K>, V> {

	HashTableADT<K, V> hashtable;
	TreeMap<K, V> treemap;

	public MyProfiler() {
		HashTableADT<Integer,Integer> hashtable = new HashTable<Integer,Integer>();
		TreeMap<Integer,Integer> treemap = new TreeMap<Integer,Integer>();
	}

	public void insert(K key, V value) {
		try {
			hashtable.insert(key, value);
		} catch (IllegalNullKeyException e) {
			e.printStackTrace();
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
		}
		treemap.put(key, value);
	}

	public void retrieve(K key) {
		try {
			hashtable.get(key);
		} catch (IllegalNullKeyException e) {
			e.printStackTrace();
		} catch (KeyNotFoundException e) {
			e.printStackTrace();
		}
		treemap.get(key);
	}

	public static void main(String[] args) {
		try {
			int numElements = Integer.parseInt(args[0]);
			MyProfiler<Integer, Integer> profile = new MyProfiler<Integer,Integer>();
			for (int i = 0; i < numElements; i++) {
				profile.insert(i, i);
				profile.retrieve(i);
			}
			String msg = String.format("Inserted and retreived %d (key,value) pairs", numElements);
			System.out.println(msg);
		} catch (Exception e) {
			System.out.println("Usage: java MyProfiler <number_of_elements>");
			System.exit(1);
		}
	}
}
