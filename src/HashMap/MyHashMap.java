package HashMap;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static int DEFAULT_INITIAL_CAPACITY = 4;
    private static int MAXIMUM_CAPACITY = 1 << 30;
    private int capacity;
    private static float DEFAULT_MAX_LOAD_FACTOR = 0.75f;
    private float loadFactorThreshold;
    private int numberOfMapEntries = 0;
    LinkedList<MyMap.Entry<K,V>>[] table;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_MAX_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactorThreshold) {
        if (initialCapacity > MAXIMUM_CAPACITY)
            this.capacity = MAXIMUM_CAPACITY;
        else
            this.capacity = trimToPowerOf2(initialCapacity);
        this.loadFactorThreshold = loadFactorThreshold;
        table = new LinkedList[capacity];
    }

    @Override
    public void clear() {
        numberOfMapEntries = 0;
        removeEntries();
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public java.util.Set<MyMap.Entry<K,V>> getExistingEntries() {
        java.util.Set<MyMap.Entry<K, V>> set = new java.util.HashSet<>();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                LinkedList<Entry<K, V>> bucket = table[i];
                for (Entry<K, V> entry : bucket)
                    set.add(entry);
            }
        }
        return set;
    }

    @Override
    public V get(K key) {
        int bucketIndex = hash(key.hashCode());
        if (table[bucketIndex] != null) {
            LinkedList<Entry<K, V>> bucket = table[bucketIndex];
            for (Entry<K, V> entry: bucket)
                if (entry.getKey().equals(key))
                    return entry.getValue().getFirst();
        }
        return null;
    }

    public java.util.Set<V> getAll(K key) {
        java.util.Set<V> set = new java.util.HashSet<>();
        int bucketIndex = hash(key.hashCode());
        if (table[bucketIndex] != null) {
            LinkedList<Entry<K, V>> bucket = table[bucketIndex];
            for (Entry<K, V> entry: bucket)
                if (entry.getKey().equals(key))
                    for (V value : entry.valuesList)
                        set.add(value);
            return set;
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return (numberOfMapEntries == 0);
    }

    @Override
    public java.util.Set<K> getExistingKeys() {
        java.util.Set<K> set = new java.util.HashSet<>();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                LinkedList<Entry<K, V>> bucket = table[i];
                for (Entry<K, V> entry : bucket)
                    set.add(entry.getKey());
            }
        }
        return set;
    }

    @Override
    public V put(K key, V value) {
        if(key == null)
            throw new NullPointerException("Key was null.");
        if(value == null)
            throw new NullPointerException("Value was null.");
        if (get(key) != null) {
            int bucketIndex = hash(key.hashCode());
            LinkedList<Entry<K, V>> bucket = table[bucketIndex];
            for (Entry<K, V> entry : bucket)
                if (entry.getKey().equals(key)) {
                    entry.valuesList.addLast(value);
                    return value;
                }
        }

        if (numberOfMapEntries >= capacity * loadFactorThreshold)
            if (capacity == MAXIMUM_CAPACITY)
                throw new RuntimeException("Exceeding maximum capacity");
            rehash();

        int bucketIndex = hash(key.hashCode());
        if (table[bucketIndex] == null)
            table[bucketIndex] = new LinkedList<>();
        table[bucketIndex].add(new MyMap.Entry<>(key, value));
        numberOfMapEntries++;

        return value;
    }

    @Override
    public void remove(K key) {
        if(!containsKey(key))
            throw new IllegalArgumentException("Key \"" + key + "\" does not exist.");
        int bucketIndex = hash(key.hashCode());
        if (table[bucketIndex] != null) {
            LinkedList<Entry<K, V>> bucket = table[bucketIndex];
            for (Entry<K, V> entry : bucket)
                if (entry.getKey().equals(key)) {
                    bucket.remove(entry);
                    numberOfMapEntries--;
                    break;
                }
        }
    }

    @Override
    public int size() {
        return numberOfMapEntries;
    }

    @Override
    public java.util.Set<V> getExistingValues() {
        java.util.Set<V> set = new java.util.HashSet<>();

        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                LinkedList<Entry<K, V>> bucket = table[i];
                for (Entry<K, V> entry : bucket)
                    for(V value : entry.valuesList)
                        set.add(value);
            }
        }

        return set;
    }

    private int hash(int hashCode) {
        return supplementalHash(hashCode) & (capacity - 1);
    }

    private static int supplementalHash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    private int trimToPowerOf2(int initialCapacity) {
        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        return capacity;
    }

    private void removeEntries() {
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                table[i].clear();
            }
        }
    }

    private void rehash() {
        java.util.Set<Entry<K, V>> set = getExistingEntries();
        capacity <<= 1;
        table = new LinkedList[capacity];
        numberOfMapEntries = 0;

        for (Entry<K, V> entry : set) {
            for(V value : entry.valuesList)
                put(entry.getKey(), value);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i].size() > 0)
                for (Entry<K, V> entry : table[i])
                    builder.append(entry);
        }

        builder.append("]");
        return builder.toString();
    }
}