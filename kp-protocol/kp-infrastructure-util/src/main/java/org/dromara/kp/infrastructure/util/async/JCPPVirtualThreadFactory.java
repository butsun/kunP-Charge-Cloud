
package org.dromara.kp.infrastructure.util.async;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class JCPPVirtualThreadFactory implements ThreadFactory {
    private final String namePrefix;
    private final AtomicLong threadNumber = new AtomicLong(1);

    public JCPPVirtualThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        return ThreadUtil.newThread(r, namePrefix + "-" + threadNumber.getAndIncrement());
    }
}
