package com.josecjuniors.gerenciadorsessoesvotacao.adapters.out.pauta.jpa;

import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.Pauta;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.repository.PautaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PautaJpaRepository extends PautaRepository, JpaRepository<Pauta, UUID> {
}
