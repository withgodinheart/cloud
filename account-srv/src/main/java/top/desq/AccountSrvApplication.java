package top.desq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AccountSrvApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountSrvApplication.class, args);
    }

}