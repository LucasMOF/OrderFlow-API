package com.orderflow.payment_service.listener;

import com.orderflow.payment_service.messaging.PedidoCriadoEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoCriadoListener {

    @RabbitListener(queues = "payment.queue")
    public void ouvirPedidoCriado(PedidoCriadoEvent evento) {
        System.out.println("Evento recebido: " + evento);
    }
}
