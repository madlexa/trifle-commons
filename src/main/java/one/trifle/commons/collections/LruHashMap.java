package one.trifle.commons.collections;

import one.trifle.commons.utils.Objects;

import java.io.Serializable;
import java.util.*;

public class LruHashMap<K, V> extends AbstractMap<K, V>
        implements Map<K, V>, Serializable {
    /**
     * The load factor for this table. Overrides of this value in
     * constructors affect only the initial table capacity.  The
     * actual floating point value isn't normally used -- it is
     * simpler to use expressions such as {@code n - (n >>> 2)} for
     * the associated resizing threshold.
     */
    private static final float LOAD_FACTOR = 0.75f;
    private static final int HASH_BITS = 0x7fffffff; // usable bits of normal node hash
    private final int maxSize;
    /**
     * The array of bins. Lazily initialized upon first insertion.
     * Size is always a power of two. Accessed directly by iterators.
     */
    private transient final Entry<K, V>[] table;
    private int size;
    private Entry<K, V> root;
    private Entry<K, V> last;
    private transient int modCount = 0;
    private transient EntrySet entrySet;

    @SuppressWarnings("unchecked")
    public LruHashMap(int size) {
        table = new Entry[(int) ((size / LOAD_FACTOR) + 1)];
        this.maxSize = size;
        this.size = 0;
    }

    static final int spread(int hash) {
        return (hash ^ (hash >>> 16)) & HASH_BITS;
    }

    static final int backed(int length, int hash) {
        return (length - 1) & hash;
    }

    @Override
    public V put(K key, V value) {
        return putVal(key, value);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySet es = entrySet;
        return (es != null) ? es : (entrySet = new EntrySet());
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Implementation for put and putIfAbsent
     */
    final V putVal(K key, V value) {
        if (key == null || value == null) throw new NullPointerException();
        int hash = spread(key.hashCode());
        int backed = backed(table.length, hash);
        Entry<K, V> entry;
        if ((entry = table[backed]) == null) { // if not entry in backed
            entry = table[backed] = new Entry<K, V>(hash, key, value, root, null, null);
            modCount++;
            size++;
            setHeadEntry(entry);
            removeTail();
            return null;
        } else {
            V oldVal = null;
            Entry<K, V> cur = entry;
            do {
                if (cur.hash == hash && Objects.equals(cur.key, key)) {
                    oldVal = cur.setValue(value);
                    removeEntryFromStatistic(cur);
                    setHeadEntry(cur);
                    break;
                }

                if (cur.next == null) {
                    setHeadEntry(cur.next = new Entry<K, V>(hash, key, value, root, null, null));
                    size++;
                    break;
                }
            } while ((cur = cur.next) != null);
            if (oldVal != null)
                return oldVal;
        }
        modCount++;
        removeTail();
        return null;
    }

    private void removeTail() {
        while (size > maxSize && last != null) {
            removeEntry(last);
        }
    }

    /**
     * Implements Map.get and related methods
     *
     * @param hash hash for key
     * @param key  the key
     * @return the node, or null if none
     */
    final Entry<K, V> getEntry(int hash, Object key) {
        Entry<K, V> first, entry;
        if ((first = table[backed(table.length, hash)]) != null) {
            if (first.hash == hash && // always check first node
                    Objects.equals(first.key, key)) {
                return first;
            }
            if ((entry = first.next) != null) {
                do {
                    if (entry.hash == hash && Objects.equals(entry.key, key)) {
                        return entry;
                    }
                } while ((entry = entry.next) != null);
            }
        }
        return null;
    }

    @Override
    public V get(Object key) {
        int hash = spread(key.hashCode());
        Entry<K, V> entry = getEntry(hash, key);
        if (entry != null) {
            removeEntryFromStatistic(entry);
            setHeadEntry(entry);
            return entry.value;
        }
        return null;
    }

    private V removeEntry(Entry<K, V> entry) {
        int backed = backed(table.length, entry.hash);
        Entry<K, V> cur = table[backed];
        if (cur == entry) {
            table[backed] = cur.next;
            removeEntryFromStatistic(cur);
            size--;
            modCount++;
            return cur.value;
        }
        Entry<K, V> prev = null;
        while (cur != entry) {
            prev = cur;
            cur = cur.next;
        }
        removeEntryFromStatistic(cur);
        prev.next = cur.next;
        size--;
        modCount++;
        return cur.value;
    }

    private void setHeadEntry(Entry<K, V> entry) {
        entry.after = root;
        if (root != null) {
            root.before = entry;
        }
        if (last == null) {
            last = entry;
        }
        root = entry;
    }

    private void removeEntryFromStatistic(Entry<K, V> entry) {
        Entry<K, V> before = entry.before;
        Entry<K, V> after = entry.after;
        if (before != null) {
            before.after = after;
        } else {
            root = after;
        }
        if (after != null) {
            after.before = before;
        } else {
            last = before;
        }
    }

    /**
     * Node in the Map.  Doubles as a means to pass key-value pairs back to
     * user (see Map.Entry).
     */
    static class Entry<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        Entry<K, V> before, after, next;
        V value;

        Entry(int hash, K key, V value, Entry<K, V> after, Entry<K, V> before, Entry<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.after = after;
            this.before = before;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public boolean equals(Object o) {
            Object k, v, u;
            Map.Entry<?, ?> e;
            return ((o instanceof Map.Entry) &&
                    (k = (e = (Map.Entry<?, ?>) o).getKey()) != null &&
                    (v = e.getValue()) != null &&
                    (k == key || k.equals(key)) &&
                    (v == (u = value) || v.equals(u)));
        }

        @Override
        public int hashCode() {
            return key.hashCode() ^ value.hashCode();
        }

        @Override
        public String toString() {
            return key + " = " + value;
        }
    }

    private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            Entry<K, V> first = null;
            for (Entry<K, V> entry : table)
                if ((first = entry) != null)
                    break;
            return new EntryIterator(first);
        }

        @Override
        public int size() {
            return LruHashMap.this.size();
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

            if (e.next != null) {
                next = e.next;
            } else {
                next = null;
                for (int cursor = backed(table.length, e.hash) + 1; cursor < table.length; cursor++)
                    if ((next = table[cursor]) != null)
                        break;
            }
            return lastReturned = e;
        }

        @Override
        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();

            LruHashMap.this.removeEntry(lastReturned);

            expectedModCount = modCount;
            lastReturned = null;
        }
    }
}
