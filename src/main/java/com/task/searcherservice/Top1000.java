package com.task.searcherservice;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alexander Shakhov
 *
 *         Generified class which must return 1000 elements strickly and wait for collection to be full otherwise.
 */
@Slf4j
public class Top1000<T extends Number> {

    private final PriorityBlockingQueue<T> queue;
    private CountDownLatch count;
    private final ReentrantReadWriteLock.WriteLock lock;
    private final int capacity;

    public Top1000(int capacity) {
        this.capacity = capacity;
        queue = new PriorityBlockingQueue<>(capacity);
        count = new CountDownLatch(capacity);
        lock = new ReentrantReadWriteLock().writeLock();
    }

    /**
     * Would be called in case of incoming element. One incoming element would be passed as argument
     */
    public synchronized void onEvent(final T element) {
        try {
            lock.lock();
            if (queue.size() != capacity) {
                log.info(
                        String.format("Put element to queue:  |%10s| capacity %2d|", element.toString(), queue.size()));
                queue.put(element);
                count.countDown();
            } else {
                getTop();
                count.countDown();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns top (with maximum value) 1000 elements. Could be called anytime.
     */
    public Set<T> getTop() {
        try {
            count.await();
            log.debug("Waiting for collection to be full...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final Set<T> list = new CopyOnWriteArraySet<>();
        queue.drainTo(list);
        log.debug(String.format("Clean up queue: %8s", queue.toString()));
        count = new CountDownLatch(0);
        log.warn(String.format("List is full: %20s", list.toString()));
        return list.stream().sorted().distinct().limit(capacity).collect(Collectors.toUnmodifiableSet());
    }

    public static void main(String[] args) throws InterruptedException {
        final int capacity = 1000;
        final ExecutorService executor = Executors.newCachedThreadPool();

        final Top1000<Integer> top1000 = new Top1000<>(capacity);

        final Runnable producer = () -> {
            while (true) {
                final int producedElement = ThreadLocalRandom.current().nextInt(10000);
                try {
                    // Sleep is for visualizing purposes only.
                    Thread.sleep(500);
                    top1000.onEvent(producedElement);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        };

        final Runnable consumer = () -> {
            try {
                // Sleep is for visualizing purposes only.
                Thread.sleep(500);
                top1000.getTop();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        executor.execute(producer);
        executor.execute(consumer);

        executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        executor.shutdown();
    }
}
