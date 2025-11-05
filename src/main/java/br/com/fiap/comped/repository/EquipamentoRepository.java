package br.com.fiap.comped.repository;

import br.com.fiap.comped.model.EquipamentoMonitorado;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface EquipamentoRepository extends JpaRepository<EquipamentoMonitorado, Long> {
    Page<EquipamentoMonitorado> findBySetor_IdSetorAndAtivo(Long setorId, Integer ativo, Pageable pageable);
    List<EquipamentoMonitorado> findBySetor_IdSetor(Long setorId);
}
