package com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josecjuniors.gerenciadorsessoesvotacao.IntegrationTest;
import com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.web.dto.AbrirSessaoRequest;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.Pauta;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.repository.PautaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class SessaoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PautaRepository pautaRepository;

    @Test
    void deveAbrirSessaoComSucesso() throws Exception {
        // Arrange
        Pauta pauta = new Pauta("Pauta Teste", "Descrição Teste");
        pauta = pautaRepository.save(pauta);

        AbrirSessaoRequest request = new AbrirSessaoRequest(pauta.getId(), 10);

        // Act & Assert
        mockMvc.perform(post("/v1/sessoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.pautaId").value(pauta.getId().toString()))
                .andExpect(jsonPath("$.status").value("ABERTA"))
                .andExpect(jsonPath("$.dataAbertura").exists())
                .andExpect(jsonPath("$.dataFechamento").exists());
    }

    @Test
    void deveRetornarErroQuandoPautaNaoExistir() throws Exception {
        // Arrange
        AbrirSessaoRequest request = new AbrirSessaoRequest(UUID.randomUUID(), 10);

        // Act & Assert
        mockMvc.perform(post("/v1/sessoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarErroValidacaoQuandoPautaIdForNulo() throws Exception {
        // Arrange
        AbrirSessaoRequest request = new AbrirSessaoRequest(null, 10);

        // Act & Assert
        mockMvc.perform(post("/v1/sessoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Erro de Validação"))
                .andExpect(jsonPath("$.details.pautaId").value("O ID da pauta é obrigatório"));
    }
}
