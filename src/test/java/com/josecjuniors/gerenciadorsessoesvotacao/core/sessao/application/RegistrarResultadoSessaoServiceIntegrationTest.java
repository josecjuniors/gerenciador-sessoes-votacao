package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.application;

import com.josecjuniors.gerenciadorsessoesvotacao.IntegrationTest;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.Pauta;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.repository.PautaRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.ResultadoVotacao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacaoResultado;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.Voto;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.SessaoVotacaoResultadoRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.SessaoVotacaoRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
class RegistrarResultadoSessaoServiceIntegrationTest {

    @Autowired
    private RegistrarResultadoSessaoService registrarResultadoSessaoService;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private SessaoVotacaoResultadoRepository sessaoVotacaoResultadoRepository;

    @Test
    void deveRegistrarResultadoAprovado() {
        // Arrange
        SessaoVotacao sessao = criarSessao();
        criarVoto(sessao, "11111111111", true);
        criarVoto(sessao, "22222222222", true);
        criarVoto(sessao, "33333333333", false);

        // Act
        registrarResultadoSessaoService.execute(sessao);

        // Assert
        Optional<SessaoVotacaoResultado> resultadoOpt = sessaoVotacaoResultadoRepository.findBySessaoVotacaoId(sessao.getId());
        assertTrue(resultadoOpt.isPresent());
        SessaoVotacaoResultado resultado = resultadoOpt.get();
        
        assertEquals(ResultadoVotacao.APROVADA, resultado.getResultado());
        assertEquals(2, resultado.getQuantidadeSim());
        assertEquals(1, resultado.getQuantidadeNao());
        assertEquals(3, resultado.getQuantidadeVotos());
    }

    @Test
    void deveRegistrarResultadoReprovado() {
        // Arrange
        SessaoVotacao sessao = criarSessao();
        criarVoto(sessao, "11111111111", true);
        criarVoto(sessao, "22222222222", false);
        criarVoto(sessao, "33333333333", false);

        // Act
        registrarResultadoSessaoService.execute(sessao);

        // Assert
        SessaoVotacaoResultado resultado = sessaoVotacaoResultadoRepository.findBySessaoVotacaoId(sessao.getId()).orElseThrow();
        
        assertEquals(ResultadoVotacao.REPROVADA, resultado.getResultado());
        assertEquals(1, resultado.getQuantidadeSim());
        assertEquals(2, resultado.getQuantidadeNao());
        assertEquals(3, resultado.getQuantidadeVotos());
    }

    @Test
    void deveRegistrarResultadoEmpate() {
        // Arrange
        SessaoVotacao sessao = criarSessao();
        criarVoto(sessao, "11111111111", true);
        criarVoto(sessao, "22222222222", false);

        // Act
        registrarResultadoSessaoService.execute(sessao);

        // Assert
        SessaoVotacaoResultado resultado = sessaoVotacaoResultadoRepository.findBySessaoVotacaoId(sessao.getId()).orElseThrow();
        
        assertEquals(ResultadoVotacao.EMPATE, resultado.getResultado());
        assertEquals(1, resultado.getQuantidadeSim());
        assertEquals(1, resultado.getQuantidadeNao());
        assertEquals(2, resultado.getQuantidadeVotos());
    }

    @Test
    void deveRegistrarResultadoSemVotos() {
        // Arrange
        SessaoVotacao sessao = criarSessao();

        // Act
        registrarResultadoSessaoService.execute(sessao);

        // Assert
        SessaoVotacaoResultado resultado = sessaoVotacaoResultadoRepository.findBySessaoVotacaoId(sessao.getId()).orElseThrow();
        
        assertEquals(ResultadoVotacao.EMPATE, resultado.getResultado());
        assertEquals(0, resultado.getQuantidadeSim());
        assertEquals(0, resultado.getQuantidadeNao());
        assertEquals(0, resultado.getQuantidadeVotos());
    }

    private SessaoVotacao criarSessao() {
        Pauta pauta = pautaRepository.save(new Pauta("Pauta Resultado " + UUID.randomUUID(), "Desc"));
        return sessaoVotacaoRepository.save(new SessaoVotacao(pauta, 10));
    }

    private void criarVoto(SessaoVotacao sessao, String cpf, boolean voto) {
        votoRepository.save(new Voto(sessao, cpf, voto));
    }
}
