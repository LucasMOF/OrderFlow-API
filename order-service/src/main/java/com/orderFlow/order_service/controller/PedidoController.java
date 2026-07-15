package com.orderFlow.order_service.controller;

import com.orderFlow.order_service.dto.CriarPedidoRequest;
import com.orderFlow.order_service.dto.PedidoResponse;
import com.orderFlow.order_service.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(@Valid @RequestBody CriarPedidoRequest pedidoRequest) {
        PedidoResponse pedido = pedidoService.criarPedido(pedidoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }
}
