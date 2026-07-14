package com.orderFlow.order_service.dto;

import com.orderFlow.order_service.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponse(
        UUID id,
        StatusPedido status,
        BigDecimal valorTotal,
        ClienteResponse cliente,
        List<ItemPedidoResponse> itens,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
    public record ClienteResponse(String nome, String email, String cpf){}
}
