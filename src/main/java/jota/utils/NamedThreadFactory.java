package jota.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Adrian
 */
public class NamedThreadFactory implements ThreadFactory {

    private final String baseName;
    private final AtomicInteger threadNum = new AtomicInteger(0);

    /**
     * @param baseName
     */
    public NamedThreadFactory(String baseName) {
        this.baseName = baseName;
    }

    /**
     * @param r
     * @return
     */
    @Override
    public synchronized Thread newThread(Runnable r) {
        Thread t = Executors.defaultThreadFactory().newThread(r);

        t.setName(baseName + "-" + threadNum.getAndIncrement());

        return t;
    }
}