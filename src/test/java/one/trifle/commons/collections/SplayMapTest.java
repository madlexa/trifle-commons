package one.trifle.commons.collections;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SplayMapTest {
    @Test
    public void uniqueAdd() {
        // INIT
        Map<String, Integer> map = new SplayMap<String, Integer>();

        // EXEC
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);
        map.put("5", 5);

        // CHECK
        assertEquals(map.size(), 5);
    }

    @Test
    public void doubleAdd() {
        // INIT
        Map<String, Integer> map = new SplayMap<String, Integer>();

        // EXEC
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("1", 4);
        map.put("2", 5);

        // CHECK
        assertEquals(map.size(), 3);
    }

    @Test
    public void getRootValue() {
        // INIT
        Map<String, Integer> map = new SplayMap<String, Integer>();

        // EXEC
        map.put("1", 1);

        // CHECK
        assertEquals(map.get("1"), Integer.valueOf(1));
    }

    @Test
    public void getNotRootValue() {
        // INIT
        Map<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.put(5, 5);
        map.put(6, 6);
        map.put(7, 7);
        map.put(8, 8);
        map.put(9, 9);

        // CHECK
        assertEquals(map.get(9), Integer.valueOf(9));
        assertEquals(map.get(7), Integer.valueOf(7));
        assertEquals(map.get(1), Integer.valueOf(1));
        assertEquals(map.get(1), Integer.valueOf(1));
    }

    @Test
    public void randomAdd() {
        // INIT
        Map<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(1, 1);
        map.put(6, 6);
        map.put(2, 2);
        map.put(7, 7);
        map.put(4, 4);
        map.put(8, 8);
        map.put(3, 3);
        map.put(9, 9);

        // CHECK
        assertEquals(map.get(9), Integer.valueOf(9));
        assertEquals(map.get(7), Integer.valueOf(7));
        assertEquals(map.get(1), Integer.valueOf(1));
        assertEquals(map.get(1), Integer.valueOf(1));
    }

    @Test
    public void getNone() {
        // INIT
        Map<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(1, 1);

        // CHECK
        assertNull(map.get(10));
    }

    @Test
    public void custom_comparator() {
        // INIT
        Map<Integer, Integer> map = new SplayMap<Integer, Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 == null) {
                    if (o2 == null) {
                        return 0;
                    }
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                return o1.compareTo(o2);
            }
        });

        // EXEC
        map.put(null, -1);
        map.put(5, 5);
        map.put(null, -8);
        map.put(1, 1);

        // CHECK
        assertEquals(map.get(null), Integer.valueOf(-8));
    }

    @Test
    public void remove_single_root() {
        // INIT
        Map<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, -1);

        // CHECK
        assertEquals(map.remove(1), Integer.valueOf(-1));
        assertEquals(map.size(), 0);
        assertNull(map.remove(1));
        assertEquals(map.size(), 0);
    }

    @Test
    public void remove_root_has_only_left() {
        // INIT
        Map<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(3, 3);
        map.put(2, 2);
        map.put(1, 1);

        // CHECK
        assertEquals(map.remove(4), Integer.valueOf(4));
        assertEquals(map.size(), 3);
        assertEquals(map.remove(3), Integer.valueOf(3));
        assertEquals(map.size(), 2);

        assertEquals(map.get(2), Integer.valueOf(2));
        assertEquals(map.get(1), Integer.valueOf(1));
    }

    @Test
    public void remove_root_has_only_right() {
        // INIT
        Map<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertEquals(map.remove(1), Integer.valueOf(1));
        assertEquals(map.size(), 3);
        assertEquals(map.remove(2), Integer.valueOf(2));
        assertEquals(map.size(), 2);

        assertEquals(map.get(3), Integer.valueOf(3));
        assertEquals(map.get(4), Integer.valueOf(4));
    }

    @Test
    public void remove_root() {
        // INIT
        Map<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(2, 2);
        map.put(1, 1);
        map.put(3, 3);

        // CHECK
        assertEquals(map.remove(2), Integer.valueOf(2));
        assertEquals(map.size(), 2);
        assertNull(map.remove(2));
        assertEquals(map.size(), 2);

        assertEquals(map.get(1), Integer.valueOf(1));
        assertEquals(map.get(3), Integer.valueOf(3));
    }

    @Test
    public void remove_not_root() {
        // INIT
        Map<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(4, 4);
        map.put(6, 6);
        map.put(2, 2);
        map.put(3, 3);
        map.put(1, 1);

        // CHECK
        assertEquals(map.remove(4), Integer.valueOf(4));
        assertEquals(map.size(), 5);
        assertNull(map.remove(4));
        assertEquals(map.size(), 5);

        assertEquals(map.get(1), Integer.valueOf(1));
        assertEquals(map.get(2), Integer.valueOf(2));
        assertEquals(map.get(3), Integer.valueOf(3));
        assertEquals(map.get(5), Integer.valueOf(5));
        assertEquals(map.get(6), Integer.valueOf(6));
    }

    @Test
    public void remove_child() {
        // INIT
        Map<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(3, 3);
        map.put(2, 2);
        map.put(1, 1);

        // CHECK
        assertEquals(map.remove(1), Integer.valueOf(1));
        assertEquals(map.size(), 2);

        assertEquals(map.get(3), Integer.valueOf(3));
        assertEquals(map.get(2), Integer.valueOf(2));
    }

    @Test
    public void remove_child_last_ordered() {
        // INIT
        Map<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertEquals(map.remove(4), Integer.valueOf(4));
        assertEquals(map.size(), 3);

        assertEquals(map.get(1), Integer.valueOf(1));
        assertEquals(map.get(2), Integer.valueOf(2));
        assertEquals(map.get(3), Integer.valueOf(3));

        assertEquals(map.size(), 3);
    }

    @Test
    public void putAll_empty() {
        // INIT
        Map<Integer, Integer> destination = new SplayMap<Integer, Integer>();
        Map<Integer, Integer> source = new HashMap<Integer, Integer>();

        // EXEC
        destination.putAll(source);

        // CHECK
        assertEquals(destination.size(), 0);
    }

    @Test
    public void putAll_from_empty() {
        // INIT
        Map<Integer, Integer> destination = new SplayMap<Integer, Integer>();
        Map<Integer, Integer> source = new HashMap<Integer, Integer>();

        // EXEC
        destination.put(1, 1);
        destination.put(2, 2);

        destination.putAll(source);

        // CHECK
        assertEquals(destination.size(), 2);
        assertEquals(destination.get(1), Integer.valueOf(1));
        assertEquals(destination.get(2), Integer.valueOf(2));
    }

    @Test
    public void putAll_to_empty() {
        // INIT
        Map<Integer, Integer> destination = new SplayMap<Integer, Integer>();
        Map<Integer, Integer> source = new HashMap<Integer, Integer>();

        // EXEC
        source.put(1, 1);
        source.put(2, 2);

        destination.putAll(source);

        // CHECK
        assertEquals(destination.size(), 2);
        assertEquals(destination.get(1), Integer.valueOf(1));
        assertEquals(destination.get(2), Integer.valueOf(2));
    }

    @Test
    public void putAll_without_interceptions() {
        // INIT
        Map<Integer, Integer> destination = new SplayMap<Integer, Integer>();
        Map<Integer, Integer> source = new HashMap<Integer, Integer>();

        // EXEC
        source.put(1, 1);
        source.put(2, 2);
        destination.put(3, 3);
        destination.put(4, 4);

        destination.putAll(source);

        // CHECK
        assertEquals(destination.size(), 4);
        assertEquals(destination.get(1), Integer.valueOf(1));
        assertEquals(destination.get(2), Integer.valueOf(2));
        assertEquals(destination.get(3), Integer.valueOf(3));
        assertEquals(destination.get(4), Integer.valueOf(4));
    }

    @Test
    public void putAll_with_interceptions() {
        // INIT
        Map<Integer, Integer> destination = new SplayMap<Integer, Integer>();
        Map<Integer, Integer> source = new HashMap<Integer, Integer>();

        // EXEC
        destination.put(1, 1);
        destination.put(2, 2);
        source.put(2, 3);
        source.put(3, 4);

        destination.putAll(source);

        // CHECK
        assertEquals(destination.size(), 3);
        assertEquals(destination.get(1), Integer.valueOf(1));
        assertEquals(destination.get(2), Integer.valueOf(3));
        assertEquals(destination.get(3), Integer.valueOf(4));
    }

    @Test
    public void lowerEntry_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // CHECK
        assertNull(map.lowerEntry(3));
    }

    @Test
    public void lowerEntry_none() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertNull(map.lowerEntry(1));
    }


    @Test
    public void lowerEntry() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertEquals(map.lowerEntry(3), new SplayMap.Entry<Integer, Integer>(2, 2, null));
    }

    @Test
    public void lowerEntry_right_none() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(3, 3);
        map.put(2, 2);
        map.put(4, 4);

        // CHECK
        assertEquals(map.lowerEntry(3), new SplayMap.Entry<Integer, Integer>(2, 2, null));
    }

    @Test
    public void lowerKey_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // CHECK
        assertNull(map.lowerKey(3));
    }

    @Test
    public void lowerKey_none() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertNull(map.lowerKey(1));
    }


    @Test
    public void lowerKey() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertEquals(map.lowerKey(3), Integer.valueOf(2));
    }

    @Test
    public void floorEntry_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // CHECK
        assertNull(map.floorEntry(3));
    }

    @Test
    public void floorEntry_none() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertNull(map.floorEntry(0));
    }


    @Test
    public void floorEntry() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertEquals(map.floorEntry(3), new SplayMap.Entry<Integer, Integer>(3, 3, null));
    }

    @Test
    public void floorEntry_right_none() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(2, 2);

        // CHECK
        assertEquals(map.floorEntry(3), new SplayMap.Entry<Integer, Integer>(2, 2, null));
    }

    @Test
    public void floorKey_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // CHECK
        assertNull(map.floorKey(3));
    }

    @Test
    public void floorKey_none() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertNull(map.floorKey(0));
    }


    @Test
    public void floorKey() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertEquals(map.floorKey(3), Integer.valueOf(3));
    }

    @Test
    public void emptyEntrySet() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        Iterator<Map.Entry<Integer, Integer>> iterator = entries.iterator();

        // CHECK
        assertEquals(entries.size(), 0);
        assertTrue(entries.isEmpty());
        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            Assert.fail();
        } catch (NoSuchElementException ignore) {
        }
    }

    @Test
    public void singleEntrySet() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        Iterator<Map.Entry<Integer, Integer>> iterator = entries.iterator();

        assertEquals(entries.size(), 1);
        assertFalse(entries.isEmpty());
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().getKey(), Integer.valueOf(1));

        try {
            iterator.next();
            Assert.fail();
        } catch (NoSuchElementException ignore) {
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void entrySet() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(4, 4);
        map.put(6, 6);
        map.put(2, 2);
        map.put(3, 3);
        map.put(1, 1);

        // CHECK
        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        assertEquals(entries.size(), 6);

        Integer counter = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            counter++;
            assertEquals(entry.getKey(), counter);
            assertEquals(entry.getValue(), counter);
        }
        assertEquals(counter, Integer.valueOf(6));
    }

    @Test
    public void entrySet_ordered() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.put(5, 5);
        map.put(6, 6);

        // CHECK
        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        assertEquals(entries.size(), 6);

        Integer counter = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            counter++;
            assertEquals(entry.getKey(), counter);
            assertEquals(entry.getValue(), counter);
        }
        assertEquals(counter, Integer.valueOf(6));
    }

    @Test
    public void entrySet_ordered_desc() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(6, 6);
        map.put(5, 5);
        map.put(4, 4);
        map.put(3, 3);
        map.put(2, 2);
        map.put(1, 1);

        // CHECK
        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        assertEquals(entries.size(), 6);

        Integer counter = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            counter++;
            assertEquals(entry.getKey(), counter);
            assertEquals(entry.getValue(), counter);
        }
        assertEquals(counter, Integer.valueOf(6));
    }

    @Test
    public void entrySet_ordered_desc_with_min_root() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(6, 6);
        map.put(5, 5);
        map.put(4, 4);
        map.put(3, 3);
        map.put(2, 2);

        // CHECK
        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        assertEquals(entries.size(), 6);

        Integer counter = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            counter++;
            assertEquals(entry.getKey(), counter);
            assertEquals(entry.getValue(), counter);
        }
        assertEquals(counter, Integer.valueOf(6));
    }

    @Test
    public void entrySet_read_and_put() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        assertEquals(entries.size(), 2);

        Iterator<Map.Entry<Integer, Integer>> iterator = entries.iterator();
        assertEquals(iterator.next().getKey(), Integer.valueOf(1));

        map.put(3, 3);

        try {
            iterator.next();
            Assert.fail();
        } catch (ConcurrentModificationException ignore) {
        }
    }

    @Test
    public void entrySet_double_read() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(4, 4);
        map.put(6, 6);
        map.put(2, 2);
        map.put(3, 3);
        map.put(1, 1);

        // CHECK
        Integer counter_outer = 0;
        for (Map.Entry<Integer, Integer> entry_outer : map.entrySet()) {
            counter_outer++;
            assertEquals(entry_outer.getKey(), counter_outer);
            assertEquals(entry_outer.getValue(), counter_outer);

            Integer counter_inner = 0;
            for (Map.Entry<Integer, Integer> entry_inner : map.entrySet()) {
                counter_inner++;
                assertEquals(entry_inner.getKey(), counter_inner);
                assertEquals(entry_inner.getValue(), counter_inner);
            }
            assertEquals(counter_inner, Integer.valueOf(6));
        }
        assertEquals(counter_outer, Integer.valueOf(6));
    }

    @Test
    public void singleEntrySet_remove() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        for (Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, Integer> entry = it.next();
            if (entry.getKey().equals(1)) {
                it.remove();
            }
        }

        assertEquals(map.size(), 0);
    }

    @Test
    public void entrySet_remove() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(4, 4);
        map.put(6, 6);
        map.put(2, 2);
        map.put(3, 3);
        map.put(1, 1);

        // CHECK
        for (Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, Integer> entry = it.next();
            if (entry.getKey() % 2 == 1) {
                it.remove();
            }
        }

        int counter = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            counter++;
            assertEquals(entry.getKey(), (Integer) (counter * 2));
            assertEquals(entry.getValue(), (Integer) (counter * 2));
        }
        assertEquals(counter, 3);
    }

    @Test(expected = ConcurrentModificationException.class)
    public void entrySet_remove_and_modify() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.put(5, 5);
        map.put(6, 6);

        // CHECK
        for (Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, Integer> entry = it.next();
            if (entry.getKey() == 3) {
                map.remove(3);
            }
            if (entry.getKey() % 2 == 1) {
                it.remove();
            }
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void entrySet_get_more() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator();
        it.next();
        it.next();
        it.next();
    }

    @Test
    public void emptyKeySet() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        Set<Integer> keys = map.keySet();
        Iterator<Integer> iterator = keys.iterator();

        // CHECK
        assertEquals(keys.size(), 0);
        assertTrue(keys.isEmpty());
        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            Assert.fail();
        } catch (NoSuchElementException ignore) {
        }
    }

    @Test
    public void singleKeySet() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, -1);

        // CHECK
        Set<Integer> keys = map.keySet();
        Iterator<Integer> iterator = keys.iterator();

        assertEquals(keys.size(), 1);
        assertFalse(keys.isEmpty());
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), Integer.valueOf(1));

        try {
            iterator.next();
            Assert.fail();
        } catch (NoSuchElementException ignore) {
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void keySet() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(4, 4);
        map.put(6, 6);
        map.put(2, 2);
        map.put(3, 3);
        map.put(1, 1);

        // CHECK
        Set<Integer> keys = map.keySet();
        assertEquals(keys.size(), 6);

        Integer counter = 0;
        for (Integer key : map.keySet()) {
            assertEquals(key, ++counter);
        }
        assertEquals(counter, Integer.valueOf(6));
    }

    @Test
    public void keySet_ordered() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.put(5, 5);
        map.put(6, 6);

        // CHECK
        Set<Integer> keys = map.keySet();
        assertEquals(keys.size(), 6);

        Integer counter = 0;
        for (Integer key : map.keySet()) {
            assertEquals(key, ++counter);
        }
        assertEquals(counter, Integer.valueOf(6));
    }

    @Test
    public void keySet_ordered_desc() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(6, 6);
        map.put(5, 5);
        map.put(4, 4);
        map.put(3, 3);
        map.put(2, 2);
        map.put(1, 1);

        // CHECK
        Set<Integer> keys = map.keySet();
        assertEquals(keys.size(), 6);

        Integer counter = 0;
        for (Integer key : map.keySet()) {
            assertEquals(key, ++counter);
        }
        assertEquals(counter, Integer.valueOf(6));
    }

    @Test
    public void keySet_ordered_desc_with_min_root() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(6, 6);
        map.put(5, 5);
        map.put(4, 4);
        map.put(3, 3);
        map.put(2, 2);

        // CHECK
        Set<Integer> keys = map.keySet();
        assertEquals(keys.size(), 6);

        Integer counter = 0;
        for (Integer key : map.keySet()) {
            assertEquals(key, ++counter);
        }
        assertEquals(counter, Integer.valueOf(6));
    }

    @Test
    public void keySet_read_and_put_from_iterator() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, -1);
        map.put(2, -2);

        // CHECK
        Set<Integer> keys = map.keySet();
        assertEquals(keys.size(), 2);

        Iterator<Integer> iterator = keys.iterator();
        assertEquals(iterator.next(), Integer.valueOf(1));

        map.put(3, -3);

        try {
            iterator.next();
            Assert.fail();
        } catch (ConcurrentModificationException ignore) {
        }
    }

    @Test
    public void keySet_double_read_from_iterator() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(4, 4);
        map.put(6, 6);
        map.put(2, 2);
        map.put(3, 3);
        map.put(1, 1);

        // CHECK
        Integer counter_outer = 0;
        for (Integer key_outer : map.keySet()) {
            assertEquals(key_outer, ++counter_outer);
            Integer counter_inner = 0;
            for (Integer key_inner : map.keySet()) {
                assertEquals(key_inner, ++counter_inner);
            }
            assertEquals(counter_inner, Integer.valueOf(6));
        }
        assertEquals(counter_outer, Integer.valueOf(6));
    }

    @Test
    public void singleKeySet_remove_from_iterator() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        for (Iterator<Integer> it = map.keySet().iterator(); it.hasNext(); ) {
            Integer entry = it.next();
            if (entry.equals(1)) {
                it.remove();
            }
        }

        assertEquals(map.size(), 0);
    }

    @Test
    public void keySet_remove_from_iterator() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(4, 4);
        map.put(6, 6);
        map.put(2, 2);
        map.put(3, 3);
        map.put(1, 1);

        // CHECK
        for (Iterator<Integer> it = map.keySet().iterator(); it.hasNext(); ) {
            Integer entry = it.next();
            if (entry % 2 == 1) {
                it.remove();
            }
        }

        Integer counter = 0;
        for (Integer key : map.keySet()) {
            assertEquals(key, (Integer) (++counter * 2));
        }
        assertEquals(counter, Integer.valueOf(3));
    }

    @Test(expected = ConcurrentModificationException.class)
    public void keySet_remove_and_modify_from_iterator() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.put(5, 5);
        map.put(6, 6);

        // CHECK
        for (Iterator<Integer> it = map.keySet().iterator(); it.hasNext(); ) {
            Integer key = it.next();
            if (key == 3) {
                map.remove(3);
            }
            if (key % 2 == 1) {
                it.remove();
            }
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void keySet_get_more_from_iterator() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        Iterator<Integer> it = map.keySet().iterator();
        it.next();
        it.next();
        it.next();
    }

    @Test
    public void keySet_contains() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(2, 2);
        map.put(1, 1);
        map.put(3, 3);

        // CHECK
        Set<Integer> keys = map.keySet();
        Assert.assertTrue(keys.contains(1));
        Assert.assertTrue(keys.contains(2));
        Assert.assertTrue(keys.contains(3));
        Assert.assertFalse(keys.contains(4));
        Assert.assertFalse(keys.contains(0));
    }

    @Test
    public void keySet_contains_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // CHECK
        Set<Integer> keys = map.keySet();
        Assert.assertFalse(keys.contains(1));
        Assert.assertFalse(keys.contains(0));
    }

    @Test
    public void keySet_clear() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        Set<Integer> keys = map.keySet();
        assertTrue(keys.contains(2));
        assertEquals(keys.size(), 4);

        keys.clear();

        assertFalse(keys.contains(2));
        assertEquals(keys.size(), 0);

        assertEquals(map.size(), 0);
        assertFalse(map.containsValue(2));
        assertFalse(map.values().contains(2));
        assertEquals(map.values().size(), 0);
    }

    @Test
    public void keySet_lower() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertEquals(((NavigableSet<Integer>) map.keySet()).lower(3), Integer.valueOf(2));
    }

    @Test
    public void keySet_floor() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);

        map.put(4, 4);

        // CHECK
        assertEquals(((NavigableSet<Integer>) map.keySet()).floor(4), Integer.valueOf(4));
        assertEquals(((NavigableSet<Integer>) map.keySet()).floor(3), Integer.valueOf(2));
    }

    @Test
    public void keySet_ceiling() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);

        map.put(4, 4);

        // CHECK
        assertEquals(((NavigableSet<Integer>) map.keySet()).ceiling(1), Integer.valueOf(1));
        assertEquals(((NavigableSet<Integer>) map.keySet()).ceiling(3), Integer.valueOf(4));
    }

    @Test
    public void keySet_higher() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);

        map.put(4, 4);

        // CHECK
        assertEquals(((NavigableSet<Integer>) map.keySet()).higher(1), Integer.valueOf(2));
        assertEquals(((NavigableSet<Integer>) map.keySet()).higher(3), Integer.valueOf(4));
    }

    @Test
    public void keySet_first() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(2, 2);
        map.put(1, 1);

        // CHECK
        assertEquals(((NavigableSet<Integer>) map.keySet()).first(), Integer.valueOf(1));
    }

    @Test
    public void keySet_last() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(2, 2);
        map.put(1, 1);

        // CHECK
        assertEquals(((NavigableSet<Integer>) map.keySet()).last(), Integer.valueOf(2));
    }

    @Test
    public void keySet_comparator() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        Comparator<? super Integer> comparator = ((NavigableSet<Integer>) map.keySet()).comparator();
        assert comparator != null;
        assertEquals(comparator.compare(1, 2), -1);
        assertEquals(comparator.compare(1, 1), 0);
        assertEquals(comparator.compare(2, 1), 1);
    }

    @Test
    public void keySet_pollFirst() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(3, 3);
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        NavigableSet<Integer> keys = (NavigableSet<Integer>) map.keySet();
        assertEquals(keys.pollFirst(), Integer.valueOf(1));
        Integer counter = 1;
        for (Integer key : map.keySet()) {
            assertEquals(key, ++counter);
        }
        assertEquals(map.size(), 3);
        assertEquals(counter, Integer.valueOf(4));
    }

    @Test
    public void keySet_pollLast() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(3, 3);
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        NavigableSet<Integer> keys = (NavigableSet<Integer>) map.keySet();
        assertEquals(keys.pollLast(), Integer.valueOf(4));
        Integer counter = 0;
        for (Integer key : map.keySet()) {
            assertEquals(key, ++counter);
        }
        assertEquals(map.size(), 3);
        assertEquals(counter, Integer.valueOf(3));
    }

    @Test
    public void keySet_remove() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(3, 3);
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        NavigableSet<Integer> keys = (NavigableSet<Integer>) map.keySet();
        assertTrue(keys.remove(3));
        assertFalse(keys.remove(5));
        assertEquals(keys.size(), 3);
        assertEquals(map.size(), 3);
        assertFalse(map.containsKey(3));
    }

    @Test
    public void firstEntry_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC

        // CHECK
        assertNull(map.firstEntry());
    }

    @Test
    public void firstEntry_single() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        assertEquals(map.firstEntry(), new SplayMap.Entry<Integer, Integer>(1, 1, null));
    }

    @Test
    public void firstEntry() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(3, 3);
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        assertEquals(map.firstEntry(), new SplayMap.Entry<Integer, Integer>(1, 1, null));
    }

    @Test
    public void firstKey_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC

        // CHECK
        assertNull(map.firstKey());
    }

    @Test
    public void firstKey_single() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        assertEquals(map.firstKey(), Integer.valueOf(1));
    }

    @Test
    public void firstKey() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(3, 3);
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        assertEquals(map.firstKey(), Integer.valueOf(1));
    }

    @Test
    public void lastEntry_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC

        // CHECK
        assertNull(map.lastEntry());
    }

    @Test
    public void lastEntry_single() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        assertEquals(map.lastEntry(), new SplayMap.Entry<Integer, Integer>(1, 1, null));
    }

    @Test
    public void lastEntry() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(3, 3);
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        assertEquals(map.lastEntry(), new SplayMap.Entry<Integer, Integer>(4, 4, null));
    }

    @Test
    public void lastEntry_ordered() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertEquals(map.lastEntry(), new SplayMap.Entry<Integer, Integer>(4, 4, null));
    }


    @Test
    public void lastKey_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC

        // CHECK
        assertNull(map.lastKey());
    }

    @Test
    public void lastKey_single() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        assertEquals(map.lastKey(), Integer.valueOf(1));
    }

    @Test
    public void lastKey() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(3, 3);
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        assertEquals(map.lastKey(), Integer.valueOf(4));
    }

    @Test
    public void lastKey_ordered() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertEquals(map.lastKey(), Integer.valueOf(4));
    }

    @Test
    public void containsKey_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC

        // CHECK
        assertFalse(map.containsKey(1));
    }

    @Test
    public void containsKey_single() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        assertTrue(map.containsKey(1));
    }

    @Test
    public void containsKey_none() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(3, 3);
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        assertFalse(map.containsKey(4));
    }

    @Test
    public void containsKey() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(3, 3);
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        assertTrue(map.containsKey(3));
    }

    @Test
    public void containsValue_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC

        // CHECK
        assertFalse(map.containsValue(1));
    }

    @Test
    public void containsValue_single() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        assertTrue(map.containsValue(1));
    }

    @Test
    public void containsValue_none() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(3, 3);
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        assertFalse(map.containsValue(4));
    }

    @Test
    public void containsValue() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(3, 3);
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        assertTrue(map.containsValue(3));
    }

    @Test
    public void containsDoubleValue() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 1);
        map.put(3, 1);
        map.put(1, 1);
        map.put(2, 1);

        // CHECK
        assertTrue(map.containsValue(1));
    }

    @Test
    public void ceilingEntry_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // CHECK
        assertNull(map.ceilingEntry(3));
    }

    @Test
    public void ceilingEntry_none() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertNull(map.ceilingEntry(5));
    }

    @Test
    public void ceilingEntryEq() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertEquals(map.ceilingEntry(3), new SplayMap.Entry<Integer, Integer>(3, 3, null));
    }

    @Test
    public void ceilingEntryBiggest() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);

        map.put(4, 4);

        // CHECK
        assertEquals(map.ceilingEntry(3), new SplayMap.Entry<Integer, Integer>(4, 4, null));
    }

    @Test
    public void ceilingEntry_left() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(4, 4);

        // CHECK
        assertEquals(map.ceilingEntry(3), new SplayMap.Entry<Integer, Integer>(4, 4, null));
    }

    @Test
    public void ceilingKey_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // CHECK
        assertNull(map.ceilingKey(3));
    }

    @Test
    public void ceilingKey_none() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertNull(map.ceilingKey(5));
    }


    @Test
    public void ceilingKeyEq() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertEquals(map.ceilingKey(3), Integer.valueOf(3));
    }

    @Test
    public void ceilingKeyBiggest() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);

        map.put(4, 4);

        // CHECK
        assertEquals(map.ceilingKey(4), Integer.valueOf(4));
    }

    @Test
    public void higherEntry_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // CHECK
        assertNull(map.higherEntry(3));
    }

    @Test
    public void higherEntry_none() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertNull(map.higherEntry(4));
        assertNull(map.higherEntry(5));
    }

    @Test
    public void higherEntryEq() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.put(5, 5);

        // CHECK
        assertEquals(map.higherEntry(3), new SplayMap.Entry<Integer, Integer>(4, 4, null));
    }

    @Test
    public void higherEntryBiggest() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);

        map.put(4, 4);

        // CHECK
        assertEquals(map.higherEntry(3), new SplayMap.Entry<Integer, Integer>(4, 4, null));
    }

    @Test
    public void higherEntryBiggest_has_left() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(3, 3);

        // CHECK
        assertEquals(map.higherEntry(3), new SplayMap.Entry<Integer, Integer>(4, 4, null));
    }

    @Test
    public void higherKey_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // CHECK
        assertNull(map.higherKey(3));
    }

    @Test
    public void higherKey_none() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertNull(map.higherKey(4));
        assertNull(map.higherKey(5));
    }


    @Test
    public void higherKeyEq() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertEquals(map.higherKey(3), Integer.valueOf(4));
    }

    @Test
    public void higherKeyBiggest() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);

        map.put(4, 4);

        // CHECK
        assertEquals(map.higherKey(3), Integer.valueOf(4));
    }

    @Test
    public void pollFirstEntry_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC

        // CHECK
        assertNull(map.pollFirstEntry());
    }

    @Test
    public void pollFirstEntry_single() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        assertEquals(map.pollFirstEntry(), new SplayMap.Entry<Integer, Integer>(1, 1, null));
        assertEquals(map.size(), 0);
        assertNull(map.pollFirstEntry());
    }

    @Test
    public void pollFirstEntry() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(3, 3);
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        assertEquals(map.pollFirstEntry(), new SplayMap.Entry<Integer, Integer>(1, 1, null));
        Integer counter = 1;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            counter++;
            assertEquals(entry.getKey(), counter);
            assertEquals(entry.getValue(), counter);
        }
        assertEquals(map.size(), 3);
        assertEquals(counter, Integer.valueOf(4));
    }

    @Test
    public void pollLastEntry_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC

        // CHECK
        assertNull(map.pollLastEntry());
    }

    @Test
    public void pollLastEntry_single() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        assertEquals(map.pollLastEntry(), new SplayMap.Entry<Integer, Integer>(1, 1, null));
        assertEquals(map.size(), 0);
        assertNull(map.pollLastEntry());
    }

    @Test
    public void pollLastEntry() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(4, 4);
        map.put(3, 3);
        map.put(1, 1);
        map.put(2, 2);

        // CHECK
        assertEquals(map.pollLastEntry(), new SplayMap.Entry<Integer, Integer>(4, 4, null));
        Integer counter = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            counter++;
            assertEquals(entry.getKey(), counter);
            assertEquals(entry.getValue(), counter);
        }
        assertEquals(map.size(), 3);
        assertEquals(counter, Integer.valueOf(3));
    }

    @Test
    public void pollLastEntry_ordered() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        assertEquals(map.pollLastEntry(), new SplayMap.Entry<Integer, Integer>(4, 4, null));
        Integer counter = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            counter++;
            assertEquals(entry.getKey(), counter);
            assertEquals(entry.getValue(), counter);
        }
        assertEquals(map.size(), 3);
        assertEquals(counter, Integer.valueOf(3));
    }

    @Test
    public void emptyValues() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        Collection<Integer> values = map.values();
        Iterator<Integer> iterator = values.iterator();

        // CHECK
        assertEquals(values.size(), 0);
        assertTrue(values.isEmpty());
        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            Assert.fail();
        } catch (NoSuchElementException ignore) {
        }
    }

    @Test
    public void singleValues() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 7);

        // CHECK
        Collection<Integer> values = map.values();
        Iterator<Integer> iterator = values.iterator();

        assertEquals(values.size(), 1);
        assertFalse(values.isEmpty());
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), Integer.valueOf(7));

        try {
            iterator.next();
            Assert.fail();
        } catch (NoSuchElementException ignore) {
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void values() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 2);
        map.put(4, 3);
        map.put(6, 1);
        map.put(2, 5);
        map.put(3, 4);
        map.put(1, 6);

        // CHECK
        Collection<Integer> values = map.values();
        assertEquals(values.size(), 6);

        int counter = 0;
        for (Integer value : map.values()) {
            counter++;
            assertEquals(value, Integer.valueOf(7 - counter));
        }
        assertEquals(counter, 6);
    }

    @Test
    public void values_read_and_put() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 4);
        map.put(2, 5);

        // CHECK
        Collection<Integer> values = map.values();
        assertEquals(values.size(), 2);

        Iterator<Integer> iterator = values.iterator();
        assertEquals(iterator.next(), Integer.valueOf(4));

        map.put(3, 6);

        try {
            iterator.next();
            Assert.fail();
        } catch (ConcurrentModificationException ignore) {
        }
    }

    @Test
    public void values_double_read() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 2);
        map.put(4, 3);
        map.put(6, 1);
        map.put(2, 5);
        map.put(3, 4);
        map.put(1, 6);

        // CHECK
        int counter_outer = 0;
        for (Integer outer : map.values()) {
            counter_outer++;
            assertEquals(outer, Integer.valueOf(7 - counter_outer));

            Integer counter_inner = 0;
            for (Integer inner : map.values()) {
                counter_inner++;
                assertEquals(inner, Integer.valueOf(7 - counter_inner));
            }
            assertEquals(counter_inner, Integer.valueOf(6));
        }
        assertEquals(counter_outer, 6);
    }

    @Test
    public void singleValues_remove() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        for (Iterator<Integer> it = map.values().iterator(); it.hasNext(); ) {
            Integer value = it.next();
            if (value.equals(1)) {
                it.remove();
            }
        }

        assertEquals(map.size(), 0);
    }


    @Test
    public void values_remove() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(4, 4);
        map.put(6, 6);
        map.put(2, 2);
        map.put(3, 3);
        map.put(1, 1);

        // CHECK
        for (Iterator<Integer> it = map.values().iterator(); it.hasNext(); ) {
            Integer value = it.next();
            if (value % 2 == 1) {
                it.remove();
            }
        }

        int counter = 0;
        for (Integer value : map.values()) {
            counter++;
            assertEquals(value, (Integer) (counter * 2));
        }
        assertEquals(counter, 3);
    }

    @Test
    public void values_contains() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(5, 5);
        map.put(4, 4);
        map.put(6, 6);
        map.put(2, 2);
        map.put(1, 1);

        // CHECK
        assertTrue(map.containsValue(2));
        assertTrue(map.values().contains(2));
        assertFalse(map.containsValue(3));
        assertFalse(map.values().contains(3));
    }

    @Test
    public void values_clear() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);

        // CHECK
        Collection<Integer> values = map.values();
        assertTrue(values.contains(2));
        assertEquals(values.size(), 4);

        values.clear();

        assertFalse(values.contains(2));
        assertEquals(values.size(), 0);

        assertEquals(map.size(), 0);

        assertFalse(map.values().contains(2));
        assertEquals(map.values().size(), 0);
        assertTrue(map.values().isEmpty());
    }

    @Test
    public void empty_comparator() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        Comparator<? super Integer> comparator = map.comparator();

        // CHECK
        assertNull(comparator);
    }

    @Test
    public void get_custom_comparator() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return -1;
            }
        });

        // EXEC
        Comparator<? super Integer> comparator = map.comparator();

        // CHECK
        assertEquals(comparator.compare(1, -1), -1);
        assertEquals(comparator.compare(-1, 1), -1);
        assertEquals(comparator.compare(null, null), -1);
    }

    @Test
    public void auto_comparator() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);
        Comparator<? super Integer> comparator = map.comparator();

        // CHECK
        assertEquals(comparator.compare(1, -1), 1);
        assertEquals(comparator.compare(-1, 1), -1);
        try {
            comparator.compare(null, null);
            Assert.fail();
        } catch (NullPointerException ignore) {
        }
    }

    @Test
    public void toStringEntry() {
        // INIT
        Map.Entry<String, Integer> entry = new SplayMap.Entry<String, Integer>("my key", -1, null);

        // CHECK
        assertEquals(entry.toString(), "my key = -1");
    }

    @Test
    public void hashCodeEntry() {
        // INIT
        Map.Entry<String, Integer> entry = new SplayMap.Entry<String, Integer>("my key", -1, null);

        // CHECK
        assertEquals(entry.hashCode(), 1061584404);
    }

    @Test
    public void equalsEntry() {
        // INIT
        Map.Entry<String, Integer> entry = new SplayMap.Entry<String, Integer>("my key", -1, null);

        // CHECK
        assertFalse(entry.equals(null));
        assertFalse(entry.equals(new SplayMap.Entry<String, Integer>("my key", 999, null)));
        assertFalse(entry.equals(new SplayMap.Entry<String, Integer>("not my key", -1, null)));
        assertFalse(entry.equals(new SplayMap.Entry<String, String>("not my key", "-1", null)));
        assertFalse(entry.equals("String"));

        assertTrue(entry.equals(new SplayMap.Entry<String, Integer>("my key", -1, null)));
    }

}
