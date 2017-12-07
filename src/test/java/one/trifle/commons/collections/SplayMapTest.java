package one.trifle.commons.collections;

import org.junit.Test;

import java.util.Comparator;
import java.util.Map;

import static org.junit.Assert.assertEquals;

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
    public void castomComporator() {
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

}
