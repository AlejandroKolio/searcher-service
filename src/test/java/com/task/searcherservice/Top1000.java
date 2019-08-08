package com.task.searcherservice;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
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
    private final int capacity;

    public Top1000(int capacity) {
        this.capacity = capacity;
        queue = new PriorityBlockingQueue<>(capacity);
        count = new CountDownLatch(capacity);
    }

    /**
     * Would be called in case of incoming element. One incoming element would be passed as argument
     */
    public void onEvent(final Integer element) {
        if (queue.size() != capacity) {
            log.info(String.format("Capacity left:  |%10d|\n", queue.size()));
            log.info("------------------------");
            queue.put(element);
            count.countDown();
        } else {
            getTop();
            count.countDown();
        }
    }

    /**
     * Returns top (with maximum value) 1000 elements. Could be called anytime.
     */
    public List<Integer> getTop() {
        try {
            log.info("Not ready yet. Queue is {} size:", count.getCount());
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Now ready...");
        final List<Integer> list = queue.stream()
                .sorted(Comparator.reverseOrder())
                .distinct()
                .limit(capacity)
                .collect(toList());
        queue.drainTo(list);
        count = new CountDownLatch(0);
        log.info("Rest count down: {}", count.getCount());
        log.info("------------------");
        log.info("| Top 1000: {}", list.toString());
        log.info("------------------");
        return list;
    }

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService executor = Executors.newFixedThreadPool(20);
        final int capacity = 10;
        final Top1000 top1000 = new Top1000(capacity);

        final Runnable producer = () -> {
            while (true) {
                final int producedElement = ThreadLocalRandom.current().nextInt(100);
                try {
                    log.info(String.format("Put into queue: |%10d| ", producedElement));
                    Thread.sleep(1000);
                    top1000.onEvent(producedElement);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        };

        final Runnable consumer = () -> {
            try {
                log.info(String.format("Retrieving top %d out of the queue size |%20d|", capacity,
                        top1000.getQueue().size()));
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
