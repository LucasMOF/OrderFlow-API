package com.orderflow.payment_service.service;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.orderflow.payment_service.messaging.PedidoCriadoEvent;
import com.orderflow.payment_service.model.Pagamento;
import com.orderflow.payment_service.model.StatusPagamento;
import com.orderflow.payment_service.repository.PagamentoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    private Payment criarPagamentoPix(PedidoCriadoEvent event) {
        PaymentPayerRequest payer = PaymentPayerRequest.builder()
                .email(event.emailCliente())
                .build();

        PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                .transactionAmount(event.valorTotal())
                .description("Pedido OrderFlow")
                .paymentMethodId("pix")
                .payer(payer)
                .build();

        PaymentClient client = new PaymentClient();

        try {
            Payment payment = client.create(paymentCreateRequest);
            System.out.println("Pagamento Criado: " + payment.getId());
            return payment;
        } catch (MPException | MPApiException e) {
            throw new RuntimeException("Erro ao criar pagamento: " + e.getMessage(), e);
        }
    }

    public void processarPagamentoPix(PedidoCriadoEvent event) {
        Payment payment = criarPagamentoPix(event);

        String codigoPix = Optional.ofNullable(payment.getPointOfInteraction())
                .map(pointOfInteraction -> pointOfInteraction.getTransactionData().getQrCode())
                .orElseThrow(() -> new RuntimeException("Código PIX não retornado pelo Mercado Pago"));

        Pagamento pagamento = new Pagamento(
                null,
                event.pedidoId(),
                StatusPagamento.PENDENTE,
                event.valorTotal(),
                payment.getId().toString(),
                codigoPix
        );
        pagamentoRepository.save(pagamento);
    }

    private Payment buscarPagamentoNoMercadoPago(String mercadoPagoId) {

        try {
            PaymentClient client = new PaymentClient();
            long id = Long.parseLong(mercadoPagoId);
            Payment payment = client.get(id);
            return payment;

        } catch (MPException | MPApiException e) {
            throw new RuntimeException("Erro ao criar pagamento: " + e.getMessage(), e);
        }
    }

    public Payment atualizarStatusPagamento(String mercadoPagoId) {

        Payment payment = buscarPagamentoNoMercadoPago(mercadoPagoId);

        Pagamento pagamento = pagamentoRepository.findByMercadoPagoId(mercadoPagoId)
                .orElseThrow(() -> new RuntimeException("Erro ao receber id"));
        System.out.println("Status recebido: " + payment.getStatus());

        String status = payment.getStatus();

        switch (status) {
            case "pending":
                pagamento.setStatus(StatusPagamento.PENDENTE);
                break;
            case "approved":
                pagamento.setStatus(StatusPagamento.APROVADO);
                break;
            case "rejected":
                pagamento.setStatus(StatusPagamento.RECUSADO);
                break;
            default:
                throw new IllegalArgumentException("Status de pagamento desconhecido: " + status);
        }
        pagamentoRepository.save(pagamento);
        return payment;
    }
}
