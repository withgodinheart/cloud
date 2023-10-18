package top.desq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UsersSrvApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersSrvApplication.class, args);
    }

}
