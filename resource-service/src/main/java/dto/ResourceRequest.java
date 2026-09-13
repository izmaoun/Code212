package dto;

import jakarta.validation.constraints.*;

public record ResourceRequest(

        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 150)
        String name,

        @Size(max = 500)
        String description,

        @NotBlank(message = "La catégorie est obligatoire")
        @Size(max = 80)
        String category,

        @NotNull @Min(0)
        Integer quantityTotal,

        @Min(0)
        Integer quantityAvailable
) {}


