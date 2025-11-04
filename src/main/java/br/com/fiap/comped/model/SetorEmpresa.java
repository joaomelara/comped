package br.com.fiap.comped.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SETOR_EMPRESA")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SetorEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SETOR")
    private Long idSetor;

    @Column(name = "NOME_SETOR", nullable = false, length = 50, unique = true)
    private String nomeSetor;
}