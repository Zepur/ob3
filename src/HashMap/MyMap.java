package HashMap;

import java.util.LinkedList;

public interface MyMap<K, V> {

    void clear();

    boolean containsKey(K key);

    java.util.Set<Entry<K, V>> getExistingEntries();

    V get(K key);

    boolean isEmpty();

    java.util.Set<K> getExistingKeys();

    V put(K key, V value);

    void remove(K key);

    int size();

    java.util.Set<V> getExistingValues();

    class Entry<K, V> {
        K key;
        LinkedList<V> valuesList;

        public Entry(K key, V value) {
            this.key = key;
            this.valuesList = new LinkedList<>();
            valuesList.add(value);
        }

        public K getKey() {
            return key;
        }

        public LinkedList<V> getValue() {
            return valuesList;
        }

        @Override
        public String toString() {
            return "[" + key + ", " + valuesList + "]";
        }
    }
}