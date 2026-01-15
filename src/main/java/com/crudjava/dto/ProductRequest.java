package com.crudjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest (
    @NotBlank
    @Schema(example = "Notebook")
    String name,

    @NotNull
    @Min(0)
    @Schema(example = "2500", description = "Preço em reais")
    Double price
) { }
