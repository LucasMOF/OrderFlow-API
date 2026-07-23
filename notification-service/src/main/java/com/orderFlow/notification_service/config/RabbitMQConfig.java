package com.orderFlow.notification_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue notificationQueue() {
        Queue queue = new Queue("notification.queue");
        return queue;
    }

    @Bean
    public Queue notificationSendQueue() {
        Queue queue = new Queue("notification.send.queue");
        return queue;
    }

    @Bean
    public FanoutExchange pagamentoConfirmadoExchange() {
        FanoutExchange exchange = new FanoutExchange("pagamento.confirmado");
        return exchange;
    }

    @Bean
    public Binding biding(Queue notificationSendQueue, FanoutExchange pagamentoConfirmadoExchange) {
        return BindingBuilder.bind(notificationSendQueue).to(pagamentoConfirmadoExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
