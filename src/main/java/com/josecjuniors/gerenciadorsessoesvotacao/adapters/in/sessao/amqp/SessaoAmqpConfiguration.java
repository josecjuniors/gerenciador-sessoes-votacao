package com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessaoAmqpConfiguration {

    @Value("${amqp.sessao.exchange}")
    private String sessaoExchange;

    @Value("${amqp.sessao.fechamento.queue}")
    private String sessaoFechamentoQueue;

    @Value("${amqp.sessao.fechamento.dlq}")
    private String sessaoFechamentoDlq;

    @Value("${amqp.sessao.fechamento.routing-key}")
    private String sessaoFechamentoRoutingKey;

    @Bean
    DirectExchange sessaoExchange() {
        return new DirectExchange(sessaoExchange);
    }

    /**
     * Fila principal onde as mensagens de fechamento serão processadas.
     * Esta é a fila de destino final (Dead Letter Queue da fila de espera).
     */
    @Bean
    Queue sessaoFechamentoQueue() {
        return QueueBuilder.durable(sessaoFechamentoQueue).build();
    }

    /**
     * Fila de espera onde as mensagens ficarão "dormindo" até o TTL expirar.
     * Quando expirar, elas serão enviadas para a SESSAO_EXCHANGE com a routing key SESSAO_FECHAMENTO_ROUTING_KEY.
     */
    @Bean
    Queue sessaoFechamentoEsperaQueue() {
        return QueueBuilder.durable(sessaoFechamentoDlq)
                .deadLetterExchange(sessaoExchange)
                .deadLetterRoutingKey(sessaoFechamentoRoutingKey)
                .build();
    }

    @Bean
    Binding bindingSessaoFechamento() {
        return BindingBuilder.bind(sessaoFechamentoQueue())
                .to(sessaoExchange())
                .with(sessaoFechamentoRoutingKey);
    }
}
