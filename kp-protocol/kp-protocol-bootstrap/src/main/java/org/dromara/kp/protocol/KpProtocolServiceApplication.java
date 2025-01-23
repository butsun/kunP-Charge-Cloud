package org.dromara.kp.protocol;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 系统模块
 *
 * @author ruoyi
 */
@EnableDubbo
@SpringBootApplication(scanBasePackages = {"org.dromara.kp.protocol",
    "org.dromara.kp.infrastructure.util"})
public class KpProtocolServiceApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(KpProtocolServiceApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Protocol启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
