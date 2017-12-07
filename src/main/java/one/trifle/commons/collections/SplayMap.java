package one.trifle.commons.collections;

import one.trifle.commons.utils.Objects;

import java.io.Serializable;
import java.util.*;

public class SplayMap<K, V> extends AbstractMap<K, V>
        implements NavigableMap<K, V>, Cloneable, Serializable {

    /**
     * The comparator used to maintain order in this splay map, or
     * null if it uses the natural ordering of its keys.
     *
     * @serial
     */
    private Comparator<? super K> comparator;

    private transient Entry<K, V> root;

    private transient int size = 0;

    private transient int modCount = 0;

    /**
     * Constructs a new, empty splay map, using the natural ordering of its
     * keys.  All keys inserted into the map must implement the {@link
     * Comparable} interface.  Furthermore, all such keys must be
     * <em>mutually comparable</em>: {@code k1.compareTo(k2)} must not throw
     * a {@code ClassCastException} for any keys {@code k1} and
     * {@code k2} in the map.  If the user attempts to put a key into the
     * map that violates this constraint (for example, the user attempts to
     * put a string key into a map whose keys are integers), the
     * {@code put(Object key, Object value)} call will throw a
     * {@code ClassCastException}.
     */
    public SplayMap() {
        comparator = null;
    }

    /**
     * Constructs a new, empty splay map, ordered according to the given
     * comparator.  All keys inserted into the map must be <em>mutually
     * comparable</em> by the given comparator: {@code comparator.compare(k1,
     * k2)} must not throw a {@code ClassCastException} for any keys
     * {@code k1} and {@code k2} in the map.  If the user attempts to put
     * a key into the map that violates this constraint, the {@code put(Object
     * key, Object value)} call will throw a
     * {@code ClassCastException}.
     *
     * @param comparator the comparator that will be used to order this map.
     *                   If {@code null}, the {@linkplain Comparable natural
     *                   ordering} of the keys will be used.
     */
    public SplayMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }


    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> lowerEntry(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public K lowerKey(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> floorEntry(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public K floorKey(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public K ceilingKey(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> higherEntry(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public K higherKey(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<K, V> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<K> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<K> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super K> comparator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public K firstKey() {
        throw new UnsupportedOperationException();
    }

    @Override
    public K lastKey() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(Object key) {
        Entry<K, V> element = find(root, key);
        if (element == null)
            return null;

        root = splay(element);
        return element.value;
    }

    private Entry<K, V> find(Entry<K, V> parent, Object key) {
        Entry<K, V> element = parent;
        if (element == null)
            return null;

        int cmp;
        do {
            cmp = compare(key, element.key);
            if (cmp < 0)
                element = element.left;
            else if (cmp > 0)
                element = element.right;
        } while (element != null && compare(element.key, key) != 0);

        if (element == null)
            return null;

        return element;
    }

    @Override
    public V put(K key, V value) {
        Entry<K, V> tmp = root;
        if (tmp == null) {
            if (comparator == null) {
                comparator = new Comparator<K>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public int compare(K key1, K key2) {
                        return ((Comparable<? super K>) key1).compareTo(key2);
                    }
                };
            }
            compare(key, key); // type (and possibly null) check

            root = new Entry<K, V>(key, value, null);
            size = 1;
            modCount++;
            return null;
        }
        int cmp;
        Entry<K, V> parent;
        do {
            parent = tmp;
            cmp = compare(key, parent.key);
            if (cmp < 0)
                tmp = parent.left;
            else if (cmp > 0)
                tmp = parent.right;
            else
                return parent.setValue(value);
        } while (tmp != null);

        Entry<K, V> element = new Entry<K, V>(key, value, parent);
        if (cmp < 0)
            parent.left = element;
        else
            parent.right = element;

        size++;
        modCount++;
        return null;
    }

    @SuppressWarnings("unchecked")
    private int compare(Object k1, Object k2) {
        return comparator.compare((K) k1, (K) k2);
    }

    private Entry<K, V> splay(Entry<K, V> element) {
        if (element.parent == null)
            return element;

        Entry<K, V> parent = element.parent;
        Entry<K, V> gparent = parent.parent;

        if (gparent == null) {
            rotate(parent, element);
            return element;
        }

        boolean zigzig = Objects.equals(gparent.left, parent) == Objects.equals(parent.left, element);
        if (zigzig) {
            rotate(gparent, parent);
            rotate(parent, element);
        } else {
            rotate(parent, element);
            rotate(gparent, element);
        }
        return splay(element);
    }

    private void set_parent(Entry<K, V> child, Entry<K, V> parent) {
        if (child != null)
            child.parent = parent;
    }

    private void keep_parent(Entry<K, V> element) {
        set_parent(element.left, element);
        set_parent(element.right, element);
    }

    private void rotate(Entry<K, V> parent, Entry<K, V> child) {
        Entry<K, V> gparent = parent.parent;
        if (gparent != null)
            if (parent.equals(gparent.left))
                gparent.left = child;
            else
                gparent.right = child;

        if (child.equals(parent.left)) {
            parent.left = child.right;
            child.right = parent;
        } else {
            parent.right = child.left;
            child.left = parent;
        }

        keep_parent(child);
        keep_parent(parent);
        child.parent = gparent;
    }

    @Override
    public V remove(Object key) {
        Entry<K, V> element = find(root, key);
        if (element == null)
            return null;
        Entry<K, V> parent = element.parent;
        //TODO
        return element.value;
    }

    private Entry<K, V> merge(Entry<K, V> left, Entry<K, V> right) {
        /*if (left == null)
            return right;
        if (right == null)
            return left;
        Entry<K, V> entry = find(right, left.key);
        if (entry == null)
            right
        splay(entry);
        right =
        TODO
        */
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    /**
     * Node in the Tree.  Doubles as a means to pass key-value pairs back to
     * user (see Map.Entry).
     */
    static final class Entry<K, V> implements Map.Entry<K, V> {
        K key;
        V value;
        Entry<K, V> left;
        Entry<K, V> right;
        Entry<K, V> parent;

        /**
         * Make a new cell with given key, value, and parent, and with
         * {@code null} child links.
         */
        Entry(K key, V value, Entry<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        /**
         * Returns the key.
         *
         * @return the key
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the value associated with the key.
         *
         * @return the value associated with the key
         */
        public V getValue() {
            return value;
        }

        /**
         * Replaces the value currently associated with the key with the given
         * value.
         *
         * @return the value associated with the key before this method was
         * called
         */
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;

            return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
        }

        public int hashCode() {
            int keyHash = (key == null ? 0 : key.hashCode());
            int valueHash = (value == null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }

        public String toString() {
            return key + " = " + value;
        }
    }
}
