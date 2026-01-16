package com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.application;

import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.Pauta;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.dto.PautaResponse;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.repository.PautaRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.usecase.CriarPautaUseCase;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.usecase.command.CriarPautaCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarPautaService implements CriarPautaUseCase {

    private final PautaRepository pautaRepository;

    public CriarPautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    @Override
    @Transactional
    public PautaResponse handle(CriarPautaCommand command) {
        Pauta novaPauta = new Pauta(command.titulo(), command.descricao());
        Pauta pautaSalva = pautaRepository.save(novaPauta);
        return new PautaResponse(pautaSalva.getId(), pautaSalva.getTitulo(), pautaSalva.getDescricao());
    }
}
