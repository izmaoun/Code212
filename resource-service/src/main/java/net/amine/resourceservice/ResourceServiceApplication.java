package net.amine.resourceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "net.amine.resourceservice",
        "config",
        "dao",
        "dto",
        "entities",
        "exceptions",
        "mapper",
        "services",
        "web"
})
@EnableJpaRepositories(basePackages = {"dao"})
@EntityScan(basePackages = {"entities"})
public class ResourceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServiceApplication.class, args);
    }

}
