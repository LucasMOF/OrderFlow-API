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

        Cliente cliente = criarCliente(request.cliente());
        List<ItemPedido> itens = processarItens(request.itens());
        BigDecimal valorTotal = calcularValorTotal(itens);

        Pedido pedido = montarPedido(cliente, itens, valorTotal);
        Pedido salvarPedido = pedidoRepository.save(pedido);

        return converterParaResponse(salvarPedido);
    }

    private Cliente criarCliente(CriarPedidoRequest.ClienteRequest clienteRequest) {
        Cliente cliente = new Cliente(
                clienteRequest.nome(),
                clienteRequest.email(),
                clienteRequest.cpf()
        );
        return cliente;
    }

    private List<ItemPedido> processarItens(List<ItemPedidoRequest> itensRequest) {
        List<ItemPedido> itens = new ArrayList<>();
        for (ItemPedidoRequest itemRequest : itensRequest) {
            Produto produto = produtoRepository.findById(itemRequest.produtoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            ItemPedido item = new ItemPedido(null, null, produto, itemRequest.quantidade(), produto.getPreco());
            itens.add(item);
        }
        return itens;
    }

    private BigDecimal calcularValorTotal(List<ItemPedido> itens) {

        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemPedido item : itens) {
            valorTotal = valorTotal.add(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }
        return valorTotal;
    }

    private Pedido montarPedido(Cliente cliente, List<ItemPedido> itens, BigDecimal valorTotal) {
        Pedido pedido = new Pedido();

        for (ItemPedido item : itens) {
            pedido.adicionarItem(item);
        }
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setCliente(cliente);
        pedido.setValorTotal(valorTotal);
        return pedido;
    }

    private PedidoResponse converterParaResponse(Pedido pedido) {
        List<ItemPedidoResponse> itensResponse = new ArrayList<>();
        for (ItemPedido item : pedido.getItens()) {
            ItemPedidoResponse itemPedidoResponse = new ItemPedidoResponse(
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    item.getPrecoUnitario()
            );
            itensResponse.add(itemPedidoResponse);
        }
        PedidoResponse.ClienteResponse clienteResponse = new PedidoResponse.ClienteResponse(
                pedido.getCliente().getNome(),
                pedido.getCliente().getEmail(),
                pedido.getCliente().getCpf());

        PedidoResponse response = new PedidoResponse(
                pedido.getId(),
                pedido.getStatus(),
                pedido.getValorTotal(),
                clienteResponse,
                itensResponse,
                pedido.getDataCriacao(),
                pedido.getDataAtualizacao());
        return response;
    }
}



