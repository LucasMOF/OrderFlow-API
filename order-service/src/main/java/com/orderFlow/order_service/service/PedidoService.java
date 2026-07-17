package com.orderFlow.order_service.service;

import com.orderFlow.order_service.dto.CriarPedidoRequest;
import com.orderFlow.order_service.dto.ItemPedidoRequest;
import com.orderFlow.order_service.dto.ItemPedidoResponse;
import com.orderFlow.order_service.dto.PedidoResponse;
import com.orderFlow.order_service.exception.RecursoNaoEncontradoException;
import com.orderFlow.order_service.messaging.PedidoCriadoEvent;
import com.orderFlow.order_service.model.*;
import com.orderFlow.order_service.repository.PedidoRepository;
import com.orderFlow.order_service.repository.ProdutoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final RabbitTemplate rabbitTemplate;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository, RabbitTemplate rabbitTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public PedidoResponse criarPedido(CriarPedidoRequest request) {

        Cliente cliente = criarCliente(request.cliente());
        List<ItemPedido> itens = processarItens(request.itens());
        BigDecimal valorTotal = calcularValorTotal(itens);

        Pedido pedido = montarPedido(cliente, itens, valorTotal);
        Pedido salvarPedido = pedidoRepository.save(pedido);
        publicarEventoPedidoCriado(salvarPedido);

        return converterParaResponse(salvarPedido);
    }

    private void publicarEventoPedidoCriado(Pedido pedido) {

        PedidoCriadoEvent pedidoCriadoEvent = new PedidoCriadoEvent(
                pedido.getId(),
                pedido.getCliente().getNome(),
                pedido.getCliente().getEmail(),
                pedido.getValorTotal()
        );

        rabbitTemplate.convertAndSend(
                "pedido.criado",
                "",
                pedidoCriadoEvent
        );
    }

    public PedidoResponse buscarPedidoPorId(UUID id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado"));
        return converterParaResponse(pedido);
    }

    public List<PedidoResponse> listarPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidos.stream()
                .map(this::converterParaResponse)
                .toList();
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
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

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



