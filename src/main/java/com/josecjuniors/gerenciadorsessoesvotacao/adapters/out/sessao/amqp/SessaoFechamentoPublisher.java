package com.josecjuniors.gerenciadorsessoesvotacao.adapters.out.sessao.amqp;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.port.AgendarFechamentoSessaoPort;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
public class SessaoFechamentoPublisher implements AgendarFechamentoSessaoPort {

    private final RabbitTemplate rabbitTemplate;

    @Value("${amqp.sessao.fechamento.dlq}")
    private String sessaoFechamentoDlq;

    public SessaoFechamentoPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void agendar(UUID sessaoId, Duration tempo) {
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setExpiration(String.valueOf(tempo.toMillis()));
            return message;
        };

        // Enviado para a fila de espera (DLQ) diretamente (usando o default exchange)
        // ou poderíamos usar um exchange específico se configurado.
        // Para fins de exercicio estou enviando direto para a fila pelo nome dela.
        rabbitTemplate.convertAndSend(sessaoFechamentoDlq, (Object) sessaoId.toString(), messagePostProcessor);
    }
}
