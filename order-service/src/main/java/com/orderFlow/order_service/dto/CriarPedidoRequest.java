package com.orderFlow.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record CriarPedidoRequest(
        @Valid
        @NotNull
        ClienteRequest cliente,

        @Valid
        @NotEmpty
        List<ItemPedidoRequest> itens
) {
    public record ClienteRequest(
            @NotBlank(message = "O nome não pode estar em branco")
            String nome,
            @NotBlank(message = "O e-mail não pode estar em branco")
            @Email(message = "O formato do e-mail é inválido")
            String email,
            @NotBlank(message = "O CPF não pode estar em branco")
            @Pattern(regexp = "(^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$)|(^\\d{11}$)", message = "O CPF deve estar no formato 000.000.000-00 ou conter apenas 11 números.")
            String cpf
    ){}
}
