package br.com.fiap.comped.bdd.model;

import br.com.fiap.comped.model.EquipamentoMonitorado;
import com.google.gson.annotations.Expose;
import lombok.Data;

import java.time.Instant;

@Data
public class ConsumoModel {

    @Expose(serialize = false)
    private Long idConsumo;

    @Expose
    private Long equipamentoId;

    @Expose
    private Instant dataConsumo;

    @Expose
    private Double kwhConsumo;

}
