
package org.dromara.kp.protocol.provider.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.kp.infrastructure.util.async.JCPPThreadFactory;
import org.dromara.kp.infrastructure.util.config.ThreadPoolConfiguration;
import org.dromara.kp.protocol.domain.ProtocolSession;
import org.dromara.kp.protocol.domain.SessionCloseReason;
import org.dromara.kp.protocol.forwarder.Forwarder;
import org.dromara.kp.protocol.provider.ProtocolSessionRegistryProvider;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.PileLostEvent;
import org.dromara.kp.protocol.yunkuaichong.domain.dto.UplinkQueueMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author but
 */
@Service
@Slf4j
public class DefaultProtocolSessionRegistryProvider implements ProtocolSessionRegistryProvider {
    private static final int INIT_CACHE_LIMIT = 100_000;
    private static final int MAXIMUM_SIZE = 1_000_000;

    @Value("${service.protocol.sessions.default-inactivity-timeout-in-sec}")
    private int defaultInactivityTimeoutInSec;

    @Value("${service.protocol.sessions.default-state-check-interval-in-sec}")
    private int defaultStateCheckIntervalInSec;

    @Getter
    private final Cache<UUID, ProtocolSession> sessionCache = buildCache();

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(JCPPThreadFactory.forName("session-state-checker"));


    @Resource
    protected Forwarder forwarder;

    @PostConstruct
    public void init() {
        scheduledExecutorService.scheduleAtFixedRate(() -> sessionCache.asMap().forEach((id, session) -> {
            if (session.getLastActivityTime().isBefore(LocalDateTime.now().minusSeconds(defaultInactivityTimeoutInSec))) {
                session.close(SessionCloseReason.INACTIVE);
                sendUnregister(session.getPileCodeSet());
                unregister(session.getId());
            }
        }), defaultStateCheckIntervalInSec, defaultStateCheckIntervalInSec, TimeUnit.SECONDS);
    }

    private void sendUnregister(Set<String> pileCodeSet) {
        forwarder.sendMessage(UplinkQueueMessage.builder()
            .pileLostEvent(PileLostEvent.builder().pileCode(pileCodeSet).build())
            .build());
    }

    @PreDestroy
    public void destroy() {
        scheduledExecutorService.shutdownNow();
    }

    @Override
    public void register(ProtocolSession protocolSession) {

        if (log.isDebugEnabled()) {
            log.debug("Registering session {}", protocolSession);
        }

        sessionCache.put(protocolSession.getId(), protocolSession);

    }

    @Override
    public void unregister(UUID sessionId) {

        log.info("Unregistering session {}", sessionId);
        sessionCache.invalidate(sessionId);
    }

    @Override
    public ProtocolSession get(UUID sessionId) {

        log.debug("Get session {}", sessionId);

        return sessionCache.get(sessionId, uuid -> null);
    }

    @Override
    public void activate(ProtocolSession protocolSession) {

        if (log.isDebugEnabled()) {
            log.debug("Activating session {}", protocolSession);
        }

        protocolSession.setLastActivityTime(LocalDateTime.now());

        sessionCache.put(protocolSession.getId(), protocolSession);
    }

    private Cache<UUID, ProtocolSession> buildCache() {
        return Caffeine.newBuilder()
            .initialCapacity(INIT_CACHE_LIMIT)
            .maximumSize(MAXIMUM_SIZE)
            .executor(ThreadPoolConfiguration.JCPP_COMMON_THREAD_POOL)
            .build();
    }
}
