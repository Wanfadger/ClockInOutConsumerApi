package com.planetsystems.tela.api.ClockInOutConsumer.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.jms.Message;

public interface SchoolDataConsumerService {
    void subscribeLearnerHeadCounts(String learnerHeadCountStr , Message message) throws JsonProcessingException;

    void subscribeClassAttendances(String classAttendanceStr, Message message) throws JsonProcessingException;

    void subscribeClasses(String classesStr, Message message) throws JsonProcessingException;

    void subscribeStaffs(String staffStr, Message message) throws JsonProcessingException;

    void subscribeStaffDailyTimeAttendances(String staffDailyTimeAttendanceStr, Message message) throws JsonProcessingException;

    void subscribeUpdateTimetableLessons(String updateTimetableLessonStr, Message message) throws JsonProcessingException;
    void subscribeStaffDailyTimetables(String staffDailyTimetableStr, Message message) throws JsonProcessingException;
    void subscribeStaffDailyTaskSupervisions(String staffDailyTaskSupervisionStr, Message message) throws JsonProcessingException;
    void subscribeSchoolCoordinates(String staffSchoolCoordinateStr, Message message) throws JsonProcessingException;

}
