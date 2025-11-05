package br.com.fiap.comped.service;

import br.com.fiap.comped.dto.SetorDTO;
import br.com.fiap.comped.model.SetorEmpresa;
import br.com.fiap.comped.repository.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SetorService {
    @Autowired
    private SetorRepository setorRepository;

    public SetorService(SetorRepository setorRepository) {
        this.setorRepository = setorRepository;
    }

    public Page<SetorEmpresa> listar(String nome, Pageable pageable) {
        return (nome == null || nome.isBlank())
                ? setorRepository.findAll(pageable)
                : setorRepository.findByNomeSetorContainingIgnoreCase(nome, pageable);
    }

    public SetorEmpresa criar(SetorDTO request) {
        SetorEmpresa setor = new SetorEmpresa();
        setor.setNomeSetor(request.nomeSetor());
        return setorRepository.save(setor);
    }
}
