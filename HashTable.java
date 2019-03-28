
// P3a - Hash Table Implementation
// Author: Colin Lindwall Email: clindwall@wisc.edu
// Lecture 004

import java.util.ArrayList;

/**
 * Class that implements a Hash Table ADT. Has methods to insert, remove, and
 * get values from a hash table. Resolves collision with an array list at each
 * index. Also contains getter methods for the private fields.
 *
 * @author colinlindwall
 *
 * @param <K> The key that is inserted into the table
 * @param <V> The value associated with each key
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {

	/**
	 * Inner class for the hash table which is a key/value pair that is the data
	 * type being passed into the table
	 *
	 * @author Colin Lindwall
	 *
	 * @param <Object> The key for the pair
	 * @param <Object> The corresponding value for every key
	 */
	private class KeyValuePair {
		private Object key;
		// key within each node
		private V value;
		// value that accompanies the key in the node

		/**
		 * Constructor that allows a new key value pair to be created
		 *
		 * @param key   value within the node
		 * @param value for the key
		 */
		private KeyValuePair(Object key, V value) {
			this.key = key;
			this.value = value;
		}
	}

	private double loadFactorThreshold;
	// determines whether the table needs to be resized based upon how many nodes
	// are in the table
	private int tableSize;
	// current size of the table
	private int numKeys;
	// number of keys that have been added
	private Object[] hashTable;
	// the table, which is an array of array lists of nodes

	/**
	 * Empty constructor that creates an empty hash table of size 25 with a load
	 * factor threshold of 0.75
	 */
	public HashTable() {
		this.loadFactorThreshold = 0.75;
		this.numKeys = 0;
		this.tableSize = 25;
		this.hashTable = new Object[25];
	}

	/**
	 * Loaded constructor that creates an empty hash table with the user's input
	 * values as the capacity and load factor threshold
	 *
	 * @param initialCapacity     the initial size of the array
	 * @param loadFactorThreshold the threshold where the array is resized
	 */
	public HashTable(int initialCapacity, double loadFactorThreshold) {
		this.loadFactorThreshold = loadFactorThreshold;
		this.numKeys = 0;
		this.tableSize = initialCapacity;
		this.hashTable = new Object[initialCapacity];
	}

	/**
	 * Getter method that returns the load factor threshold that was preset or input
	 * by the user
	 */
	@Override
	public double getLoadFactorThreshold() {
		return loadFactorThreshold;
	}

	/**
	 * Getter method that returns the load factor by dividing the number of keys by
	 * the table size
	 */
	@Override
	public double getLoadFactor() {
		return ((double) numKeys / (double) tableSize);
	}

	/**
	 * This method inserts a KeyValue pair into the hash table. First it checks that
	 * the key is not null. Then it checks that the array list at the index it is
	 * assigned by hashCode does not already contain the key. After that, it inserts
	 * the pair into the correct arraylist. Finally, it checks if the load factor
	 * has exceeded its threshold at which point it doubles the size of the array.
	 */
	@Override
	public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
			// throws this if key is null
		}
		int index = (Math.abs(key.hashCode()) % tableSize);
		// takes the hashcode of the key and mods it by the tableSize to get the index
		// of the key
		boolean contained = false;
		// boolean used to check for duplicate keys
		ArrayList<KeyValuePair> array = (ArrayList<KeyValuePair>) hashTable[index];
		// creates a variable for the array list that should be at the table index
		if (array == null) {
			// this is the case where there is not yet an array list at that index
			hashTable[index] = new ArrayList<KeyValuePair>();
			// the array list is initialized at that index
			ArrayList<KeyValuePair> newArray = (ArrayList<KeyValuePair>) hashTable[index];
			KeyValuePair newPair = new KeyValuePair(key, value);
			// the array list and KeyValuePair are both initialized
			newArray.add(newPair);
			numKeys++;
			// the pair object is added into the new array list and numKeys is incremented
			if (this.getLoadFactor() >= this.getLoadFactorThreshold()) {
				// this code is used when the threshold is met and the tableSize is doubled
				Object[] newTable = new Object[(2 * tableSize) + 1];
				// new table is made with size (2n+1)
				int newTableSize = newTable.length;
				// new size is set so keys can be modded by the new table size
				for (int i = 0; i < tableSize; i++) {
					// now we loop through each arraylist and re-index all the pairs
					if (hashTable[i] == null) {
						continue;
						// if there is not an array list at an index we skip it
					}
					if (hashTable[i] != null) {
						// if there is an array list at an index we have to re-index its pairs
						ArrayList<KeyValuePair> oldArray = (ArrayList<KeyValuePair>) hashTable[i];
						// we name the arraylist at this index so we can traverse its pairs
						for (int j = 0; j < oldArray.size(); j++) {
							int newIndex = (Math.abs(oldArray.get(j).key.hashCode())) % newTableSize;
							// we get the new index for each pair by modding it by the new table size
							if (newTable[newIndex] != null) {
								// if it is re-indexed to an index already initiated w/ an arraylist
								// we simply add it to that array list
								ArrayList<KeyValuePair> newArrayList = (ArrayList<KeyValuePair>) newTable[newIndex];
								newArrayList.add(oldArray.get(j));
							}
							if (newTable[newIndex] == null) {
								// if it is re-indexed to an index w/o an arraylist, we create a new one
								// at that index and insert it there
								newTable[newIndex] = new ArrayList<KeyValuePair>();
								ArrayList<KeyValuePair> newArrayList = (ArrayList<KeyValuePair>) newTable[newIndex];
								newArrayList.add(oldArray.get(j));
							}
						}
					}
				}
				hashTable = newTable;
				tableSize = newTable.length;
				// finally we update our fields to match the expanded table
			}
		} else {
			// this is the case where we are inserting at an index that already has an array
			// list
			for (int i = 0; i < array.size(); i++) {
				if (array.get(i).key.equals(key)) {
					// we traverse the array list and set contained to true if we find the inserted
					// key
					contained = true;
					break;
				}
			}
			if (contained) {
				throw new DuplicateKeyException();
				// this is thrown if we found the key already in the arraylist
			}
			KeyValuePair newPair = new KeyValuePair(key, value);
			array.add(newPair);
			numKeys++;
			// the pair is initialized and added to the arraylist and numKeys is incremented
			if (this.getLoadFactor() >= this.getLoadFactorThreshold()) {
				// this portion of the code is identical to the table expansion code from the
				// upper half of the method
				Object[] newTable = new Object[(2 * tableSize) + 1];
				int newTableSize = newTable.length;
				for (int i = 0; i < tableSize; i++) {
					if (hashTable[i] == null) {
						continue;
					}
					if (hashTable[i] != null) {
						ArrayList<KeyValuePair> oldArray = (ArrayList<KeyValuePair>) hashTable[i];
						for (int j = 0; j < oldArray.size(); j++) {
							int newIndex = (Math.abs(oldArray.get(j).key.hashCode())) % newTableSize;
							if (newTable[newIndex] != null) {
								ArrayList<KeyValuePair> newArrayList = (ArrayList<KeyValuePair>) newTable[newIndex];
								newArrayList.add(oldArray.get(j));
							}
							if (newTable[newIndex] == null) {
								newTable[newIndex] = new ArrayList<KeyValuePair>();
								ArrayList<KeyValuePair> newArrayList = (ArrayList<KeyValuePair>) newTable[newIndex];
								newArrayList.add(oldArray.get(j));
							}
						}
					}
					hashTable = newTable;
					tableSize = newTable.length;
				}
			}
		}
	}

	/**
	 * This method first checks that the key being removed is not null. If not, it
	 * finds the index that key would be at, traverses the arraylist at that index
	 * and removes the key if it is found.
	 */
	@Override
	public boolean remove(K key) throws IllegalNullKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
			// thrown if the key is null
		}
		int index = (Math.abs(key.hashCode())) % tableSize;
		// calculates the index this key would be at in the table
		if (hashTable[index] == null) {
			return false;
			// if there is not array list at the index, false is returned as the key is not
			// found
		}
		ArrayList<KeyValuePair> array = (ArrayList<KeyValuePair>) hashTable[index];
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).key == key) {
				// the arraylist is traversed, and if we find a matching key, we remove it
				array.remove(i);
				numKeys--;
				return true;
			}
		}
		return false;
		// this is returned if we traverse the whole list and find no matching key
	}

	/**
	 * This method returns the value associated with a key in the hash table. First
	 * it checks that the key is not null. It then traverses the arraylist where
	 * that key would be found, and returns the value associated with the key it is
	 * looking for.
	 */
	@Override
	public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
		if (key == null) {
			throw new IllegalNullKeyException();
			// thrown if the key is null
		}
		int index = (Math.abs(key.hashCode()) % tableSize);
		ArrayList<KeyValuePair> array = (ArrayList<KeyValuePair>) hashTable[index];
		if (array == null) {
			throw new KeyNotFoundException();
			// key cannot be found if there is not an arraylist at the keys hash table index
		}
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				// if there is an array list, we traverse it
				if (array.get(i).key == key) {
					KeyValuePair keyValPair = (KeyValuePair) array.get(i);
					return keyValPair.value;
					// when we find the matching key, we return the value associated with it
				}
			}
		}
		throw new KeyNotFoundException();
		// this is thrown if we fully traverse and do not find a matching key
	}

	/**
	 * Getter method that returns the number of keys that have been added to the
	 * hash table
	 */
	@Override
	public int numKeys() {
		return numKeys;
	}

	/**
	 * Getter method that returns the size of the table
	 */
	@Override
	public int getCapacity() {
		return tableSize;
	}

	/**
	 * This method returns 4 indicating that I used an array of array lists for
	 * collision resolution in my hash table
	 */
	@Override
	public int getCollisionResolution() {
		return 4;
	}

}
