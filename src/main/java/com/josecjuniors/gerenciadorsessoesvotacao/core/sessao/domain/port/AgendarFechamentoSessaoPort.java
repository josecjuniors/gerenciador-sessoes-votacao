package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.port;

import java.time.Duration;
import java.util.UUID;

public interface AgendarFechamentoSessaoPort {
    void agendar(UUID sessaoId, Duration tempo);
}
