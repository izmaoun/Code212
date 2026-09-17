package net.amine.reservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        exclude = {
                SecurityAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class   // ← 🆕 AJOUTER
        }
)
@EnableFeignClients(basePackages = "clients")
@EnableJpaRepositories(basePackages = "dao")
@EntityScan(basePackages = "entities")
@ComponentScan(basePackages = {
        "net.amine.reservationservice",
        "config",
        "security",
        "entities",
        "dao",
        "services",
        "web",
        "clients",
        "dto",
        "mapper",
        "exceptions"
})
public class ReservationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }
}