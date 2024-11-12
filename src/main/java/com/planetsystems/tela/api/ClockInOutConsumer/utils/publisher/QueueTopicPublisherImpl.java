package com.planetsystems.tela.api.ClockInOutConsumer.utils.publisher;

import jakarta.jms.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueTopicPublisherImpl implements QueueTopicPublisher {

//    @Value("${spring.artemis.user}")
//    private String username;
//
//    @Value("${spring.artemis.password}")
//    private String password;
//
//    @Value("${spring.artemis.broker}")
//    private String brokerUrl;

    private final JmsTemplate jmsTemplate;
    final ConnectionFactory connectionFactory;
//    @Qualifier("topicConnectionFactory")
//    private final ConnectionFactory topicConnectionFactory;
//
//    @Qualifier("queueConnectionFactory")
//    private final ConnectionFactory queueConnectionFactory;


    @Override
    public void publishTopicData(String telaSchoolNumber, String data) {
        try {
            jmsTemplate.setPubSubDomain(true);
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
            jmsTemplate.setPubSubDomain(false);
            jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
            jmsTemplate.convertAndSend(telaSchoolNumber, data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @Override
    public void deleteMessageFromQueue(String queueName, Message message) {
        log.info("deleteMessageFromQueue {} " , message);
        try (Session session = connectionFactory.createConnection().createSession(false, Session.AUTO_ACKNOWLEDGE)){

            session.createConsumer(session.createQueue(queueName));

            MessageConsumer messageConsumer = session.createConsumer(session.createQueue(queueName),
                    "JMSMessageID = '" + message.getJMSMessageID() + "'");
            TextMessage textMessage = (TextMessage) messageConsumer.receiveNoWait();
            if (!ObjectUtils.isEmpty(textMessage))
                log.info("Deleted message with ID :{}" + message.getJMSMessageID());
            else
                log.info("Message with ID  :{}" + message.getJMSMessageID() + " not found.");
            // Close the MessageConsumer
            messageConsumer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void browseQueueMessages(String queueName, Message message) {
        log.info("browseQueueMessages {} " , message);
        try (Session session = connectionFactory.createConnection().createSession(false, Session.AUTO_ACKNOWLEDGE)){

            QueueBrowser browser = session.createBrowser(session.createQueue(queueName));
            Enumeration<?> messages = browser.getEnumeration();
            log.info("messages {} " , messages.nextElement());
            int count = 0;
            while (messages.hasMoreElements()) {
                count++;
                TextMessage messageInTheQueue = (TextMessage) messages.nextElement();
                log.info("message text {} " , messageInTheQueue.getText());
                log.info("Message :{}" + count + " in the queue:");
                log.info("Queue message JMS id :{}", messageInTheQueue.getJMSMessageID());
//                if (messageInTheQueue.getJMSMessageID().equals(message.getJMSMessageID())) {
//                    // Delete the message from the queue based on its ID
//                    deleteMessageFromQueue(session, queuename, messageInTheQueue.getJMSMessageID());
//                }
            }

            browser.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void browseDeleteQueueMessages(String queueName, Message message) {
        AtomicBoolean foundMatchingMessage = new AtomicBoolean(false);
        jmsTemplate.browse(queueName , (session, browser) -> {
            Queue queue = browser.getQueue();
            String queueQueueName = queue.getQueueName();
            log.info("browseDeleteQueueMessages {}  {}"  ,queueQueueName , message);
            Enumeration enumeration = browser.getEnumeration();
            while (enumeration.hasMoreElements()){
                TextMessage textMessage = (TextMessage) enumeration.nextElement();
                String messageText = textMessage.getText();
                String jmsMessageID = textMessage.getJMSMessageID();

                log.info("browseDeleteQueueMessages {}  {}"  ,queueQueueName , textMessage);



            }

            return null;
        });

//        jmsTemplate.browse(queueName, (session, browser) -> {
//            Enumeration<?> enumeration = browser.getEnumeration();
//            while (enumeration.hasMoreElements()) {
//                Message message = (Message) enumeration.nextElement();
//                if (message instanceof TextMessage) {
//                    TextMessage textMessage = (TextMessage) message;
//                    try {
//                        String msg = textMessage.getText();
//                        SyncClockIn syncClockIn = commonUtils.decryptDTO(msg, SyncClockIn.class);
//
//                        if (syncClockIn.getClockInDate().toString().equals(clockInDate) &&
//                                syncClockIn.getEmployeeNo().equals(staffCode)) {
//                            log.warn("Clocked In already.... :{}", syncClockIn.getEmployeeNo());
//                            foundMatchingMessage.set(true);
//                        }
//
//                        System.out.println("Browsing message: " + syncClockIn.toString());
//                    } catch (JMSException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return null;
//        });

    }


}
