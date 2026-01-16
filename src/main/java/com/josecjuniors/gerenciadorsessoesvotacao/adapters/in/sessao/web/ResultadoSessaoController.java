package com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.web;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.dto.ResultadoSessaoResponse;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.ConsultarResultadoSessaoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/sessoes/{sessaoId}/resultado")
@Tag(name = "Resultados", description = "Consulta de resultados de sessões de votação")
public class ResultadoSessaoController {

    private final ConsultarResultadoSessaoUseCase consultarResultadoSessaoUseCase;

    public ResultadoSessaoController(ConsultarResultadoSessaoUseCase consultarResultadoSessaoUseCase) {
        this.consultarResultadoSessaoUseCase = consultarResultadoSessaoUseCase;
    }

    @GetMapping
    @Operation(summary = "Consultar resultado da sessão", description = "Retorna o resultado consolidado de uma sessão de votação encerrada")
    public ResponseEntity<ResultadoSessaoResponse> consultar(@PathVariable UUID sessaoId) {
        ResultadoSessaoResponse response = consultarResultadoSessaoUseCase.execute(sessaoId);
        return ResponseEntity.ok(response);
    }
}
