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
        assertEquals(map.get(10), null);
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
        assertEquals(map.remove(1), null);
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
        assertEquals(map.remove(2), null);
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
        assertEquals(map.remove(4), null);
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
        assertEquals(map.lowerEntry(3), null);
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
        assertEquals(map.lowerEntry(1), null);
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
        assertEquals(map.lowerKey(3), null);
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
        assertEquals(map.lowerKey(1), null);
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
        assertEquals(map.floorEntry(3), null);
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
        assertEquals(map.floorEntry(0), null);
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
        assertEquals(map.floorKey(3), null);
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
        assertEquals(map.floorKey(0), null);
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
        assertEquals(entries.isEmpty(), true);
        assertEquals(iterator.hasNext(), false);
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
        assertEquals(entries.isEmpty(), false);
        assertEquals(iterator.hasNext(), true);
        assertEquals(iterator.next().getKey(), Integer.valueOf(1));

        try {
            iterator.next();
            Assert.fail();
        } catch (NoSuchElementException ignore) {
        }
        assertEquals(iterator.hasNext(), false);
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

        Integer counter = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            counter++;
            assertEquals(entry.getKey(), (Integer) (counter * 2));
            assertEquals(entry.getValue(), (Integer) (counter * 2));
        }
        assertEquals(counter, Integer.valueOf(3));
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
    public void firstEntry_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC

        // CHECK
        assertEquals(map.firstEntry(), null);
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
        assertEquals(map.firstKey(), null);
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
        assertEquals(map.lastEntry(), null);
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
        assertEquals(map.lastKey(), null);
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
        assertEquals(map.containsKey(1), false);
    }

    @Test
    public void containsKey_single() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        assertEquals(map.containsKey(1), true);
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
        assertEquals(map.containsKey(4), false);
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
        assertEquals(map.containsKey(3), true);
    }

    @Test
    public void containsValue_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC

        // CHECK
        assertEquals(map.containsValue(1), false);
    }

    @Test
    public void containsValue_single() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        map.put(1, 1);

        // CHECK
        assertEquals(map.containsValue(1), true);
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
        assertEquals(map.containsValue(4), false);
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
        assertEquals(map.containsValue(3), true);
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
        assertEquals(map.containsValue(1), true);
    }

    @Test
    public void ceilingEntry_empty() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // CHECK
        assertEquals(map.ceilingEntry(3), null);
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
        assertEquals(map.ceilingEntry(5), null);
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
        assertEquals(map.ceilingKey(3), null);
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
        assertEquals(map.ceilingKey(5), null);
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
        assertEquals(map.higherEntry(3), null);
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
        assertEquals(map.higherEntry(4), null);
        assertEquals(map.higherEntry(5), null);
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
        assertEquals(map.higherKey(3), null);
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
        assertEquals(map.higherKey(4), null);
        assertEquals(map.higherKey(5), null);
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
        assertEquals(map.pollFirstEntry(), null);
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
        assertEquals(map.pollFirstEntry(), null);
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
        assertEquals(map.pollLastEntry(), null);
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
        assertEquals(map.pollLastEntry(), null);
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
        assertEquals(values.isEmpty(), true);
        assertEquals(iterator.hasNext(), false);
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
        assertEquals(values.isEmpty(), false);
        assertEquals(iterator.hasNext(), true);
        assertEquals(iterator.next(), Integer.valueOf(7));

        try {
            iterator.next();
            Assert.fail();
        } catch (NoSuchElementException ignore) {
        }
        assertEquals(iterator.hasNext(), false);
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

        Integer counter = 0;
        for (Integer value : map.values()) {
            counter++;
            assertEquals(value, Integer.valueOf(7 - counter));
        }
        assertEquals(counter, Integer.valueOf(6));
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
        Integer counter_outer = 0;
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
        assertEquals(counter_outer, Integer.valueOf(6));
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

        Integer counter = 0;
        for (Integer value : map.values()) {
            counter++;
            assertEquals(value, (Integer) (counter * 2));
        }
        assertEquals(counter, Integer.valueOf(3));
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
        assertTrue(map.values().contains(2));
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
    }

    @Test
    public void empty_comparator() {
        // INIT
        NavigableMap<Integer, Integer> map = new SplayMap<Integer, Integer>();

        // EXEC
        Comparator<? super Integer> comparator = map.comparator();

        // CHECK
        assertEquals(comparator, null);
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
