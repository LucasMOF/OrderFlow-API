package com.orderFlow.order_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProdutoResponse(
        UUID id,
        String nome,
        BigDecimal preco,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
}
