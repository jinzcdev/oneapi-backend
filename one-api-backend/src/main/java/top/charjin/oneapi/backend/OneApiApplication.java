package top.charjin.oneapi.backend;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.charjin.oneapi.clientsdk.OneApiClientAutoConfiguration;

/**
 * 主类（项目启动入口）
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class, OneApiClientAutoConfiguration.class})
@MapperScan("top.charjin.oneapi.backend.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableDubbo
public class OneApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneApiApplication.class, args);
    }

}
