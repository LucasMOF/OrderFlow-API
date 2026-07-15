package com.orderFlow.order_service.service;

import com.orderFlow.order_service.dto.CriarProdutoRequest;
import com.orderFlow.order_service.dto.ProdutoResponse;
import com.orderFlow.order_service.model.Produto;
import com.orderFlow.order_service.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ProdutoResponse criarProduto(CriarProdutoRequest produtoRequest) {
        Produto produto = new Produto(
                null,
                produtoRequest.nome(),
                produtoRequest.preco()
        );
        Produto salvarProduto = produtoRepository.save(produto);
        return criarProdutoResponse(salvarProduto);
    }

    private ProdutoResponse criarProdutoResponse(Produto produto) {
        ProdutoResponse response = new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.getDataCriacao(),
                produto.getDataAtualizacao()
        );
        return response;
    }

}
