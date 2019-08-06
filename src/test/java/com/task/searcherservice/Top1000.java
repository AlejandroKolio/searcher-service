package com.task.searcherservice;

import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Alexander Shakhov
 */
@Slf4j
public class Top1000<T> {

    //todo: 1 must return 1000 elements strickly wait otherwise.
    //todo: generify

    private final TreeSet<Integer> elements = new TreeSet<>();

    private final PriorityQueue<T> queue = new PriorityQueue<>();

    private final AtomicInteger counter = new AtomicInteger();

    /**
     * Would be called in case of incoming element. One incoming element would be passed as argument
     */
    public <T> void onEvent(T element) {
/*        if (queue.size() < 1000) {
            queue.add(element);
            counter.incrementAndGet();
        } else if (1000 == queue.size()) {
            queue.remove(queue.size() - 1);
            queue.add(element);
        }*/
    }

    /**
     * Returns top (with maximum value) 1000 elements. Could be called anytime.
     */
    public PriorityQueue<T> getTop() {
        if (1000 == queue.size()) {
            return queue;
        } else {
            return null;
        }
    }

    @Test
    public void elementsAddTest() {
        final Top1000 top1000 = new Top1000();
        Stream.iterate(0, n -> n + 1).limit(2000).forEach(x -> top1000.onEvent(x));
        log.info("{}", top1000.getTop());
    }
}
