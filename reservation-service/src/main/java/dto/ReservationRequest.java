package dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationRequest(
        @NotNull(message = "resourceId is required") Long resourceId,
        @NotNull(message = "userId is required") Long userId,
        @NotNull(message = "quantity is required") @Min(1) Integer quantity,
        @NotNull(message = "startDate is required") LocalDateTime startDate,
        @NotNull(message = "endDate is required") LocalDateTime endDate
) {
}
