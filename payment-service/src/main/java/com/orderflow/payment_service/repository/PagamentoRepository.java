package com.orderflow.payment_service.repository;

import com.orderflow.payment_service.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {
}
