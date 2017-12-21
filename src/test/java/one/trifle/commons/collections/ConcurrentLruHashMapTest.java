package one.trifle.commons.collections;

import org.junit.Test;

import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;

public class ConcurrentLruHashMapTest {
    final static int threads = Runtime.getRuntime().availableProcessors() * 2;
    final static ExecutorService pool = Executors.newFixedThreadPool(threads);

    @Test
    public void init() {
        new ConcurrentLruHashMap<Integer, Integer>(10);
    }

    @Test
    public void put() {
        // INIT
        ConcurrentMap<MyClass<Integer>, Integer> map = new ConcurrentLruHashMap<MyClass<Integer>, Integer>(10);

        // CHECK
        assertEquals(map.size(), 0);
        assertEquals(map.put(new MyClass<Integer>(1, 1), 1), null);
        assertEquals(map.size(), 1);
        assertEquals(map.put(new MyClass<Integer>(1, 1), 2), Integer.valueOf(1));
        assertEquals(map.size(), 1);
        assertEquals(map.put(new MyClass<Integer>(2, 1), 3), null);
        assertEquals(map.size(), 2);
        assertEquals(map.put(new MyClass<Integer>(2, 1), 4), Integer.valueOf(3));
        assertEquals(map.size(), 2);
        assertEquals(map.put(new MyClass<Integer>(2, 1), 5), Integer.valueOf(4));
        assertEquals(map.size(), 2);
        assertEquals(map.put(new MyClass<Integer>(3, 3), 6), null);
        assertEquals(map.size(), 3);
    }

    @Test
    public void putIfAbsent() {
        // INIT
        ConcurrentMap<MyClass<Integer>, Integer> map = new ConcurrentLruHashMap<MyClass<Integer>, Integer>(10);

        // CHECK
        assertEquals(map.size(), 0);
        assertEquals(map.putIfAbsent(new MyClass<Integer>(1, 1), 1), null);
        assertEquals(map.size(), 1);
        assertEquals(map.putIfAbsent(new MyClass<Integer>(1, 1), 2), Integer.valueOf(1));
        assertEquals(map.size(), 1);
        assertEquals(map.putIfAbsent(new MyClass<Integer>(2, 1), 3), null);
        assertEquals(map.size(), 2);
        assertEquals(map.putIfAbsent(new MyClass<Integer>(2, 1), 4), Integer.valueOf(3));
        assertEquals(map.size(), 2);
        assertEquals(map.putIfAbsent(new MyClass<Integer>(2, 1), 5), Integer.valueOf(3));
        assertEquals(map.size(), 2);
        assertEquals(map.putIfAbsent(new MyClass<Integer>(3, 3), 6), null);
        assertEquals(map.size(), 3);
    }

    @Test
    public void get() {
        // INIT
        ConcurrentMap<MyClass<Integer>, Integer> map = new ConcurrentLruHashMap<MyClass<Integer>, Integer>(10);

        // EXEC
        map.put(new MyClass<Integer>(1, 1), 1);
        map.put(new MyClass<Integer>(2, 1), 2);
        map.put(new MyClass<Integer>(3, 1), 3);
        map.put(new MyClass<Integer>(4, 4), 4);

        // CHECK
        assertEquals(map.get(new MyClass<Integer>(1, 1)), Integer.valueOf(1));
        assertEquals(map.get(new MyClass<Integer>(2, 1)), Integer.valueOf(2));
        assertEquals(map.get(new MyClass<Integer>(3, 1)), Integer.valueOf(3));
        assertEquals(map.get(new MyClass<Integer>(4, 4)), Integer.valueOf(4));
        assertEquals(map.get(new MyClass<Integer>(4, 1)), null);
        assertEquals(map.get(new MyClass<Integer>(5, 1)), null);
        assertEquals(map.get(new MyClass<Integer>(5, 2)), null);
        assertEquals(map.size(), 4);
    }

