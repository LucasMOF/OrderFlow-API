package com.orderflow.payment_service.dto;

public record WebhookPagamentoRequest(
        String action,
        DataRequest data
) {
    public record DataRequest(String id){
    }
}
