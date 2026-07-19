package com.orderflow.payment_service.config;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoInitializer  {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostConstruct
    public void inicializarMercadoPago() {
        MercadoPagoConfig.setAccessToken(accessToken);
    }
}
