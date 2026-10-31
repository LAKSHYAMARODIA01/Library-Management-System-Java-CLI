package com.library.ds;

/**
 * Key-value pair stored inside CustomHashMap collision bucket chains.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class HashEntry<K, V> {
    private final K key;
    private V value;

    /**
     * Constructs a new HashEntry.
     * Complexity: O(1)
     *
     * @param key   entry key
     * @param value entry value
     */
    public HashEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the entry key.
     * Complexity: O(1)
     *
     * @return key
     */
    public K getKey() {
        return key;
    }

    /**
     * Returns the entry value.
     * Complexity: O(1)
     *
     * @return value
     */
    public V getValue() {
        return value;
    }

    /**
     * Updates the entry value.
     * Complexity: O(1)
     *
     * @param value new value
     */
    public void setValue(V value) {
        this.value = value;
    }
}
