package dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record ResourceDto(
        Long id,
        String name,
        String description,
        Double price,
        @JsonAlias("quantityAvailable") Integer quantity,
        String status
) {
}
