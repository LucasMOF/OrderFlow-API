package com.orderflow.payment_service.messaging;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PagamentoConfirmadoEvent(
        UUID pedidoId,
        String nomeCliente,
        String emailCliente,
        BigDecimal valor,
        LocalDateTime dataConfirmacao
) {
}
