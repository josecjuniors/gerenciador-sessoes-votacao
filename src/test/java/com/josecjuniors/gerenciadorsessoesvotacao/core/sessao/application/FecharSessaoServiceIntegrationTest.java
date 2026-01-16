package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.application;

import com.josecjuniors.gerenciadorsessoesvotacao.IntegrationTest;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.Pauta;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.repository.PautaRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.StatusSessao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.SessaoVotacaoRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.command.FecharSessaoCommand;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
class FecharSessaoServiceIntegrationTest {

    @Autowired
    private FecharSessaoService fecharSessaoService;

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Test
    void deveFecharSessaoAbertaComSucesso() {
        // Arrange
        Pauta pauta = pautaRepository.save(new Pauta("Pauta Teste", "Desc"));
        SessaoVotacao sessao = new SessaoVotacao(pauta, 10);
        sessao = sessaoVotacaoRepository.save(sessao);

        assertEquals(StatusSessao.ABERTA, sessao.getStatus());

        // Act
        fecharSessaoService.handle(new FecharSessaoCommand(sessao.getId()));

        // Assert
        SessaoVotacao sessaoAtualizada = sessaoVotacaoRepository.findById(sessao.getId()).orElseThrow();
        assertEquals(StatusSessao.FECHADA, sessaoAtualizada.getStatus());
    }

    @Test
    void deveLancarExcecaoQuandoSessaoNaoExistir() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
            fecharSessaoService.handle(new FecharSessaoCommand(idInexistente))
        );
    }

    @Test
    void naoDeveLancarErroSeSessaoJaEstiverFechada() {
        // Arrange
        Pauta pauta = pautaRepository.save(new Pauta("Pauta Fechada", "Desc"));
        SessaoVotacao sessao = new SessaoVotacao(pauta, 10);
        sessao.fecharSessao(); // Já nasce fechada para o teste
        sessao = sessaoVotacaoRepository.save(sessao);

        // Act
        fecharSessaoService.handle(new FecharSessaoCommand(sessao.getId()));

        // Assert
        SessaoVotacao sessaoAtualizada = sessaoVotacaoRepository.findById(sessao.getId()).orElseThrow();
        assertEquals(StatusSessao.FECHADA, sessaoAtualizada.getStatus());
    }
}
