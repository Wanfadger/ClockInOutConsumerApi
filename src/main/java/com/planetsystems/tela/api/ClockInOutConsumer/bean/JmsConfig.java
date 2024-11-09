package com.planetsystems.tela.api.ClockInOutConsumer.bean;

import jakarta.jms.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;

@Configuration
@Slf4j
public class JmsConfig {

    @Bean(name = "topicConnectionFactory")
    public JmsListenerContainerFactory<?> topicConnectionFactory(SingleConnectionFactory connectionFactory,
                                                                 DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSubscriptionDurable(true);
        factory.setPubSubDomain(true);
        factory.setConcurrency("1");
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setSessionTransacted(false);
        connectionFactory.setClientId("TELA_CLOCK_IN_TOPIC_ID");
        factory.setClientId("TELA_CLOCK_IN_TOPIC_ID");
        configurer.configure(factory, connectionFactory);

        factory.setErrorHandler(t -> {
            log.info("An error has occurred in the transaction");
            log.error("topicConnectionFactory "+t.getCause().getMessage());
        });

        return factory;
    }


    @Bean(name = "queueConnectionFactory")
    @Primary
    public JmsListenerContainerFactory<?> queueConnectionFactory(SingleConnectionFactory connectionFactory,
                                                                 DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setAutoStartup(true);
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setSessionTransacted(false);
        factory.setConcurrency("1");
        configurer.configure(factory, connectionFactory);

        factory.setErrorHandler(t -> {
            log.info("An error has occurred in the transaction");
            log.error("queueConnectionFactory "+t.getCause().getMessage());
        });

        return factory;
    }

}
