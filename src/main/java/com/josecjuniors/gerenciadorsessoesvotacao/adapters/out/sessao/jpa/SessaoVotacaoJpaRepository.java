package com.josecjuniors.gerenciadorsessoesvotacao.adapters.out.sessao.jpa;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.SessaoVotacaoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SessaoVotacaoJpaRepository extends SessaoVotacaoRepository, JpaRepository<SessaoVotacao, UUID> {
}
