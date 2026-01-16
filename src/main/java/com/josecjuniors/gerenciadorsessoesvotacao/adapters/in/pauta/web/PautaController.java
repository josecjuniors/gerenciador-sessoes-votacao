package com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.pauta.web;

import com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.pauta.web.dto.CriarPautaRequest;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.dto.PautaResponse;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.usecase.CriarPautaUseCase;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.usecase.command.CriarPautaCommand;
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
@RequestMapping("/v1/pautas")
@Tag(name = "Pautas", description = "Gerenciamento de Pautas de Votação")
public class PautaController {

    private final CriarPautaUseCase criarPautaUseCase;

    public PautaController(CriarPautaUseCase criarPautaUseCase) {
        this.criarPautaUseCase = criarPautaUseCase;
    }

    @PostMapping
    @Operation(summary = "Criar nova pauta", description = "Cria uma nova pauta para ser votada em sessões futuras")
    public ResponseEntity<PautaResponse> criar(@Valid @RequestBody CriarPautaRequest request) {
        CriarPautaCommand command = new CriarPautaCommand(request.titulo(), request.descricao());
        PautaResponse response = criarPautaUseCase.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
