package com.task.searcherservice;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alexander Shakhov
 */
@Slf4j
public class Top1000 {

    //done: 1 must return 1000 elements strickly wait otherwise.
    //todo: generify

    @Getter
    private final PriorityBlockingQueue<Integer> queue;
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
    public synchronized void onEvent(final Integer element) {
        try {
            lock.lock();
            if (queue.size() != capacity) {
                log.info(String.format("Put element to queue:  |%10d| capacity %2d|", element, queue.size()));
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
    public List<Integer> getTop() {
        try {
            count.await();
            log.debug("Waiting for collection to be full...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        queue.drainTo(list);
        log.debug(String.format("Clean up queue     \t%s", queue.toString()));
        count = new CountDownLatch(0);
        log.warn(String.format("List is full:       \t%s", list.toString()));
        return list.stream()
                .sorted(Comparator.naturalOrder())
                .distinct()
                .limit(capacity)
                .collect(Collectors.toUnmodifiableList());
    }

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService executor = Executors.newFixedThreadPool(10);
        final int capacity = 10;
        final Top1000 top1000 = new Top1000(capacity);

        final Runnable producer = () -> {
            while (true) {
                final int producedElement = ThreadLocalRandom.current().nextInt(100);
                try {
                    Thread.sleep(1000);
                    top1000.onEvent(producedElement);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        };

        final Runnable consumer = () -> {
            try {
                top1000.getTop();
                Thread.sleep(1000);
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
