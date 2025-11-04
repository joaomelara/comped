package br.com.fiap.comped.repository;

import br.com.fiap.comped.model.ConsumoEnergia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumoRepository extends JpaRepository<ConsumoEnergia, Long> {
}
