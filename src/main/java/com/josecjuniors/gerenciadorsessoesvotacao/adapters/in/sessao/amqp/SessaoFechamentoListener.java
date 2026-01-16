package com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.amqp;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.FecharSessaoUseCase;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.command.FecharSessaoCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SessaoFechamentoListener {

    private static final Logger logger = LoggerFactory.getLogger(SessaoFechamentoListener.class);
    private final FecharSessaoUseCase fecharSessaoUseCase;

    public SessaoFechamentoListener(FecharSessaoUseCase fecharSessaoUseCase) {
        this.fecharSessaoUseCase = fecharSessaoUseCase;
    }

    @RabbitListener(queues = "${amqp.sessao.fechamento.queue}")
    public void onSessaoFechamento(String sessaoIdStr) {
        logger.info("Recebida mensagem para fechar sessão: {}", sessaoIdStr);
        try {
            UUID sessaoId = UUID.fromString(sessaoIdStr);
            fecharSessaoUseCase.handle(new FecharSessaoCommand(sessaoId));
        } catch (Exception e) {
            logger.error("Erro ao processar fechamento da sessão: {}", sessaoIdStr, e);
            // Dependendo da estratégia de erro, poderíamos lançar a exceção para reprocessar (com backoff)
            // ou enviar para uma DLQ de erros definitivos.
        }
    }
}
