package com.orderFlow.notification_service.messaging;

import java.math.BigDecimal;
import java.util.UUID;

public record PedidoCriadoEvent(
        UUID pedidoId,
        String nomeCliente,
        String emailCliente,
        BigDecimal valorTotal
) {
}
