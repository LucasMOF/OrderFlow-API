package com.orderFlow.order_service.dto;

import java.math.BigDecimal;

public record ItemPedidoResponse(String nomeProduto, Integer quantidade, BigDecimal precoUnitario) {
}
