package com.orderFlow.order_service.controller;

import com.orderFlow.order_service.dto.CriarProdutoRequest;
import com.orderFlow.order_service.dto.ProdutoResponse;
import com.orderFlow.order_service.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> inserirProduto(@Valid @RequestBody CriarProdutoRequest request) {
        ProdutoResponse response = produtoService.criarProduto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
