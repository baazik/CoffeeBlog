package cz.hocuspocus.coffeeblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
public class CoffeeBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeBlogApplication.class, args);
    }
}

