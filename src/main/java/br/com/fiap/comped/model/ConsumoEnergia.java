package br.com.fiap.comped.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "CONSUMO_ENERGIA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ConsumoEnergia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONSUMO")
    private Long idConsumo;

    @ManyToOne
    @JoinColumn(name = "EQUIP_ID", nullable = false)
    private EquipamentoMonitorado equipamento;

    @Column(name = "DATA_CONSUMO", nullable = false)
    private Instant dataConsumo;

    @Column(name = "KWH_CONSUMO", nullable = false)
    private Double kwhConsumo;
}
