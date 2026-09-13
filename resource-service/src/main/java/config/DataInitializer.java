package config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dao.ResourceRepository;
import entities.Resource;
import entities.ResourceStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final ResourceRepository resourceRepository;

    @Bean
    CommandLineRunner initResources() {
        return args -> {
            if (resourceRepository.count() > 0) {
                log.info("✅ {} ressources déjà en base. Initialisation ignorée.",
                        resourceRepository.count());
                return;
            }

            log.info("🌱 Initialisation des ressources CODE 212...");

            List<Resource> resources = List.of(
                    build("Arduino Uno R3", "Carte microcontrôleur ATmega328P", "IoT", 15),
                    build("Arduino Mega 2560", "Carte microcontrôleur ATmega2560", "IoT", 8),
                    build("Raspberry Pi 4 (4GB)", "Mini-ordinateur ARM Cortex-A72", "IoT", 10),
                    build("Raspberry Pi 5 (8GB)", "Mini-ordinateur nouvelle génération", "IoT", 5),
                    build("ESP32 DevKit", "Module WiFi + Bluetooth", "IoT", 20),
                    build("ESP8266 NodeMCU", "Module WiFi bas coût", "IoT", 18),
                    build("Capteur DHT22", "Capteur température/humidité", "IoT", 25),
                    build("Capteur ultrason HC-SR04", "Mesure de distance", "IoT", 30),
                    build("GPU NVIDIA RTX 4090", "GPU pour entraînement IA", "IA", 2),
                    build("GPU NVIDIA RTX 3090", "GPU pour deep learning", "IA", 3),
                    build("Jetson Nano", "Carte IA embarquée NVIDIA", "IA", 4),
                    build("Salle Formation A", "Salle 30 places avec projecteur", "Salle", 1),
                    build("Salle Formation B", "Salle 20 places avec tableau interactif", "Salle", 1),
                    build("Labo IA", "Laboratoire équipé GPU et stations", "Salle", 1),
                    build("Salle Hackathon", "Grande salle modulable 50 places", "Salle", 1),
                    build("Licence MATLAB", "Licence MATLAB + Simulink", "Logiciel", 10),
                    build("Licence JetBrains", "Suite IntelliJ / PyCharm / WebStorm", "Logiciel", 20),
                    buildMaintenance("Oscilloscope Tektronix", "Oscilloscope numérique 2 voies", "IoT", 3),
                    buildMaintenance("Imprimante 3D Prusa", "Imprimante 3D FDM", "IoT", 2)
            );

            resourceRepository.saveAll(resources);
            log.info("✅ {} ressources insérées avec succès.", resources.size());
        };
    }

    private Resource build(String name, String desc, String cat, int qty) {
        return Resource.builder()
                .name(name).description(desc).category(cat)
                .quantityTotal(qty).quantityAvailable(qty)
                .status(ResourceStatus.AVAILABLE).build();
    }

    private Resource buildMaintenance(String name, String desc, String cat, int qty) {
        return Resource.builder()
                .name(name).description(desc).category(cat)
                .quantityTotal(qty).quantityAvailable(0)
                .status(ResourceStatus.MAINTENANCE).build();
    }
}