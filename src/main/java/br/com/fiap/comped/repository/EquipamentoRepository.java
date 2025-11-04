package br.com.fiap.comped.repository;

import br.com.fiap.comped.model.EquipamentoMonitorado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipamentoRepository extends JpaRepository<EquipamentoMonitorado, Long> {
}
