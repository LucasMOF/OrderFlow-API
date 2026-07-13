package com.orderFlow.order_service.model;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Pedido extends EntidadeBase{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    private BigDecimal valorTotal;

    @Embedded
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {
    }

    public Pedido(UUID id, StatusPedido status, BigDecimal valorTotal, Cliente cliente, List<ItemPedido> itens) {
        this.id = id;
        this.status = status;
        this.valorTotal = valorTotal;
        this.cliente = cliente;
        this.itens = itens;
    }

    public void adicionarItem(ItemPedido item) {
        if (item == null) {
            throw new IllegalArgumentException("O item não pode ser nulo");
        }
        item.setPedido(this);
        this.itens.add(item);
    }

    public void removerItem(ItemPedido item) {
        if (item == null) {
            throw new IllegalArgumentException("O item não pode ser nulo");
        }
        item.setPedido(null);
        this.itens.remove(item);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }
}
