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

    @Test
    public void containsValue() {
        // INIT
        Map<Integer, Integer> map = new LruHashMap<Integer, Integer>(5);

        // EXEC
        map.put(1, -1);
        map.put(2, -2);
        map.put(3, -3);
        map.put(4, -4);
        map.put(5, -5);

        // CHECK
        assertTrue(map.containsValue(-1));
        assertTrue(map.containsValue(-1));
        assertTrue(map.containsValue(-2));
        assertTrue(map.containsValue(-3));
        assertTrue(map.containsValue(-4));
        assertTrue(map.containsValue(-5));
        assertFalse(map.containsValue(1));
    }

    @Test
    public void containsKey() {
        // INIT
        Map<Integer, Integer> map = new LruHashMap<Integer, Integer>(4);

        // EXEC
        map.put(1, -1);
        map.put(2, -2);
        map.put(3, -3);
        map.put(4, -4);

        assertTrue(map.containsKey(1));

        map.put(5, -5);

        // CHECK
        assertTrue(map.containsKey(1));
        assertTrue(map.containsKey(3));
        assertTrue(map.containsKey(4));
        assertTrue(map.containsKey(5));

        assertFalse(map.containsKey(2));
    }

    @Test
    public void remove_empty() {
        // INIT
        Map<Integer, Integer> map = new LruHashMap<Integer, Integer>(0);

        // EXEC
        map.put(1, -1);
        map.put(2, -2);

        // CHECK
        assertEquals(map.remove(1), null);
        assertEquals(map.remove(2), null);
    }

    @Test
    public void remove() {
        // INIT
        Map<Integer, Integer> map = new LruHashMap<Integer, Integer>(5);

        // EXEC
        map.put(1, -1);
        map.put(2, -2);
        map.put(3, -3);
        map.put(4, -4);

        // CHECK
        assertEquals(map.remove(5), null);
        assertEquals(map.size(), 4);
        assertEquals(map.remove(1), Integer.valueOf(-1));
        assertEquals(map.size(), 3);
        assertEquals(map.remove(2), Integer.valueOf(-2));
        assertEquals(map.size(), 2);
        assertEquals(map.remove(3), Integer.valueOf(-3));
        assertEquals(map.size(), 1);
        assertEquals(map.remove(4), Integer.valueOf(-4));
        assertEquals(map.size(), 0);
    }

    @Test
    public void remove_from_one_backed() {
        // INIT
        Map<MyClass, Integer> map = new LruHashMap<MyClass, Integer>(5);

        // EXEC
        map.put(new MyClass(1, 5), -1);
        map.put(new MyClass(2, 5), -2);
        map.put(new MyClass(3, 5), -3);
        map.put(new MyClass(4, 5), -4);

        // CHECK
        assertEquals(map.remove(new MyClass(5, 5)), null);
        assertEquals(map.size(), 4);
        assertEquals(map.remove(new MyClass(4, 5)), Integer.valueOf(-4));
        assertEquals(map.size(), 3);
        assertEquals(map.remove(new MyClass(3, 5)), Integer.valueOf(-3));
        assertEquals(map.size(), 2);
        assertEquals(map.remove(new MyClass(2, 5)), Integer.valueOf(-2));
        assertEquals(map.size(), 1);
        assertEquals(map.remove(new MyClass(1, 5)), Integer.valueOf(-1));
        assertEquals(map.size(), 0);
    }

    private static class MyClass {
        private final int val;
        private final int hash;

        private MyClass(int val, int hash) {
            this.val = val;
            this.hash = hash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyClass myClass = (MyClass) o;
            return val == myClass.val;
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }
}