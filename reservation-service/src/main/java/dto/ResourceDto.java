package dto;

public record ResourceDto(
        Long id,
        String name,
        String description,
        Double price,
        Integer quantity,
        String status
) {
}
