package com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.web;

import com.josecjuniors.gerenciadorsessoesvotacao.IntegrationTest;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.Pauta;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.repository.PautaRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.ResultadoVotacao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacaoResultado;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.SessaoVotacaoRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.SessaoVotacaoResultadoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ResultadoSessaoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    private SessaoVotacaoResultadoRepository sessaoVotacaoResultadoRepository;

    @Test
    void deveRetornarResultadoComSucesso() throws Exception {
        // Arrange
        Pauta pauta = pautaRepository.save(new Pauta("Pauta Resultado", "Desc"));
        SessaoVotacao sessao = new SessaoVotacao(pauta, 10);
        sessao.fecharSessao();
        sessao = sessaoVotacaoRepository.save(sessao);

        // Simula um resultado já apurado (2 Sim, 1 Não = APROVADA)
        SessaoVotacaoResultado resultado = new SessaoVotacaoResultado(sessao, 2, 1);
        sessaoVotacaoResultadoRepository.save(resultado);

        // Act & Assert
        mockMvc.perform(get("/v1/sessoes/{sessaoId}/resultado", sessao.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessaoId").value(sessao.getId().toString()))
                .andExpect(jsonPath("$.resultado").value("APROVADA"))
                .andExpect(jsonPath("$.quantidadeSim").value(2))
                .andExpect(jsonPath("$.quantidadeNao").value(1))
                .andExpect(jsonPath("$.quantidadeVotos").value(3));
    }

    @Test
    void deveRetornarNotFoundQuandoResultadoNaoExistir() throws Exception {
        // Arrange
        // Cria uma sessão, mas não cria o resultado (simula sessão aberta ou recém fechada sem apuração)
        Pauta pauta = pautaRepository.save(new Pauta("Pauta Sem Resultado", "Desc"));
        SessaoVotacao sessao = sessaoVotacaoRepository.save(new SessaoVotacao(pauta, 10));

        // Act & Assert
        mockMvc.perform(get("/v1/sessoes/{sessaoId}/resultado", sessao.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Recurso não encontrado"));
    }

    @Test
    void deveRetornarNotFoundQuandoSessaoNaoExistir() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/sessoes/{sessaoId}/resultado", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}
