package com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josecjuniors.gerenciadorsessoesvotacao.IntegrationTest;
import com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.web.dto.RegistrarVotoRequest;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.Pauta;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.repository.PautaRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.Voto;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.SessaoVotacaoRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class VotoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    private VotoRepository votoRepository;

    @Test
    void deveRegistrarVotoComSucesso() throws Exception {
        // Arrange
        Pauta pauta = pautaRepository.save(new Pauta("Pauta Voto", "Desc"));
        SessaoVotacao sessao = sessaoVotacaoRepository.save(new SessaoVotacao(pauta, 10));
        
        RegistrarVotoRequest request = new RegistrarVotoRequest("12345678901", true);

        // Act & Assert
        mockMvc.perform(post("/v1/sessoes/{sessaoId}/votos", sessao.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveRetornarErroQuandoSessaoNaoExistir() throws Exception {
        // Arrange
        RegistrarVotoRequest request = new RegistrarVotoRequest("12345678901", true);

        // Act & Assert
        mockMvc.perform(post("/v1/sessoes/{sessaoId}/votos", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Sessão não encontrada com ID: " + UUID.randomUUID().toString().substring(0, 8) + "...")); // Validação parcial da mensagem ou apenas status
    }

    @Test
    void deveRetornarErroQuandoCpfJaVotou() throws Exception {
        // Arrange
        Pauta pauta = pautaRepository.save(new Pauta("Pauta Duplicada", "Desc"));
        SessaoVotacao sessao = sessaoVotacaoRepository.save(new SessaoVotacao(pauta, 10));
        
        // Primeiro voto
        votoRepository.save(new Voto(sessao, "12345678901", true));

        RegistrarVotoRequest request = new RegistrarVotoRequest("12345678901", false);

        // Act & Assert
        mockMvc.perform(post("/v1/sessoes/{sessaoId}/votos", sessao.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflito"));
    }

    @Test
    void deveRetornarErroQuandoSessaoEstiverFechada() throws Exception {
        // Arrange
        Pauta pauta = pautaRepository.save(new Pauta("Pauta Fechada", "Desc"));
        SessaoVotacao sessao = new SessaoVotacao(pauta, 10);
        sessao.fecharSessao();
        sessao = sessaoVotacaoRepository.save(sessao);

        RegistrarVotoRequest request = new RegistrarVotoRequest("12345678901", true);

        // Act & Assert
        mockMvc.perform(post("/v1/sessoes/{sessaoId}/votos", sessao.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A sessão de votação está fechada."));
    }

    @Test
    void deveRetornarErroValidacaoCpfInvalido() throws Exception {
        // Arrange
        RegistrarVotoRequest request = new RegistrarVotoRequest("123", true); // CPF curto

        // Act & Assert
        mockMvc.perform(post("/v1/sessoes/{sessaoId}/votos", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.cpfAssociado").exists());
    }
}
