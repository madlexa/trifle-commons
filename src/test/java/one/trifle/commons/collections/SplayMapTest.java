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


}
