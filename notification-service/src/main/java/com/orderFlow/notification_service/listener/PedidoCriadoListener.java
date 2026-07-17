package com.orderFlow.notification_service.listener;

import com.orderFlow.notification_service.messaging.PedidoCriadoEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoCriadoListener {

    @RabbitListener(queues = "notification.queue")
    public void ouvirPedidoCriado(PedidoCriadoEvent evento) {
        System.out.println("Evento recebido: " + evento);
    }
}
