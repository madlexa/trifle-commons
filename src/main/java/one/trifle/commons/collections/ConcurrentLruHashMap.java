package one.trifle.commons.collections;

import one.trifle.commons.utils.Objects;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentLruHashMap<K, V> extends AbstractMap<K, V>
        implements ConcurrentMap<K, V>, Cloneable, Serializable {

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
    private final int backedSize;
    private transient final AtomicReference<Entry<K, V>>[] table;
    private final AtomicReference<Entry<K, V>> root = new AtomicReference<Entry<K, V>>(Entry.EMPTY);
    private final AtomicReference<Entry<K, V>> last = new AtomicReference<Entry<K, V>>(Entry.EMPTY);
    private final AtomicInteger modCount = new AtomicInteger(0);
    private final AtomicInteger size = new AtomicInteger(0);

    @SuppressWarnings("unchecked")
    public ConcurrentLruHashMap(int size) {
        backedSize = (int) ((size / LOAD_FACTOR) + 1);
        table = new AtomicReference[backedSize];
        Arrays.fill(table, new AtomicReference<Entry<K, V>>());
        this.maxSize = size;
    }

    static final int spread(int hash) {
        return (hash ^ (hash >>> 16)) & HASH_BITS;
    }

    static final int backed(int length, int hash) {
        return (length - 1) & hash;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V put(K key, V value) {
        return putVal(key, value, false);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return putVal(key, value, true);
    }

    @Override
    public V get(Object key) {
        Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            return null;
        }
        return entry.value;
    }

    @Override
    public V remove(Object key) {
        Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            return null;
        }
        return removeEntry(entry);
    }

    Entry<K, V> getEntry(Object key) {
        int hash = spread(key.hashCode());
        int backed = backed(backedSize, hash);

        Entry<K, V> entry;
        if ((entry = table[backed].get()) == null) {
            return null;
        }
        if (entry.hash == hash && Objects.equals(entry.key, key)) {
            return entry;
        }
        while ((entry = entry.next.get()) != null) {
            if (entry.hash == hash && Objects.equals(entry.key, key)) {
                return entry;
            }
        }
        return null;
    }

    final V putVal(K key, V value, boolean onlyIfAbsent) {
        int hash = spread(key.hashCode());
        int backed = backed(backedSize, hash);
        Entry<K, V> entry;
        V oldValue;
        boolean isAdd = false;

        outer:
        while (true) {
            Entry<K, V> last, prev;
            entry = null;
            // add entry in free backed
            if ((last = table[backed].get()) == null) {
                entry = new Entry<K, V>(hash, key, value);
                if (!table[backed].compareAndSet(null, entry)) {
                    continue;
                }
                oldValue = null;
                isAdd = true;
                break outer;
            }

            // find
            while (true) {
                prev = last;
                if (last.hash == hash && Objects.equals(last.key, key)) {
                    entry = last;
                    break;
                }
                if ((last = last.next.get()) == null) {
                    break;
                }
            }

            // if not find
            if (entry == null) {
                // add last
                entry = new Entry<K, V>(hash, key, value);
                while (true) {
                    synchronized (table[backed]) {
                        if (!prev.next.compareAndSet(null, entry)) {
                            prev = prev.next.get();
                            // if find then update
                            if (prev.hash == entry.hash && Objects.equals(prev.key, key)) {
                                entry = prev;
                                oldValue = entry.value;
                                if (!onlyIfAbsent) {
                                    entry.value = value;
                                }
                                break;
                            }
                        } else {
                            isAdd = true;
                            oldValue = null;
                            break;
                        }
                    }
                }
            } else {
                oldValue = entry.value;
                if (!onlyIfAbsent) {
                    entry.value = value;
                }
            }

            // check element
            if ((last = table[backed].get()) != null) {
                if (last == entry) {
                    break outer;
                }
                while ((last = last.next.get()) != null) {
                    if (last == entry) {
                        break outer;
                    }
                }
            }
            isAdd = false;
        }

        if (isAdd) {
            modCount.incrementAndGet();
            size.incrementAndGet();
        }
        return oldValue;
    }

    final V removeEntry(Entry<K, V> entry) {
        int backed = backed(backedSize, entry.hash);
        boolean isDeleted = false;
        Entry<K, V> cur;
        outer:
        while (true) {
            if ((cur = table[backed].get()) != null) {
                if (cur == entry) {
                    synchronized (table[backed]) {
                        if (!table[backed].compareAndSet(cur, cur.next.get())) {
                            continue;
                        }
                    }
                    isDeleted = true;
                    break;
                } else {
                    Entry<K, V> prev = null;
                    while (cur != entry && cur != null) {
                        prev = cur;
                        cur = cur.next.get();
                    }

                    if (cur == null) {
                        isDeleted = false;
                        break;
                    }

                    synchronized (table[backed]) {
                        if (!prev.next.compareAndSet(cur, cur.next.get())) {
                            continue;
                        }
                    }

                    // check
                    Entry<K, V> element = table[backed].get();
                    if (element == cur) {
                        continue;
                    }
                    while (element != null && (element = element.next.get()) != null) {
                        if (element == cur) {
                            continue outer;
                        }
                    }

                    isDeleted = true;
                    break;
                }
            } else {
                isDeleted = false;
                break;
            }
        }

        if (isDeleted) {
            size.decrementAndGet();
            modCount.incrementAndGet();
        }
        return entry.value;
    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V replace(K key, V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Node in the Map.  Doubles as a means to pass key-value pairs back to
     * user (see Map.Entry).
     */
    static class Entry<K, V> implements Map.Entry<K, V> {
        static final Entry EMPTY = new Entry(0, null, null) {
            @Override
            public boolean equals(Object o) {
                return this == o;
            }
        };
        final int hash;
        final K key;
        final AtomicReference<Entry<K, V>> before = new AtomicReference<Entry<K, V>>();
        final AtomicReference<Entry<K, V>> after = new AtomicReference<Entry<K, V>>();
        final AtomicReference<Entry<K, V>> next = new AtomicReference<Entry<K, V>>();
        volatile V value;

        Entry(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
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
            throw new UnsupportedOperationException();
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
}
