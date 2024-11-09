package com.planetsystems.tela.api.ClockInOutConsumer.utils.publisher;

public interface QueueTopicPublisher {
     void  publishTopicData(String telaSchoolNumber , String data);
     void  publishQueueData(String telaSchoolNumber , String data);
}
