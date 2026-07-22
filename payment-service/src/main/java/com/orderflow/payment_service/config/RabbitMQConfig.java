package com.orderflow.payment_service.config;

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
    public Queue paymentQueue() {
        Queue queue = new Queue("payment.queue");
        return queue;
    }

    @Bean
    public FanoutExchange pedidoCriadoExchange() {
        FanoutExchange exchange = new FanoutExchange("pedido.criado");
        return exchange;
    }

    @Bean
    public FanoutExchange pagamentoConfirmadoExchange() {
        FanoutExchange exchange = new FanoutExchange("pagamento.confirmado");
        return exchange;
    }

    @Bean
    public Binding biding(Queue paymentQueue, FanoutExchange pedidoCriadoExchange) {
        return BindingBuilder.bind(paymentQueue).to(pedidoCriadoExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
