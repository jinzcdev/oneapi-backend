package top.charjin.oneapi.gateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class OneApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneApiGatewayApplication.class, args);
    }

}