    @Test
    public void remove() {
        // INIT
        ConcurrentMap<MyClass<Integer>, Integer> map = new ConcurrentLruHashMap<MyClass<Integer>, Integer>(10);

        // EXEC
        map.put(new MyClass<Integer>(1, 1), 1);
        map.put(new MyClass<Integer>(2, 1), 2);
        map.put(new MyClass<Integer>(3, 1), 3);
        map.put(new MyClass<Integer>(4, 4), 4);

        // CHECK
        assertEquals(map.size(), 4);
        assertEquals(map.get(new MyClass<Integer>(5, 1)), null);
        assertEquals(map.size(), 4);
        assertEquals(map.remove(new MyClass<Integer>(1, 1)), Integer.valueOf(1));
        assertEquals(map.get(new MyClass<Integer>(1, 1)), null);
        assertEquals(map.size(), 3);
        assertEquals(map.remove(new MyClass<Integer>(4, 4)), Integer.valueOf(4));
        assertEquals(map.get(new MyClass<Integer>(4, 1)), null);
        assertEquals(map.size(), 2);
        assertEquals(map.remove(new MyClass<Integer>(3, 1)), Integer.valueOf(3));
        assertEquals(map.get(new MyClass<Integer>(3, 1)), null);
        assertEquals(map.size(), 1);
        assertEquals(map.remove(new MyClass<Integer>(2, 1)), Integer.valueOf(2));
        assertEquals(map.get(new MyClass<Integer>(2, 1)), null);
        assertEquals(map.size(), 0);
    }

