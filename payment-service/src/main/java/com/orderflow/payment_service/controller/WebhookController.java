package com.orderflow.payment_service.controller;


import com.orderflow.payment_service.dto.WebhookPagamentoRequest;
import com.orderflow.payment_service.service.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final PagamentoService pagamentoService;

    public WebhookController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/pagamento")
    public ResponseEntity<Void> atualizarPagamento(@RequestBody WebhookPagamentoRequest request) {
        pagamentoService.atualizarStatusPagamento(request.data().id());
        return ResponseEntity.ok().build();
    }
}
