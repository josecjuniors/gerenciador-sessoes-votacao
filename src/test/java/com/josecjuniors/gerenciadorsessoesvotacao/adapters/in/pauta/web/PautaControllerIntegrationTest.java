package com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.pauta.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josecjuniors.gerenciadorsessoesvotacao.IntegrationTest;
import com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.pauta.web.dto.CriarPautaRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class PautaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarPautaComSucesso() throws Exception {
        // Arrange
        CriarPautaRequest request = new CriarPautaRequest("Nova Pauta", "Descrição da pauta");

        // Act & Assert
        mockMvc.perform(post("/v1/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Nova Pauta"))
                .andExpect(jsonPath("$.descricao").value("Descrição da pauta"));
    }

    @Test
    void deveRetornarErroQuandoTituloForVazio() throws Exception {
        // Arrange
        CriarPautaRequest request = new CriarPautaRequest("", "Descrição da pauta");

        // Act & Assert
        mockMvc.perform(post("/v1/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
