package com.orderflow.payment_service.model;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Pagamento extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID pedidoId;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status;
    private BigDecimal valor;
    private String mercadoPagoId;
    private String codigoPix;
    private String nomeCliente;
    private String emailCliente;

    public Pagamento() {
    }

    public Pagamento(UUID id, UUID pedidoId, StatusPagamento status, BigDecimal valor, String mercadoPagoId, String codigoPix, String nomeCliente, String emailCliente) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.status = status;
        this.valor = valor;
        this.mercadoPagoId = mercadoPagoId;
        this.codigoPix = codigoPix;
        this.nomeCliente = nomeCliente;
        this.emailCliente = emailCliente;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(UUID pedidoId) {
        this.pedidoId = pedidoId;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getMercadoPagoId() {
        return mercadoPagoId;
    }

    public void setMercadoPagoId(String mercadoPagoId) {
        this.mercadoPagoId = mercadoPagoId;
    }

    public String getCodigoPix() {
        return codigoPix;
    }

    public void setCodigoPix(String codigoPix) {
        this.codigoPix = codigoPix;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }
}
