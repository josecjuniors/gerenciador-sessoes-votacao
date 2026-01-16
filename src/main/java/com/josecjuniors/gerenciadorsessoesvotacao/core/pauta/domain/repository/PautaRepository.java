package com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.repository;

import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.Pauta;
import java.util.Optional;
import java.util.UUID;

public interface PautaRepository {
    Pauta save(Pauta pauta);
    Optional<Pauta> findById(UUID id);
}
