package com.library.ds;

/**
 * Statistics snapshot for CustomHashMap profiling and insights.
 */
public class HashStats {

    private final int capacity;
    private final int size;
    private final double loadFactor;
    private final int nonEmptyBuckets;
    private final int longestChain;
    private final int resizeCount;

    /**
     * Constructs a HashStats object with the given metrics.
     * Complexity: O(1)
     *
     * @param capacity        total bucket array capacity
     * @param size            number of stored entries
     * @param loadFactor      current ratio of size to capacity
     * @param nonEmptyBuckets count of buckets containing at least one entry
     * @param longestChain    maximum chain length among all buckets
     * @param resizeCount     number of times the map has resized
     */
    public HashStats(int capacity, int size, double loadFactor, int nonEmptyBuckets,
                     int longestChain, int resizeCount) {
        this.capacity = capacity;
        this.size = size;
        this.loadFactor = loadFactor;
        this.nonEmptyBuckets = nonEmptyBuckets;
        this.longestChain = longestChain;
        this.resizeCount = resizeCount;
    }

    /**
     * Returns the total bucket capacity of the hash map.
     * Complexity: O(1)
     *
     * @return bucket capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the number of key-value pairs stored in the hash map.
     * Complexity: O(1)
     *
     * @return entry count
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the load factor (size / capacity) of the hash map.
     * Complexity: O(1)
     *
     * @return load factor
     */
    public double getLoadFactor() {
        return loadFactor;
    }

    /**
     * Returns the number of buckets that contain at least one entry.
     * Complexity: O(1)
     *
     * @return non-empty bucket count
     */
    public int getNonEmptyBuckets() {
        return nonEmptyBuckets;
    }

    /**
     * Returns the length of the longest collision chain.
     * Complexity: O(1)
     *
     * @return longest chain length
     */
    public int getLongestChain() {
        return longestChain;
    }

    /**
     * Returns the number of times the hash map has resized.
     * Complexity: O(1)
     *
     * @return resize count
     */
    public int getResizeCount() {
        return resizeCount;
    }

    /**
     * Returns a string representation of hash statistics.
     * Complexity: O(1)
     *
     * @return formatted string of stats
     */
    @Override
    public String toString() {
        return "Capacity: " + capacity
                + ", Size: " + size
                + ", Load Factor: " + String.format("%.2f", loadFactor)
                + ", Non-empty Buckets: " + nonEmptyBuckets
                + ", Longest Chain: " + longestChain
                + ", Resize Count: " + resizeCount;
    }
}
