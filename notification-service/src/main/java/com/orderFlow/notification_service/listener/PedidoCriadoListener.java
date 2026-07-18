package com.orderFlow.notification_service.listener;

import com.orderFlow.notification_service.messaging.PedidoCriadoEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class PedidoCriadoListener {

    private final JavaMailSender javaMailSender;

    public PedidoCriadoListener(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RabbitListener(queues = "notification.queue")
    public void ouvirPedidoCriado(PedidoCriadoEvent evento) {
        System.out.println("Evento recebido: " + evento);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(evento.emailCliente());
        message.setSubject("Recebemos seu pedido");

        String corpoTexto = String.format("Olá, %s! Recebemos seu pedido no valor de R$ %.2f. Em breve enviaremos a confirmação do pagamento.",
                evento.nomeCliente(), evento.valorTotal());

        message.setText(corpoTexto);

        javaMailSender.send(message);
    }
}
