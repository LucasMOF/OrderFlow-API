package com.orderFlow.order_service.listener;

import com.orderFlow.order_service.messaging.PagamentoConfirmadoEvent;
import com.orderFlow.order_service.service.PedidoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoConfirmadoListener {

    private final PedidoService pedidoService;

    public PagamentoConfirmadoListener(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @RabbitListener(queues = "payment.confirm.queue")
    public void ouvirPagamentoConfirmado(PagamentoConfirmadoEvent evento) {
        pedidoService.marcarPedidoComoPago(evento.pedidoId());
    }
}
