package mapper;

import dto.ReservationRequest;
import dto.ReservationResponse;
import entities.Reservation;
import entities.ReservationStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    public Reservation toEntity(ReservationRequest request) {
        Reservation reservation = new Reservation();
        reservation.setResourceId(request.resourceId());
        reservation.setUserId(request.userId());
        reservation.setQuantity(request.quantity());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setStartDate(request.startDate());
        reservation.setEndDate(request.endDate());
        return reservation;
    }

    public ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getResourceId(),
                reservation.getUserId(),
                reservation.getQuantity(),
                reservation.getStatus(),
                reservation.getReservationDate(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
    }

    public List<ReservationResponse> toResponseList(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
