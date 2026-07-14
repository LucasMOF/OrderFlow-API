package com.orderFlow.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ItemPedidoRequest(
        @NotNull(message = "O ID do produto não pode estar em branco")
        UUID produtoId,
        @NotNull(message = "A quantidade do produto não pode estar em branco")
        @Min(1)
        Integer quantidade
        ) {
}
