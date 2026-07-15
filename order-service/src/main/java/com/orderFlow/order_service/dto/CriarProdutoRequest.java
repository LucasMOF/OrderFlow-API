package com.orderFlow.order_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarProdutoRequest(
        @NotBlank(message = "O nome não pode ficar em branco")
        String nome,

        @NotNull(message = "O preço não pode ficar em branco")
        @DecimalMin(value = "0.01", inclusive = true, message = "O preço deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "O preço deve ter no máximo 8 dígitos inteiros e 2 casas decimais")
        BigDecimal preco
) {
}
