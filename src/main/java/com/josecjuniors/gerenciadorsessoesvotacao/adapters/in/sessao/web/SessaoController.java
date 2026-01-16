package com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.web;

import com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.web.dto.AbrirSessaoRequest;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.dto.SessaoResponse;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.AbrirSessaoUseCase;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.command.AbrirSessaoCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sessoes")
@Tag(name = "Sessões", description = "Gerenciamento de Sessões de Votação")
public class SessaoController {

    private final AbrirSessaoUseCase abrirSessaoUseCase;

    public SessaoController(AbrirSessaoUseCase abrirSessaoUseCase) {
        this.abrirSessaoUseCase = abrirSessaoUseCase;
    }

    @PostMapping
    @Operation(summary = "Abrir sessão de votação", description = "Abre uma nova sessão de votação para uma pauta específica")
    public ResponseEntity<SessaoResponse> abrir(@Valid @RequestBody AbrirSessaoRequest request) {
        AbrirSessaoCommand command = new AbrirSessaoCommand(request.pautaId(), request.tempoEmMinutos());
        SessaoResponse response = abrirSessaoUseCase.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
