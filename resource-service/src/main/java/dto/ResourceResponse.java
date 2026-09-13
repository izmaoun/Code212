package dto;

import entities.ResourceStatus;

import java.time.LocalDateTime;

public record ResourceResponse(
        Long id,
        String name,
        String description,
        String category,
        Integer quantityTotal,
        Integer quantityAvailable,
        ResourceStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}