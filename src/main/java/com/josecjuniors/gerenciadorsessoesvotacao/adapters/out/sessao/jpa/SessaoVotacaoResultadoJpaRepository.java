package com.josecjuniors.gerenciadorsessoesvotacao.adapters.out.sessao.jpa;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacaoResultado;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.SessaoVotacaoResultadoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SessaoVotacaoResultadoJpaRepository extends SessaoVotacaoResultadoRepository, JpaRepository<SessaoVotacaoResultado, UUID> {
}
