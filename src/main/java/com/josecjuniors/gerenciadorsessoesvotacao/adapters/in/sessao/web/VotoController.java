package com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.web;

import com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.web.dto.RegistrarVotoRequest;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.RegistrarVotoUseCase;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.command.RegistrarVotoCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/sessoes/{sessaoId}/votos")
@Tag(name = "Votos", description = "Registro de votos em sessões")
public class VotoController {

    private final RegistrarVotoUseCase registrarVotoUseCase;

    public VotoController(RegistrarVotoUseCase registrarVotoUseCase) {
        this.registrarVotoUseCase = registrarVotoUseCase;
    }

    @PostMapping
    @Operation(summary = "Registrar voto", description = "Registra o voto de um associado em uma sessão aberta")
    public ResponseEntity<Void> registrar(@PathVariable UUID sessaoId, @Valid @RequestBody RegistrarVotoRequest request) {
        RegistrarVotoCommand command = new RegistrarVotoCommand(sessaoId, request.cpfAssociado(), request.voto());
        registrarVotoUseCase.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
