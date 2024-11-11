package com.planetsystems.tela.api.ClockInOutConsumer.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface SchoolDataConsumerService {
    void subscribeLearnerHeadCounts(String learnerHeadCountStr) throws JsonProcessingException;

    void subscribeClassAttendances(String classAttendanceStr) throws JsonProcessingException;

    void subscribeClasses(String classesStr) throws JsonProcessingException;

    void subscribeStaffs(String staffStr) throws JsonProcessingException;

    void subscribeStaffDailyTimeAttendances(String taffDailyTimeAttendanceStr) throws JsonProcessingException;

    void subscribeUpdateTimetableLessons(String updateTimetableLessonStr) throws JsonProcessingException;
    void subscribeStaffDailyTimetables(String staffDailyTimetableStr) throws JsonProcessingException;
    void subscribeStaffDailyTaskSupervisions(String staffDailyTaskSupervisionStr) throws JsonProcessingException;
    void subscribeSchoolCoordinates(String staffSchoolCoordinateStr) throws JsonProcessingException;

}
