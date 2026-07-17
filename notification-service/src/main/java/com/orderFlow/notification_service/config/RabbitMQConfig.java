package com.orderFlow.notification_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig{

    @Bean
    public Queue notificationQueue() {
        Queue queue = new Queue("notification.queue");
        return queue;
    }
}
