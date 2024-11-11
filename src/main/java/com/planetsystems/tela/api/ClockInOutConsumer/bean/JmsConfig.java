package com.planetsystems.tela.api.ClockInOutConsumer.bean;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Session;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;

@Configuration
//@EnableJms
public class JmsConfig {

    @Bean
    public JmsListenerContainerFactory<?> topicConnectionFactory(SingleConnectionFactory connectionFactory,
                                                                 DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);

        factory.setSubscriptionDurable(true);
        factory.setPubSubDomain(true);
        factory.setConcurrency("1");
        connectionFactory.setClientId("TELA_CLOCK_IN_TOPIC_ID");
        factory.setClientId("TELA_CLOCK_IN_TOPIC_ID"); 


        return factory;


    }
}
