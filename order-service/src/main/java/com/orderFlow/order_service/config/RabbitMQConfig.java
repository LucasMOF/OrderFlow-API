package com.orderFlow.order_service.config;

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
    public FanoutExchange pedidoCriadoExchange() {
        FanoutExchange exchange = new FanoutExchange("pedido.criado");
        return exchange;
    }

    @Bean
    public Binding biding(Queue notificationQueue, FanoutExchange pedidoCriadoExchange) {
        return BindingBuilder.bind(notificationQueue).to(pedidoCriadoExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
