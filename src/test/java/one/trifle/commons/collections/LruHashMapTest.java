package one.trifle.commons.collections;

import org.junit.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class LruHashMapTest {
    @Test
    public void createEmptyMap() {
        new LruHashMap<Integer, Integer>(0);
    }

    @Test
    public void simple_put() {
        // INIT
        Map<Integer, Integer> map = new LruHashMap<Integer, Integer>(2);

        // EXEC
        map.put(1, 1);
        map.put(1, 1);
        map.put(4, 1);
        assertEquals(map.size(), 2);
    }

    @Test
    public void string_put() {
        // INIT
        Map<String, Integer> map = new LruHashMap<String, Integer>(2);

        // EXEC
        map.put("1", 1);
        map.put(new String("1"), 1);
        map.put("4", 1);
        assertEquals(map.size(), 2);
    }

    @Test
    public void simple_get() {
        // INIT
        Map<Integer, Integer> map = new LruHashMap<Integer, Integer>(2);

        // EXEC
        map.put(1, 1);
        map.put(4, 4);

        // CHECK
        assertEquals(map.get(1), Integer.valueOf(1));
        assertEquals(map.get(4), Integer.valueOf(4));
        assertEquals(map.get(3), null);
        assertEquals(map.size(), 2);
    }

    @Test
    public void remove_old() {
        // INIT
        Map<Integer, Integer> map = new LruHashMap<Integer, Integer>(2);

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);


        // CHECK
        assertEquals(map.get(1), null);
        assertEquals(map.get(2), null);
        assertEquals(map.get(3), Integer.valueOf(3));
        assertEquals(map.get(4), Integer.valueOf(4));
    }

    @Test
    public void remove_old_and_up_read() {
        // INIT
        Map<Integer, Integer> map = new LruHashMap<Integer, Integer>(2);

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.get(1);

        map.put(3, 3);
        map.get(1);

        map.put(4, 4);
        map.get(1);

        map.put(5, 5);

        // CHECK
        assertEquals(map.get(1), Integer.valueOf(1));
        assertEquals(map.get(2), null);
        assertEquals(map.get(3), null);
        assertEquals(map.get(4), null);
        assertEquals(map.get(5), Integer.valueOf(5));
    }

    @Test
    public void zero_size() {
        // INIT
        Map<Integer, Integer> map = new LruHashMap<Integer, Integer>(0);

        // EXEC
        map.put(1, 1);

        // CHECK
        assertEquals(map.get(1), null);
        assertEquals(map.size(), 0);
    }

    @Test
    public void emptyEntrySet() {
        // INIT
        Map<Integer, Integer> map = new LruHashMap<Integer, Integer>(1);

        // EXEC
        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();

        // CHECK
        assertEquals(entries.size(), 0);
        assertFalse(entries.iterator().hasNext());
    }

    @Test
    public void singleEntrySet() {
        // INIT
        Map<Integer, Integer> map = new LruHashMap<Integer, Integer>(1);

        // EXEC
        map.put(1, -1);
        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        Iterator<Map.Entry<Integer, Integer>> iterator = entries.iterator();

        // CHECK
        assertEquals(entries.size(), 1);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().getKey(), Integer.valueOf(1));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void entrySet_with_change_value() {
        // INIT
        Map<Integer, Integer> map = new LruHashMap<Integer, Integer>(5);

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.put(5, 5);
        map.put(6, 6);
        map.put(7, 7);


        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();

        // CHECK
        Integer counter = 2;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            counter++;
            assertEquals(entry.getKey(), counter);
            assertEquals(entry.getValue(), counter);
            entry.setValue(counter + 1);
        }
        assertEquals(entries.size(), 5);
        assertEquals(counter, Integer.valueOf(7));
        assertEquals(map.get(5), Integer.valueOf(6));
    }

    @Test
    public void entrySet_with_remove() {
        // INIT
        Map<Integer, Integer> map = new LruHashMap<Integer, Integer>(5);

        // EXEC
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.put(5, 5);
        map.put(6, 6);
        map.put(7, 7);


        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        Iterator<Map.Entry<Integer, Integer>> iterator = entries.iterator();

        // CHECK
        Integer counter = 2;
        while (iterator.hasNext()) {
            counter++;
            Map.Entry<Integer, Integer> entry = iterator.next();
            assertEquals(entry.getKey(), counter);
            assertEquals(entry.getValue(), counter);
            if (counter % 2 == 0) {
                iterator.remove();
            }
        }
        assertEquals(entries.size(), 3);
        assertEquals(counter, Integer.valueOf(7));
        assertEquals(map.get(5), Integer.valueOf(5));
        assertEquals(map.get(4), null);
        assertEquals(map.get(6), null);
    }
}