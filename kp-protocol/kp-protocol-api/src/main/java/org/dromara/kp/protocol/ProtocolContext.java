
package org.dromara.kp.protocol;

import io.netty.util.ResourceLeakDetector;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.config.ShardingThreadPool;
import org.dromara.kp.protocol.provider.ProtocolSessionRegistryProvider;
import org.dromara.kp.protocol.provider.ProtocolsConfigProvider;
import org.springframework.stereotype.Component;

/**
 * @author but
 */
@Component
@Getter
@Slf4j
public class ProtocolContext {


    private final ProtocolsConfigProvider protocolsConfigProvider;

    private final ProtocolSessionRegistryProvider protocolSessionRegistryProvider;

//    private final ServiceInfoProvider serviceInfoProvider;
//
//    private final PartitionProvider partitionProvider;
//
//    private final AppQueueFactory appQueueFactory;

    private final ShardingThreadPool shardingThreadPool;

    public ProtocolContext(ProtocolsConfigProvider protocolsConfigProvider,
                           ProtocolSessionRegistryProvider protocolSessionRegistryProvider,
//                           ServiceInfoProvider serviceInfoProvider,
//                           @Autowired(required = false) PartitionProvider partitionProvider,
//                           @Autowired(required = false) AppQueueFactory appQueueFactory,
                           ShardingThreadPool shardingThreadPool) {
        this.protocolsConfigProvider = protocolsConfigProvider;
        this.protocolSessionRegistryProvider = protocolSessionRegistryProvider;
//        this.serviceInfoProvider = serviceInfoProvider;
//        this.partitionProvider = partitionProvider;
//        this.appQueueFactory = appQueueFactory;
        this.shardingThreadPool = shardingThreadPool;
    }

    @PostConstruct
    public void init() {
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
        log.info("Setting resource leak detector level to {}", ResourceLeakDetector.Level.DISABLED);
    }
}
