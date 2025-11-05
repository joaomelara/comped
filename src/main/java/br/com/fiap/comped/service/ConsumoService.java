package br.com.fiap.comped.service;

import br.com.fiap.comped.dto.ConsumoDTO;
import br.com.fiap.comped.model.ConsumoEnergia;
import br.com.fiap.comped.model.EquipamentoMonitorado;
import br.com.fiap.comped.repository.ConsumoRepository;
import br.com.fiap.comped.repository.EquipamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ConsumoService {
    private final ConsumoRepository consumoRepository;
    private final EquipamentoRepository equipamentoRepository;

    public ConsumoService(ConsumoRepository consumoRepository, EquipamentoRepository equipamentoRepository) {
        this.consumoRepository = consumoRepository;
        this.equipamentoRepository = equipamentoRepository;
    }

    public List<ConsumoEnergia> listar(Long equipId, Instant inicio, Instant fim) {
        if (inicio == null && fim == null) {
            // Return all records for this equipment
            return consumoRepository.findByEquipamento_IdEquipamento(equipId);
        }

        if (inicio == null) {
            inicio = Instant.EPOCH;
        }
        if (fim == null) {
            fim = Instant.now();
        }

        return consumoRepository.findByEquipamento_IdEquipamentoAndDataConsumoBetween(equipId, inicio, fim);
    }

    public ConsumoEnergia registrar(ConsumoDTO dto) {
        EquipamentoMonitorado eqp = equipamentoRepository.findById(dto.equipId())
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado"));

        if (consumoRepository.existsByEquipamento_IdEquipamentoAndDataConsumo(dto.equipId(), dto.dataConsumo())) {
            throw new IllegalStateException("Consumo já registrado neste horário");
        }

        ConsumoEnergia consumo = new ConsumoEnergia();
        consumo.setEquipamento(eqp);
        consumo.setDataConsumo(dto.dataConsumo());
        consumo.setKwhConsumo(dto.kwhConsumo());

        if (dto.kwhConsumo() > eqp.getLimiteKwh()) {
            System.out.println("Alerta: consumo acima do limite do equipamento " + eqp.getNomeEquipamento());
        }

        return consumoRepository.save(consumo);
    }
}