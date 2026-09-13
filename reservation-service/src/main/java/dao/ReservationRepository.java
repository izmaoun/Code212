package dao;

import entities.Reservation;
import entities.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByResourceId(Long resourceId);
    List<Reservation> findByStatus(ReservationStatus status);
}
