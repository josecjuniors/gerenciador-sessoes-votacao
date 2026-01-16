package com.josecjuniors.gerenciadorsessoesvotacao.adapters.out.sessao.jpa;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.Voto;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.VotoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VotoJpaRepository extends VotoRepository, JpaRepository<Voto, UUID> {
}
