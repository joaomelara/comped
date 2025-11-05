package br.com.fiap.comped.service;

import br.com.fiap.comped.dto.EquipamentoDTO;
import br.com.fiap.comped.dto.EquipamentoUpdateDTO;
import br.com.fiap.comped.model.EquipamentoMonitorado;
import br.com.fiap.comped.model.SetorEmpresa;
import br.com.fiap.comped.repository.EquipamentoRepository;
import br.com.fiap.comped.repository.SetorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class EquipamentoService {
    private final EquipamentoRepository equipamentoRepository;
    private final SetorRepository setorRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository, SetorRepository setorRepository) {
        this.equipamentoRepository = equipamentoRepository;
        this.setorRepository = setorRepository;
    }

    public Page<EquipamentoMonitorado> listar(Long setorId, Boolean ativo, Pageable pageable) {
        if (setorId != null && ativo != null) {
            return equipamentoRepository.findBySetor_IdSetorAndAtivo(setorId, ativo ? 1 : 0, pageable);
        }
        return equipamentoRepository.findAll(pageable);
    }

    public EquipamentoMonitorado criar(EquipamentoDTO dto) {
        SetorEmpresa setor = setorRepository.findById(dto.setorId())
                .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado"));

        EquipamentoMonitorado eqp = new EquipamentoMonitorado();
        eqp.setSetor(setor);
        eqp.setNomeEquipamento(dto.nomeEquipamento());
        eqp.setDataInstalacao(dto.dataInstalacao());
        eqp.setAtivo(1);
        eqp.setLimiteKwh(dto.limiteKwh());
        return equipamentoRepository.save(eqp);
    }

    public EquipamentoMonitorado atualizar(Long id, EquipamentoUpdateDTO dto) {
        EquipamentoMonitorado eqp = equipamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado"));

        if (dto.nomeEquipamento() != null) eqp.setNomeEquipamento(dto.nomeEquipamento());
        if (dto.limiteKwh() != null) eqp.setLimiteKwh(dto.limiteKwh());
        if (dto.ativo() != null) eqp.setAtivo(dto.ativo() ? 1 : 0);

        return equipamentoRepository.save(eqp);
    }
}
