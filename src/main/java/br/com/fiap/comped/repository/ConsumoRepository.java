package br.com.fiap.comped.repository;

import br.com.fiap.comped.model.ConsumoEnergia;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ConsumoRepository extends JpaRepository<ConsumoEnergia, Long> {
    List<ConsumoEnergia> findByEquipamento_IdEquipamento(Long equipId);
    List<ConsumoEnergia> findByEquipamento_IdEquipamentoAndDataConsumoBetween(Long equipId, Instant inicio, Instant fim);
    boolean existsByEquipamento_IdEquipamentoAndDataConsumo(Long equipId, Instant dataConsumo);
}
