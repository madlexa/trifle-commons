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

    private transient EntrySet entrySet;

    private transient KeySet<K> navigableKeySet;

    private transient Collection<V> values;

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
        EntrySet es = entrySet;
        return (es != null) ? es : (entrySet = new EntrySet());
    }

    @Override
    public Entry<K, V> lowerEntry(K key) {
        Entry<K, V> p = root;
        while (p != null) {
            int cmp = compare(key, p.key);
            if (cmp > 0) {
                if (p.right != null)
                    p = p.right;
                else
                    return p;
            } else {
                if (p.left != null) {
                    p = p.left;
                } else {
                    Entry<K, V> parent = p.parent;
                    Entry<K, V> ch = p;
                    while (parent != null && ch == parent.left) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            }
        }
        return null;
    }

    @Override
    public K lowerKey(K key) {
        Entry<K, V> entry = lowerEntry(key);
        if (entry == null)
            return null;
        return entry.key;
    }

    @Override
    public Entry<K, V> floorEntry(K key) {
        Entry<K, V> p = root;
        while (p != null) {
            int cmp = compare(key, p.key);
            if (cmp > 0) {
                if (p.right != null)
                    p = p.right;
                else
                    return p;
            } else if (cmp < 0) {
                if (p.left != null) {
                    p = p.left;
                } else {
                    Entry<K, V> parent = p.parent;
                    Entry<K, V> ch = p;
                    while (parent != null && ch == parent.left) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            } else
                return p;

        }
        return null;
    }

    @Override
    public K floorKey(K key) {
        Entry<K, V> entry = floorEntry(key);
        if (entry == null)
            return null;
        return entry.key;
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) {
        Entry<K, V> p = root;
        while (p != null) {
            int cmp = compare(key, p.key);
            if (cmp < 0) {
                if (p.left != null)
                    p = p.left;
                else
                    return p;
            } else if (cmp > 0) {
                if (p.right != null) {
                    p = p.right;
                } else {
                    Entry<K, V> parent = p.parent;
                    Entry<K, V> ch = p;
                    while (parent != null && ch == parent.right) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            } else
                return p;
        }
        return null;
    }

    @Override
    public K ceilingKey(K key) {
        Entry<K, V> entry = ceilingEntry(key);
        if (entry == null)
            return null;
        return entry.key;
    }

    @Override
    public Entry<K, V> higherEntry(K key) {
        Entry<K, V> p = root;
        while (p != null) {
            int cmp = compare(key, p.key);
            if (cmp < 0) {
                if (p.left != null)
                    p = p.left;
                else
                    return p;
            } else {
                if (p.right != null) {
                    p = p.right;
                } else {
                    Entry<K, V> parent = p.parent;
                    Entry<K, V> ch = p;
                    while (parent != null && ch == parent.right) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            }
        }
        return null;
    }

    @Override
    public K higherKey(K key) {
        Entry<K, V> entry = higherEntry(key);
        if (entry == null)
            return null;
        return entry.key;
    }

    @Override
    public Entry<K, V> firstEntry() {
        return min(root);
    }

    @Override
    public Entry<K, V> lastEntry() {
        return max(root);
    }

    /**
     * Return SimpleImmutableEntry for entry, or null if null
     */
    static <K, V> Map.Entry<K, V> exportEntry(Map.Entry<K, V> e) {
        return (e == null) ? null :
                new AbstractMap.SimpleImmutableEntry<K, V>(e);
    }

    @Override
    public Map.Entry<K, V> pollFirstEntry() {
        Entry<K, V> p = firstEntry();
        Map.Entry<K, V> result = exportEntry(p);
        if (p != null)
            removeEntry(p);
        return result;
    }

    @Override
    public NavigableMap<K, V> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<K> navigableKeySet() {
        KeySet<K> nks = navigableKeySet;
        return (nks != null) ? nks : (navigableKeySet = new KeySet<K>(this));
    }

    @Override
    public NavigableSet<K> descendingKeySet() {
        return descendingMap().navigableKeySet();
    }

    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        Entry<K, V> start = min(root);
        if (start == null) {
            throw new UnsupportedOperationException();
        }
        return subMap(start.key, true, toKey, false);
    }

    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        Entry<K, V> end = max(root);
        if (end == null) {
            throw new UnsupportedOperationException();
        }
        return subMap(fromKey, true, end.key, false);
    }

    @Override
    public Comparator<? super K> comparator() {
        return comparator;
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return subMap(fromKey, true, toKey, false);
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
        Entry<K, V> entry = firstEntry();
        if (entry == null)
            return null;
        return entry.key;
    }

    @Override
    public K lastKey() {
        Entry<K, V> entry = lastEntry();
        if (entry == null)
            return null;
        return entry.key;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Map.Entry<K, V> entry : entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                // TODO need splay?
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        Entry<K, V> entry = getEntry(root, key);
        if (entry != null) {
            // TODO need splay?
            return true;
        }
        return false;
    }

    @Override
    public V get(Object key) {
        Entry<K, V> element = getEntry(root, key);
        if (element == null)
            return null;

        return (root = splay(element)).value;
    }

    private Entry<K, V> getEntry(Entry<K, V> parent, Object key) {
        Entry<K, V> element = parent;
        if (element == null)
            return null;

        int cmp = compare(element.key, key);
        do {
            if (cmp > 0)
                element = element.left;
            else if (cmp < 0)
                element = element.right;
        } while (element != null && (cmp = compare(element.key, key)) != 0);

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
        Entry<K, V> grandparent = parent.parent;

        if (grandparent == null) {
            rotate(parent, element);
            return element;
        }

        boolean zig_zig = Objects.equals(grandparent.left, parent) == Objects.equals(parent.left, element);
        if (zig_zig) {
            rotate(grandparent, parent);
            rotate(parent, element);
        } else {
            rotate(parent, element);
            rotate(grandparent, element);
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
        Entry<K, V> grandparent = parent.parent;
        if (grandparent != null)
            if (parent.equals(grandparent.left))
                grandparent.left = child;
            else
                grandparent.right = child;

        if (child.equals(parent.left)) {
            parent.left = child.right;
            child.right = parent;
        } else {
            parent.right = child.left;
            child.left = parent;
        }

        keep_parent(child);
        keep_parent(parent);
        child.parent = grandparent;
    }

    private void removeEntry(Entry<K, V> entry) {
        if (entry == null)
            return;
        Entry<K, V> v = merge(entry.left, entry.right);
        Entry<K, V> parent = entry.parent;

        if (parent != null) {
            if (entry.equals(parent.left))
                parent.left = v;
            else
                parent.right = v;

            while (parent.parent != null)
                parent = parent.parent;

            root = parent;
        } else {
            root = v;
        }

        if (v != null)
            v.parent = entry.parent;

        size--;
        modCount++;
    }

    @Override
    public V remove(Object key) {
        Entry<K, V> element = getEntry(root, key);
        removeEntry(element);
        if (element == null)
            return null;
        return element.value;
    }

    private Entry<K, V> merge(Entry<K, V> left, Entry<K, V> right) {
        if (left == null)
            return right;
        if (right == null)
            return left;

        left.parent = null;
        right.parent = null;

        Entry<K, V> entry = max(left);
        splay(entry);
        entry.right = right;
        right.parent = entry;

        return entry;
    }

    private Entry<K, V> max(Entry<K, V> root) {
        if (root == null)
            return null;
        while (root.right != null)
            root = root.right;
        return root;
    }

    private Entry<K, V> min(Entry<K, V> root) {
        if (root == null)
            return null;
        while (root.left != null)
            root = root.left;
        return root;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        super.putAll(map);
    }

    @Override
    public void clear() {
        modCount++;
        size = 0;
        root = null;
    }

    @Override
    public Set<K> keySet() {
        return navigableKeySet();
    }

    @Override
    public Collection<V> values() {
        Collection<V> vs = values;
        if (vs == null) {
            vs = new Values();
            values = vs;
        }
        return vs;
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
        @Override
        public K getKey() {
            return key;
        }

        /**
         * Returns the value associated with the key.
         *
         * @return the value associated with the key
         */
        @Override
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
        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;

            return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
        }

        @Override
        public int hashCode() {
            int keyHash = (key == null ? 0 : key.hashCode());
            int valueHash = (value == null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }

        public String toString() {
            return key + " = " + value;
        }
    }

    private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator(min(root));
        }

        @Override
        public int size() {
            return SplayMap.this.size();
        }
    }

    /**
     * Base class for SplayMap Iterators
     */
    abstract class PrivateEntryIterator<T> implements Iterator<T> {
        Entry<K, V> next;
        Entry<K, V> lastReturned;
        int expectedModCount;

        PrivateEntryIterator(Entry<K, V> first) {
            expectedModCount = modCount;
            lastReturned = null;
            next = first;
        }

        @Override
        public final boolean hasNext() {
            return next != null;
        }

        final Entry<K, V> nextEntry() {
            Entry<K, V> e = next;
            if (e == null)
                throw new NoSuchElementException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();

            if (e.right != null) {
                Entry<K, V> n = e.right;
                while (n.left != null) {
                    n = n.left;
                }
                next = n;
            } else if (e.parent != null) {
                if (e.equals(e.parent.left)) {
                    next = e.parent;
                } else {
                    Entry<K, V> element = e;
                    while (element.parent != null && element.equals(element.parent.right)) {
                        element = element.parent;
                    }
                    next = element.parent;
                }
            } else {
                next = null;
            }
            lastReturned = e;

            return e;
        }

        @Override
        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();

            removeEntry(lastReturned);

            expectedModCount = modCount;
            lastReturned = null;
        }
    }

    final class EntryIterator extends PrivateEntryIterator<Map.Entry<K, V>> {
        EntryIterator(Entry<K, V> first) {
            super(first);
        }

        @Override
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }


    @Override
    public Map.Entry<K, V> pollLastEntry() {
        Entry<K, V> p = lastEntry();
        Map.Entry<K, V> result = exportEntry(p);
        if (p != null)
            removeEntry(p);
        return result;
    }

    class Values extends AbstractCollection<V> {
        public Iterator<V> iterator() {
            return new ValueIterator(min(root));
        }

        @Override
        public int size() {
            return SplayMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return SplayMap.this.containsValue(o);
        }

        @Override
        public void clear() {
            SplayMap.this.clear();
        }
    }

    final class ValueIterator extends PrivateEntryIterator<V> {
        ValueIterator(Entry<K, V> first) {
            super(first);
        }

        @Override
        public V next() {
            return nextEntry().value;
        }
    }

    Iterator<K> keyIterator() {
        return new KeyIterator(firstEntry());
    }

    Iterator<K> descendingKeyIterator() {
        throw new UnsupportedOperationException();
    }

    static final class KeySet<E> extends AbstractSet<E> implements NavigableSet<E> {
        private final NavigableMap<E, ?> m;

        KeySet(NavigableMap<E, ?> map) {
            m = map;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Iterator<E> iterator() {
            if (m instanceof SplayMap)
                return ((SplayMap<E, ?>) m).keyIterator();
            else
                throw new UnsupportedOperationException("Unsupported sub map");
        }

        @Override
        @SuppressWarnings("unchecked")
        public Iterator<E> descendingIterator() {
            if (m instanceof SplayMap)
                return ((SplayMap<E, ?>) m).descendingKeyIterator();
            else
                throw new UnsupportedOperationException("Unsupported sub map");
        }

        @Override
        public int size() {
            return m.size();
        }

        @Override
        public boolean isEmpty() {
            return m.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return m.containsKey(o);
        }

        @Override
        public void clear() {
            m.clear();
        }

        @Override
        public E lower(E e) {
            return m.lowerKey(e);
        }

        @Override
        public E floor(E e) {
            return m.floorKey(e);
        }

        @Override
        public E ceiling(E e) {
            return m.ceilingKey(e);
        }

        @Override
        public E higher(E e) {
            return m.higherKey(e);
        }

        @Override
        public E first() {
            return m.firstKey();
        }

        @Override
        public E last() {
            return m.lastKey();
        }

        @Override
        public Comparator<? super E> comparator() {
            return m.comparator();
        }

        @Override
        public E pollFirst() {
            Map.Entry<E, ?> e = m.pollFirstEntry();
            return (e == null) ? null : e.getKey();
        }

        @Override
        public E pollLast() {
            Map.Entry<E, ?> e = m.pollLastEntry();
            return (e == null) ? null : e.getKey();
        }

        @Override
        public boolean remove(Object o) {
            int oldSize = size();
            m.remove(o);
            return size() != oldSize;
        }

        @Override
        public NavigableSet<E> subSet(E fromElement, boolean fromInclusive,
                                      E toElement, boolean toInclusive) {
            return new KeySet<E>(m.subMap(fromElement, fromInclusive,
                    toElement, toInclusive));
        }

        @Override
        public NavigableSet<E> headSet(E toElement, boolean inclusive) {
            return new KeySet<E>(m.headMap(toElement, inclusive));
        }

        @Override
        public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
            return new KeySet<E>(m.tailMap(fromElement, inclusive));
        }

        @Override
        public SortedSet<E> subSet(E fromElement, E toElement) {
            return subSet(fromElement, true, toElement, false);
        }

        @Override
        public SortedSet<E> headSet(E toElement) {
            return headSet(toElement, false);
        }

        @Override
        public SortedSet<E> tailSet(E fromElement) {
            return tailSet(fromElement, true);
        }

        @Override
        public NavigableSet<E> descendingSet() {
            return new KeySet<E>(m.descendingMap());
        }
    }

    final class KeyIterator extends PrivateEntryIterator<K> {
        KeyIterator(Entry<K, V> first) {
            super(first);
        }

        public K next() {
            return nextEntry().key;
        }
    }
}