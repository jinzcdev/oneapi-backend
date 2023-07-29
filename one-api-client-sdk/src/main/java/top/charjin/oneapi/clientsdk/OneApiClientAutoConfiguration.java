package top.charjin.oneapi.clientsdk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.charjin.oneapi.clientsdk.client.OneApiClient;

@Data
@Configuration
@ComponentScan
@ConfigurationProperties("oneapi.client")
public class OneApiClientAutoConfiguration {
    private String accessKey;
    private String secretKey;

    @Bean
    public OneApiClient oneApiClient() {
        return new OneApiClient(new Credential(accessKey, secretKey));
    }
}
