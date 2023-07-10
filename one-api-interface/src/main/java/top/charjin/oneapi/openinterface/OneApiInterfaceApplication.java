package top.charjin.oneapi.openinterface;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OneApiInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneApiInterfaceApplication.class, args);
    }

}
