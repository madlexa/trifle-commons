package one.trifle.commons.collections;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

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
}