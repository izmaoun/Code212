package dto;

import entities.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long resourceId,
        Long userId,
        Integer quantity,
        ReservationStatus status,
        LocalDateTime reservationDate,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
