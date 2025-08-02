import java.util.NoSuchElementException;
import java.util.LinkedList;
import java.util.List;

//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;


public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

    /**
     * Inner class that stores the key-value pairs in our hashtable array.
     */
    protected class Pair {

        public KeyType key;
        public ValueType value;

        public Pair(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }

    }

    //Instance fields.
    private int capacity;
    private int size;
    protected LinkedList<Pair>[] table = null;

    //Constructors, with a default capacity of 64.
    @SuppressWarnings("unchecked")
    public HashtableMap() {
        this.capacity = 64;
        table = (LinkedList<Pair>[]) new LinkedList[64];
    }
    @SuppressWarnings("unchecked")
    public HashtableMap(int capacity) {
        this.capacity = capacity;
        table = (LinkedList<Pair>[]) new LinkedList[capacity];
    }

    /**
     * Adds a new key,value pair/mapping to this collection.It is ok that the value is null.
     * @param key the key of the key,value pair
     * @param value the value that key maps to
     * @throws IllegalArgumentException if key already maps to a value
     * @throws NullPointerException if key is null
     */
    @Override
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
        
        //If key is null or is already present in the map, throw an exception.
        if (key == null) { throw new NullPointerException("Key cannot be null."); }
        if (containsKey(key)) { throw new IllegalArgumentException("Key cannot be a duplicate."); }

        //Find the index that the key, value pair should be inserted into.
        int index = findIndex(key);
        //Insert.
        if (table[index] == null) { table[index] = new LinkedList<Pair>(); }
        table[index].add(new Pair(key, value));

        //Increment size.
        size++;

        //Resize the hash table if load factor is greater than or equal to 80%.
        double loadFactor = (double)size/capacity;
        if (loadFactor >= 0.8) { resize(); } 

    }

     /**
     * Checks whether a key maps to a value in this collection.
     * @param key the key to check
     * @return true if the key maps to a value, and false is the
     *         key doesn't map to a value
     */
    @Override
    public boolean containsKey(KeyType key) {
        boolean found = false;

        //Find index of given key.
        int index = findIndex(key);

        //Check if table at index contains the given key.
        if (table[index] == null) { return false; }
        for (Pair p : table[index]) {
            if (p.key.equals(key)) {
                found = true;
            }
        }

        return found;
    }

    /**
     * Retrieves the specific value that a key maps to.
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        ValueType result = null;

        //If the key is not found in the map, throw an exception.
        if (!containsKey(key)) { 
            throw new NoSuchElementException("Key doesn't exist in map."); 
        }

        //Find where the key is located and return the value it holds.
        int index = findIndex(key);

        for (Pair p : table[index]) {
            if (p.key.equals(key)) {
                result = p.value;
            }
        }

        return result;
    }

    /**
     * Remove the mapping for a key from this collection.
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    @Override
    public ValueType remove(KeyType key) throws NoSuchElementException {
        ValueType result = null;

        //If the key is not found in the map, throw an exception.
        if (!containsKey(key)) { throw new NoSuchElementException("Key doesn't exist in map."); }

        //Find where the key is located and return (and remove) the value it holds.
        int index = findIndex(key);
        LinkedList<Pair> pairs = table[index];

        if (pairs != null) {
            for (Pair p : pairs) {
                if (p.key.equals(key)) {
                    result = p.value;
                    pairs.remove(p);
                    size--;
                }
            }
        }

        return result;
    }

    /**
     * Removes all key,value pairs from this collection.
     */
    @Override
    public void clear() {
        //Clear the table by setting all elements in the hashtable to null.
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        //Reset size.
        size = 0;
    }

    /**
     * Retrieves the number of keys stored in this collection.
     * @return the number of keys stored in this collection
     */
    @Override
    public int getSize() {
        return size;
    }

     /**
     * Retrieves this collection's capacity.
     * @return the size of te underlying array for this collection
     */
    @Override
    public int getCapacity() {
        return capacity;
    }

    /**
     * Retrieves this collection's keys.
     * @return a list of keys in the underlying array for this collection
     */
    public List<KeyType> getKeys() {
        List<KeyType> keys = new LinkedList<>();

        for (LinkedList<Pair> pairs : table) {

            if (pairs != null) {
                for (Pair pair : pairs) {
                    keys.add(pair.key);
                }
            }
        }

        return keys;
    }
    
    /**
     * Helper method that finds the index to insert a key.
     */
    private int findIndex(KeyType key) {
        return Math.abs(key.hashCode() % capacity);
    }

    /**
     * Helper method that resizes and rehashes the hashtable.
     */
    private void resize() {

        //Save the current table.
        LinkedList<Pair>[] current = table;

        //Create new table with doubled capacity, as per instructions.
        capacity = 2*capacity;
        table = (LinkedList<Pair>[])new LinkedList[capacity];

        //Add each element from current table to new table.
        for (int i = 0; i < current.length; i++) {
            
            //For each non null element in the table:
            if (current[i] != null) {
                LinkedList<Pair> pairs = current[i];

                for (int j = 0; j < pairs.size(); j++) {
                    Pair pair = pairs.get(j);

                    //Find new index and insert into new table.
                    int newIndex = findIndex(pair.key);
                    if (table[newIndex] == null) { table[newIndex] = new LinkedList<Pair>(); }
                    table[newIndex].add(pair);

                }
            }
        }

    }
}
