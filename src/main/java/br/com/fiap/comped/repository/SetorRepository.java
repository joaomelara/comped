package br.com.fiap.comped.repository;

import br.com.fiap.comped.model.SetorEmpresa;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;



public interface SetorRepository extends JpaRepository<SetorEmpresa, Long> {
    Page<SetorEmpresa> findByNomeSetorContainingIgnoreCase(String nomeSetor, Pageable pageable);
}
