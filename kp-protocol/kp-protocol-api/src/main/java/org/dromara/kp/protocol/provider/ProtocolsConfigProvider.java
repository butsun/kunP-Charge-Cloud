/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package org.dromara.kp.protocol.provider;


import org.dromara.kp.protocol.cfg.ProtocolCfg;

/**
 * @author baigod
 */
public interface ProtocolsConfigProvider {

    ProtocolCfg loadConfig(String protocol);
}
