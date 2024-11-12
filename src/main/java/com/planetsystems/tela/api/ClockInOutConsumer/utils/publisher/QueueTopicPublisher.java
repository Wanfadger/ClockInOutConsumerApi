package com.planetsystems.tela.api.ClockInOutConsumer.utils.publisher;

import jakarta.jms.Message;

public interface QueueTopicPublisher {
     void  publishTopicData(String telaSchoolNumber , String data);
     void  publishQueueData(String telaSchoolNumber , String data);
      void deleteMessageFromQueue(String queueName, Message message);

      void browseQueueMessages(String queueName, Message message);
      void browseDeleteQueueMessages(String queueName, Message message);
}
