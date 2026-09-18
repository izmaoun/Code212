package net.amine.resourceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
                "net.amine.resourceservice",
                "config",
                "dao",
                "dto",
                "entities",
                "exceptions",
                "mapper",
                "services",
                "web"
        },
        exclude = {
                SecurityAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class   // ← 🆕 AJOUTER
        }
)
@EnableJpaRepositories(basePackages = {"dao"})
@EntityScan(basePackages = {"entities"})
@EnableDiscoveryClient    // ← 🆕 AJOUTER

public class ResourceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServiceApplication.class, args);
    }
}