    @Test
    public void concurrent_put_another_backed() throws InterruptedException, ExecutionException {
        // INIT
        final int iterations = 10;
        Future[] runners = new Future[threads];
        final CyclicBarrier barrier = new CyclicBarrier(threads);
        final ConcurrentMap<MyClass<String>, Integer> map = new ConcurrentLruHashMap<MyClass<String>, Integer>(threads * iterations);

        // EXEC
        for (int i = 0; i < threads; i++) {
            final int thread = i;
            runners[i] = pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int iteration = 0; iteration < iterations; iteration++) {
                            barrier.await();
                            map.put(new MyClass<String>(iteration + "_" + thread, thread), iteration + thread);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (BrokenBarrierException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        for (int i = 0; i < threads; i++) {
            runners[i].get();
        }

        // CHECK
        assertEquals(map.size(), threads * iterations);
        for (int thread = 0; thread < threads; thread++) {
            for (int iteration = 0; iteration < iterations; iteration++) {
                assertEquals(map.get(new MyClass<String>(iteration + "_" + thread, thread)), Integer.valueOf(iteration + thread));
            }
        }
    }

    @Test
    public void concurrent_put_one_backed() throws InterruptedException, ExecutionException {
        // INIT
        final int iterations = 10;
        Future[] runners = new Future[threads];
        final CyclicBarrier barrier = new CyclicBarrier(threads);
        final ConcurrentMap<MyClass<String>, Integer> map = new ConcurrentLruHashMap<MyClass<String>, Integer>(threads * iterations);

        // EXEC
        for (int i = 0; i < threads; i++) {
            final int thread = i;
            runners[i] = pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int iteration = 0; iteration < iterations; iteration++) {
                            barrier.await();
                            map.put(new MyClass<String>(iteration + "_" + thread, iteration), iteration + thread);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (BrokenBarrierException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        for (int i = 0; i < threads; i++) {
            runners[i].get();
        }

        // CHECK
        assertEquals(map.size(), threads * iterations);
        for (int thread = 0; thread < threads; thread++) {
            for (int iteration = 0; iteration < iterations; iteration++) {
                assertEquals(map.get(new MyClass<String>(iteration + "_" + thread, iteration)), Integer.valueOf(iteration + thread));
            }
        }
    }

    @Test
    public void put_one_backed_and_concurrent_remove() throws InterruptedException, ExecutionException {
        // INIT
        final int iterations = 10;
        Future[] runners = new Future[threads];
        final CyclicBarrier barrier = new CyclicBarrier(threads);

        final ConcurrentMap<MyClass<String>, Integer> map = new ConcurrentLruHashMap<MyClass<String>, Integer>(threads);

        // EXEC
        // add
        for (int thread = 0; thread < threads; thread++) {
            for (int iteration = 0; iteration < iterations; iteration++) {
                map.put(new MyClass<String>(iteration + "_" + thread, 1), iteration + thread);
            }
        }
        // remove
        for (int i = 0; i < threads; i++) {
            final int thread = i;
            runners[i] = pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int iteration = 0; iteration < iterations; iteration++) {
                            barrier.await();
                            map.remove(new MyClass<String>((iteration) + "_" + thread, 1));
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (BrokenBarrierException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        for (int i = 0; i < threads; i++) {
            runners[i].get();
        }

        // CHECK
        assertEquals(map.size(), 0);
        for (int thread = 0; thread < threads; thread++) {
            for (int iteration = 0; iteration < iterations; iteration++) {
                assertEquals("don`t removed thread: " + thread + " iteration: " + iteration, map.get(new MyClass<String>(iteration + "_" + thread, 1)), null);
            }
        }
    }

    @Test
    public void concurrent_put_one_backed_and_remove() throws InterruptedException, ExecutionException {
        // INIT
        final int iterations = 10;
        Future[] runners = new Future[threads];
        final CyclicBarrier barrier = new CyclicBarrier(threads);

        final ConcurrentMap<MyClass<String>, Integer> map = new ConcurrentLruHashMap<MyClass<String>, Integer>(threads);

        // EXEC
        // Added and remove
        for (int i = 0; i < threads; i++) {
            final int thread = i;
            runners[i] = pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int iteration = 0; iteration < iterations; iteration++) {
                            if (thread % 2 == 0) {
                                barrier.await();
                                map.put(new MyClass<String>(iteration + "_" + thread, 1), iteration + thread);
                                map.remove(new MyClass<String>((iteration - 1) + "_" + thread, 1));
                            } else {
                                barrier.await();
                                map.remove(new MyClass<String>((iteration - 1) + "_" + thread, 1));
                                map.put(new MyClass<String>(iteration + "_" + thread, 1), iteration + thread);
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (BrokenBarrierException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        for (int i = 0; i < threads; i++) {
            runners[i].get();
        }

        // CHECK
        assertEquals(map.size(), threads);
        for (int thread = 0; thread < threads; thread++) {
            for (int iteration = 0; iteration < iterations - 1; iteration++) {
                assertEquals("don`t removed thread: " + thread + " iteration: " + iteration, map.get(new MyClass<String>(iteration + "_" + thread, 1)), null);
            }
        }
        for (int thread = 0; thread < threads; thread++) {
            assertEquals("removed thread: " + thread + " iteration: " + (iterations - 1), map.get(new MyClass<String>((iterations - 1) + "_" + thread, 1)), Integer.valueOf(iterations - 1 + thread));
        }
    }

    //    @Test
    public void concurrent_put_one_backed_and_remove_oldest() throws InterruptedException, ExecutionException {
        // INIT
        final int iterations = 10;
        Future[] runners = new Future[threads];
        final CyclicBarrier barrier = new CyclicBarrier(threads);
        final ConcurrentMap<MyClass<String>, Integer> map = new ConcurrentLruHashMap<MyClass<String>, Integer>(threads);

        // EXEC
        for (int i = 0; i < threads; i++) {
            final int thread = i;
            runners[i] = pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int iteration = 0; iteration < iterations; iteration++) {
                            barrier.await();
                            map.put(new MyClass<String>(iteration + "_" + thread, iteration), iteration + thread);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (BrokenBarrierException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        for (int i = 0; i < threads; i++) {
            runners[i].get();
        }

        // CHECK
        assertEquals(map.size(), threads);
        for (int thread = 0; thread < threads; thread++) {
            for (int iteration = 0; iteration < iterations - 1; iteration++) {
                assertEquals(map.get(new MyClass<String>(iteration + "_" + thread, iteration)), null);
            }
        }
        for (int thread = 0; thread < threads - 1; thread++) {
            assertEquals(map.get(new MyClass<String>((iterations - 1) + "_" + thread, iterations - 1)), Integer.valueOf(iterations - 1 + thread));
        }
    }


    private static class MyClass<T> {
        private final T val;
        private final int hash;

        private MyClass(T val, int hash) {
            this.val = val;
            this.hash = hash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return val.equals(((MyClass) o).val);
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

}