package com.orderflow.payment_service.listener;

import com.orderflow.payment_service.messaging.PedidoCriadoEvent;
import com.orderflow.payment_service.service.PagamentoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoCriadoListener {

    private final PagamentoService pagamentoService;

    public PagamentoCriadoListener(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @RabbitListener(queues = "payment.queue")
    public void ouvirPedidoCriado(PedidoCriadoEvent evento) {
        System.out.println("Evento recebido: " + evento);
        pagamentoService.processarPagamentoPix(evento);
    }
}
