package com.orderFlow.notification_service.listener;

import com.orderFlow.notification_service.messaging.PagamentoConfirmadoEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class PagamentoConfirmadoListener {

    private final JavaMailSender mailSender;

    public PagamentoConfirmadoListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = "notification.send.queue")
    public void ouvirPagamentoEnviado(PagamentoConfirmadoEvent evento) {
        System.out.println("Evento recebido: " + evento);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(evento.emailCliente());
        message.setSubject("Recebemos seu pagamento");

        String corpoTexto = String.format("Olá, %s! O seu pagamento no valor de R$ %.2f foi confirmado.",
                evento.nomeCliente(), evento.valor());

        message.setText(corpoTexto);
        mailSender.send(message);
    }
}
