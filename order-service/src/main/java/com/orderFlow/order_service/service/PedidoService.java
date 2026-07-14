package com.orderFlow.order_service.service;

import com.orderFlow.order_service.dto.CriarPedidoRequest;
import com.orderFlow.order_service.dto.ItemPedidoRequest;
import com.orderFlow.order_service.dto.ItemPedidoResponse;
import com.orderFlow.order_service.dto.PedidoResponse;
import com.orderFlow.order_service.model.*;
import com.orderFlow.order_service.repository.PedidoRepository;
import com.orderFlow.order_service.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public PedidoResponse criarPedido(CriarPedidoRequest request) {
        Cliente cliente = new Cliente(request.cliente().nome(), request.cliente().email(), request.cliente().cpf());

        List<ItemPedido> itens = new ArrayList<>();

        for (ItemPedidoRequest itemRequest : request.itens()){
            Produto produto = produtoRepository.findById(itemRequest.produtoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            ItemPedido item = new ItemPedido(null, null, produto, itemRequest.quantidade(), produto.getPreco());
            itens.add(item);
        }
        BigDecimal valorTotal = BigDecimal.ZERO;
        Pedido pedido = new Pedido();

        for (ItemPedido item : itens) {
            pedido.adicionarItem(item);
            valorTotal = valorTotal.add(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setCliente(cliente);
        pedido.setValorTotal(valorTotal);
        Pedido salvarPedido = pedidoRepository.save(pedido);

        List<ItemPedidoResponse> itensResponse = new ArrayList<>();
        for (ItemPedido item : salvarPedido.getItens()){
            ItemPedidoResponse itemPedidoResponse = new ItemPedidoResponse(
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    item.getPrecoUnitario()
            );
            itensResponse.add(itemPedidoResponse);
        }
        PedidoResponse.ClienteResponse clienteResponse = new PedidoResponse.ClienteResponse(
                salvarPedido.getCliente().getNome(),
                salvarPedido.getCliente().getEmail(),
                salvarPedido.getCliente().getCpf());

        PedidoResponse response = new PedidoResponse(
                salvarPedido.getId(),
                salvarPedido.getStatus(),
                salvarPedido.getValorTotal(),
                clienteResponse,
                itensResponse,
                salvarPedido.getDataCriacao(),
                salvarPedido.getDataAtualizacao());
        return response;

    }
}
