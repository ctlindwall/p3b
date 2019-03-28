
// P3a - Hash Table Implementation
// Author: Colin Lindwall Email: clindwall@wisc.edu
// Lecture 004

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class is made up of various tests that test the functionality of the
 * methods in the HashTable class, and check them using JUnit testing
 * 
 * @author colinlindwall
 *
 */
public class HashTableTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests that a HashTable returns an integer code indicating which collision
	 * resolution strategy is used. REFER TO HashTableADT for valid collision scheme
	 * codes.
	 */
	@Test
	public void test000_collision_scheme() {
		HashTableADT htIntegerKey = new HashTable<Integer, String>();
		int scheme = htIntegerKey.getCollisionResolution();
		if (scheme < 1 || scheme > 9)
			fail("collision resolution must be indicated with 1-9");
	}

	/**
	 * IMPLEMENTED AS EXAMPLE FOR YOU Tests that insert(null,null) throws
	 * IllegalNullKeyException
	 */
	@Test
	public void test001_IllegalNullKey() {
		try {
			HashTableADT htIntegerKey = new HashTable<Integer, String>();
			htIntegerKey.insert(null, null);
			fail("should not be able to insert null key");
		} catch (IllegalNullKeyException e) {
			/* expected */ } catch (Exception e) {
			fail("insert null key should not throw exception " + e.getClass().getName());
		}
	}

	/**
	 * This test checks that the load factor is properly being set to 0.75
	 */
	@Test
	public void test002_LoadFactorThreshold() {
		HashTableADT htIntegerKey = new HashTable<Integer, String>();
		if (htIntegerKey.getLoadFactorThreshold() != 0.75)
			fail("load factor threshold should be set at 0.75");
	}

	/**
	 * This test simply inserts a pair and checks that it throws no exceptions and
	 * increments numKeys to 1
	 */
	@Test
	public void test003_OneInsert() {
		HashTableADT htIntegerKey = new HashTable<Integer, String>();
		try {
			htIntegerKey.insert(1, "one");
			if (htIntegerKey.numKeys() != 1) {
				fail("numKeys should equal one after inserting a key");
			}
		} catch (IllegalNullKeyException e) {
			e.printStackTrace();
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This test inserts two keys that should be indexed to the same array list in
	 * the hash table and makes sure that the collision is handled properly and
	 * numKeys is equal to 2
	 */
	@Test
	public void test004_CollisionFix() {
		HashTableADT htIntegerKey = new HashTable<Integer, String>();
		try {
			htIntegerKey.insert(1, "one");
			htIntegerKey.insert(26, "one");
			if (htIntegerKey.numKeys() != 2) {
				fail("numKeys should equal two after inserting two keys");
			}
		} catch (IllegalNullKeyException e) {
			e.printStackTrace();
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test inserts 5 keys and then makes sure that the Load factor is
	 * calculated properly by dividing numKeys (5) by the table size (25) to get 0.2
	 */
	@Test
	public void test005_LoadFactor() {
		HashTableADT htIntegerKey = new HashTable<Integer, String>();
		try {
			htIntegerKey.insert(1, "one");
			htIntegerKey.insert(2, "two");
			htIntegerKey.insert(3, "three");
			htIntegerKey.insert(4, "four");
			htIntegerKey.insert(5, "five");
			if (htIntegerKey.getLoadFactor() != 0.2) {
				fail("Load factor after adding 5 keys should equal 0.2");
			}
		} catch (IllegalNullKeyException e) {
			e.printStackTrace();
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test inserts a pair and then removes it, and makes sure the numKeys is
	 * back at 0, and that no exceptions are thrown
	 */
	@Test
	public void test006_InsertAndRemove() {
		HashTableADT htIntegerKey = new HashTable<Integer, String>();
		try {
			htIntegerKey.insert(1, "one");
			htIntegerKey.remove(1);
			if (htIntegerKey.numKeys() != 0) {
				fail("numKeys should equal zero after inserting and removing a pair");
			}
		} catch (IllegalNullKeyException e) {
			e.printStackTrace();
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This test tries to remove a key that does not exist, and makes sure that the
	 * method returns false because that key is not in the hash table
	 */
	@Test
	public void test007_RemoveNonExisting() {
		HashTableADT htIntegerKey = new HashTable<Integer, String>();
		try {
			boolean removal = htIntegerKey.remove(1);
			if (removal) {
				fail("Removal should return false as key does not exist in table");
			}

		} catch (IllegalNullKeyException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test inserts several keys into the same index and then gets one of them,
	 * and makes sure no exceptions are thrown, and that the correct value is
	 * returned
	 */
	@Test
	public void test008_InsertThenGet() {
		HashTableADT htIntegerKey = new HashTable<Integer, String>();
		try {
			htIntegerKey.insert(1, "one");
			htIntegerKey.insert(26, "twenty-six");
			htIntegerKey.insert(51, "fifty-one");
			// three pairs are inserted
			if (!(htIntegerKey.get(1).equals("one"))) {
				// test fails if the get method does not return the correct value
				fail("returned value should be one");
			}
		} catch (IllegalNullKeyException e) {
			e.printStackTrace();
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
		} catch (KeyNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test inserts 20 nodes and ensures that no exceptions are thrown and that
	 * the has table size is properly incremented
	 */
	@Test
	public void test009_InsertCheckTableSize() {
		HashTableADT htIntegerKey = new HashTable<Integer, String>();
		try {
			for (int i = 0; i < 20; i++) {
				htIntegerKey.insert(i, "value");
				// twenty values are inserted, surpassing the load factor threshold
				// so the hash table size should be doubled and become 51
			}
			if (htIntegerKey.getCapacity() != 51) {
				fail("capacity should be doubled plus one from initial!");
				// check that the new table is the correct size
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
