package dto;

import jakarta.validation.constraints.NotNull;

public record StockUpdateRequest(
        @NotNull Integer delta
) {}