package com.library.ds;

/**
 * Hash table implementation using separate chaining with CustomLinkedList.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class CustomHashMap<K, V> {

    /**
     * Key-value pair stored inside hash map buckets.
     *
     * @param <K> key type
     * @param <V> value type
     */
    public static class Entry<K, V> extends HashEntry<K, V> {
        /**
         * Constructs a map entry.
         * Complexity: O(1)
         *
         * @param key   entry key
         * @param value entry value
         */
        public Entry(K key, V value) {
            super(key, value);
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    private CustomLinkedList<Entry<K, V>>[] buckets;
    private int capacity;
    private int size;
    private int resizeCount;

    /**
     * Constructs an empty CustomHashMap with default capacity 16.
     * Complexity: O(1)
     */
    @SuppressWarnings("unchecked")
    public CustomHashMap() {
        this.capacity = INITIAL_CAPACITY;
        this.buckets = (CustomLinkedList<Entry<K, V>>[]) new CustomLinkedList<?>[capacity];
        this.size = 0;
        this.resizeCount = 0;
    }

    /**
     * Removes all key-value mappings from this map.
     * Complexity: O(capacity)
     */
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            buckets[i] = null;
        }
        this.size = 0;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * Complexity: O(1) average, O(n) worst case
     *
     * @param key   key with which the value is associated
     * @param value value to associate
     * @return previous value associated with key, or null if none
     * @throws IllegalArgumentException if the key is null
     */
    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Null keys are not permitted.");
        }
        int index = getBucketIndex(key, capacity);
        if (buckets[index] == null) {
            buckets[index] = new CustomLinkedList<>();
        }
        for (Entry<K, V> entry : buckets[index]) {
            if (entry.getKey().equals(key)) {
                V oldVal = entry.getValue();
                entry.setValue(value);
                return oldVal;
            }
        }
        buckets[index].addLast(new Entry<>(key, value));
        size++;
        if ((double) size / capacity > LOAD_FACTOR_THRESHOLD) {
            resize();
        }
        return null;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if none.
     * Complexity: O(1) average, O(n) worst case
     *
     * @param key key whose mapped value is to be returned
     * @return mapped value, or null if not found
     * @throws IllegalArgumentException if the key is null
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Null keys are not permitted.");
        }
        int index = getBucketIndex(key, capacity);
        if (buckets[index] != null) {
            for (Entry<K, V> entry : buckets[index]) {
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     * Complexity: O(1) average, O(n) worst case
     *
     * @param key key whose mapping is to be removed
     * @return previous value associated with key, or null if none
     * @throws IllegalArgumentException if the key is null
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Null keys are not permitted.");
        }
        int index = getBucketIndex(key, capacity);
        if (buckets[index] == null) {
            return null;
        }
        Entry<K, V> toRemove = null;
        for (Entry<K, V> entry : buckets[index]) {
            if (entry.getKey().equals(key)) {
                toRemove = entry;
                break;
            }
        }
        if (toRemove != null) {
            buckets[index].remove(toRemove);
            size--;
            return toRemove.getValue();
        }
        return null;
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     * Complexity: O(1) average, O(n) worst case
     *
     * @param key key whose presence in this map is to be tested
     * @return true if mapping exists, false otherwise
     * @throws IllegalArgumentException if the key is null
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Returns the number of key-value mappings in this map.
     * Complexity: O(1)
     *
     * @return map size
     */
    public int size() {
        return size;
    }

    /**
     * Returns a CustomLinkedList view of the keys contained in this map.
     * Complexity: O(n + capacity)
     *
     * @return list of keys
     */
    public CustomLinkedList<K> keys() {
        CustomLinkedList<K> keyList = new CustomLinkedList<>();
        for (int i = 0; i < capacity; i++) {
            if (buckets[i] != null) {
                for (Entry<K, V> entry : buckets[i]) {
                    keyList.addLast(entry.getKey());
                }
            }
        }
        return keyList;
    }

    /**
     * Returns a CustomLinkedList view of the values contained in this map.
     * Complexity: O(n + capacity)
     *
     * @return list of values
     */
    public CustomLinkedList<V> values() {
        CustomLinkedList<V> valList = new CustomLinkedList<>();
        for (int i = 0; i < capacity; i++) {
            if (buckets[i] != null) {
                for (Entry<K, V> entry : buckets[i]) {
                    valList.addLast(entry.getValue());
                }
            }
        }
        return valList;
    }

    /**
     * Returns a snapshot of hash table metrics and collision characteristics.
     * Complexity: O(capacity)
     *
     * @return HashStats object
     */
    public HashStats stats() {
        int nonEmpty = 0;
        int maxChain = 0;
        for (int i = 0; i < capacity; i++) {
            if (buckets[i] != null && !buckets[i].isEmpty()) {
                nonEmpty++;
                int chainLen = buckets[i].size();
                if (chainLen > maxChain) {
                    maxChain = chainLen;
                }
            }
        }
        double lf = (double) size / capacity;
        return new HashStats(capacity, size, lf, nonEmpty, maxChain, resizeCount);
    }

    /**
     * Returns all keys residing in the specified bucket index.
     * Complexity: O(chain length)
     *
     * @param bucketIndex index of the bucket
     * @return list of keys in the bucket
     * @throws IndexOutOfBoundsException if bucketIndex is invalid
     */
    public CustomLinkedList<K> bucketKeys(int bucketIndex) {
        if (bucketIndex < 0 || bucketIndex >= capacity) {
            throw new IndexOutOfBoundsException("Invalid bucket index: " + bucketIndex);
        }
        CustomLinkedList<K> list = new CustomLinkedList<>();
        if (buckets[bucketIndex] != null) {
            for (Entry<K, V> entry : buckets[bucketIndex]) {
                list.addLast(entry.getKey());
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        CustomLinkedList<Entry<K, V>>[] newBuckets =
                (CustomLinkedList<Entry<K, V>>[]) new CustomLinkedList<?>[newCapacity];

        for (int i = 0; i < capacity; i++) {
            if (buckets[i] != null) {
                for (Entry<K, V> entry : buckets[i]) {
                    int newIndex = getBucketIndex(entry.getKey(), newCapacity);
                    if (newBuckets[newIndex] == null) {
                        newBuckets[newIndex] = new CustomLinkedList<>();
                    }
                    newBuckets[newIndex].addLast(entry);
                }
            }
        }
        this.buckets = newBuckets;
        this.capacity = newCapacity;
        this.resizeCount++;
    }

    private int getBucketIndex(K key, int modCapacity) {
        int h;
        if (key instanceof String) {
            String str = (String) key;
            h = 0;
            for (int i = 0; i < str.length(); i++) {
                h = h * 31 + str.charAt(i);
            }
        } else {
            h = key.hashCode();
        }
        return (h & 0x7fffffff) % modCapacity;
    }
}
