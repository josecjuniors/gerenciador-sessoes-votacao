package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.application;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.StatusSessao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.Voto;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.dto.SessaoResponse;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.exception.SessaoFechadaException;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.exception.SessaoNaoEncontradaException;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.exception.VotoJaRealizadoException;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.SessaoVotacaoRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.VotoRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.RegistrarVotoUseCase;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.command.RegistrarVotoCommand;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistrarVotoService implements RegistrarVotoUseCase {

    private final VotoRepository votoRepository;
    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final CacheManager cacheManager;

    public RegistrarVotoService(VotoRepository votoRepository,
                                SessaoVotacaoRepository sessaoVotacaoRepository,
                                CacheManager cacheManager) {
        this.votoRepository = votoRepository;
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    @Transactional
    public void handle(RegistrarVotoCommand command) {
        // 1. Verifica Cache  obter a Sessão
        SessaoVotacao sessao = obterSessao(command);

        // 2. Valida se a sessão está aberta (Status e Data)
        validarSessaoAberta(sessao);

        // 3. Valida se o associado já votou
        if (votoRepository.existsBySessaoVotacaoIdAndCpfAssociado(command.sessaoId(), command.cpfAssociado())) {
            throw new VotoJaRealizadoException("O associado com CPF " + command.cpfAssociado() + " já votou nesta sessão.");
        }

        // 4. Registra o voto
        Voto voto = new Voto(sessao, command.cpfAssociado(), command.voto());
        votoRepository.save(voto);
    }

    private SessaoVotacao obterSessao(RegistrarVotoCommand command) {
        // Tenta pegar do cache primeiro
        Cache cache = cacheManager.getCache("sessoes");
        if (cache != null) {
            SessaoResponse sessaoCache = cache.get(command.sessaoId(), SessaoResponse.class);
            if (sessaoCache != null) {

                // Para garantir que o status está atualizado (caso o cache esteja stale por algum motivo de falha no evict),
                // e para ter a entidade gerenciada, vamos buscar do banco.
                // O Cache aqui serve mais para leitura rápida de status em endpoints de consulta.
                // Mas se quisermos usar o cache para evitar o SELECT na validação de status:
                
                if (!"ABERTA".equals(sessaoCache.status())) {
                     throw new SessaoFechadaException("Sessão está fechada (Cache Hit).");
                }
                // Se no cache diz que está aberta, ainda assim precisamos da entidade para salvar o Voto.
            }
        }

        return sessaoVotacaoRepository.findById(command.sessaoId())
                .orElseThrow(() -> new SessaoNaoEncontradaException("Sessão não encontrada com ID: " + command.sessaoId()));
    }

    private void validarSessaoAberta(SessaoVotacao sessao) {
        if (sessao.getStatus() != StatusSessao.ABERTA) {
            throw new SessaoFechadaException("A sessão de votação está fechada.");
        }
        if (LocalDateTime.now().isAfter(sessao.getDataHoraFechamento())) {
             throw new SessaoFechadaException("A sessão de votação já expirou.");
        }
    }
}
