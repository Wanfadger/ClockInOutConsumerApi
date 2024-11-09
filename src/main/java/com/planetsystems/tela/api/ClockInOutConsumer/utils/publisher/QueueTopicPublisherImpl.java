package com.planetsystems.tela.api.ClockInOutConsumer.utils.publisher;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueTopicPublisherImpl implements QueueTopicPublisher {
    private final JmsTemplate jmsTemplate;
    @Qualifier("topicConnectionFactory")
    private final ConnectionFactory topicConnectionFactory;


    @Override
    public void publishTopicData(String telaSchoolNumber, String data) {
        try {
            jmsTemplate.setPubSubDomain(true);
            jmsTemplate.setConnectionFactory(topicConnectionFactory);
            jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
            jmsTemplate.convertAndSend(telaSchoolNumber, data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @Override
    public void publishQueueData(String telaSchoolNumber, String data) {
        try {
            jmsTemplate.setPubSubDomain(true);
            jmsTemplate.setConnectionFactory(topicConnectionFactory);
            jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
            jmsTemplate.convertAndSend(telaSchoolNumber, data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }



}
