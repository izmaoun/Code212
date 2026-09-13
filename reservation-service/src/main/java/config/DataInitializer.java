package config;

import dao.ReservationRepository;
import entities.Reservation;
import entities.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final ReservationRepository reservationRepository;

    @Bean
    public CommandLineRunner initReservations() {
        return args -> {
            if (reservationRepository.count() > 0) {
                log.info("Reservations already initialized");
                return;
            }

            Reservation reservation = Reservation.builder()
                    .resourceId(1L)
                    .userId(101L)
                    .quantity(2)
                    .status(ReservationStatus.CONFIRMED)
                    .reservationDate(LocalDateTime.now())
                    .startDate(LocalDateTime.now().plusDays(1))
                    .endDate(LocalDateTime.now().plusDays(2))
                    .build();

            reservationRepository.save(reservation);
            log.info("Initial reservation inserted");
        };
    }
}
