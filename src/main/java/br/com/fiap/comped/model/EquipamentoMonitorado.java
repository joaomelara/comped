package br.com.fiap.comped.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "EQUIPAMENTOS_MONITORADOS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class EquipamentoMonitorado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EQUIPAMENTO")
    private Long idEquipamento;

    @ManyToOne
    @JoinColumn(name = "SETOR_ID", nullable = false)
    private SetorEmpresa setor;

    @Column(name = "NOME_EQUIPAMENTO", nullable = false, length = 100)
    private String nomeEquipamento;

    @Column(name = "DATA_INSTALACAO", nullable = false)
    private LocalDate dataInstalacao;

    @Column(name = "ATIVO", nullable = false)
    private Integer ativo = 1; // Oracle não tem BOOLEAN

    @Column(name = "LIMITE_KWH", nullable = false)
    private Double limiteKwh;
}
