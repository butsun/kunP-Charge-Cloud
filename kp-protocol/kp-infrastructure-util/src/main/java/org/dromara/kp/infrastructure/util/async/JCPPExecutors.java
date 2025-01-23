
package org.dromara.kp.infrastructure.util.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class JCPPExecutors {

    public static ExecutorService newWorkStealingPool(int parallelism, String namePrefix) {
        return new ForkJoinPool(parallelism,
            new JCPPForkJoinWorkerThreadFactory(namePrefix),
            null, true);
    }

    public static ExecutorService newWorkStealingPool(int parallelism, Class<?> clazz) {
        return newWorkStealingPool(parallelism, clazz.getSimpleName());
    }

    public static ExecutorService newFixedThreadPool(String namePrefix) {
        return Executors.newFixedThreadPool(4, new JCPPVirtualThreadFactory(namePrefix));
    }

}
