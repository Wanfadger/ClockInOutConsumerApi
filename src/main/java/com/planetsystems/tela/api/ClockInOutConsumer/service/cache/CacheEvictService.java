package com.planetsystems.tela.api.ClockInOutConsumer.service.cache;


public interface CacheEvictService {

    void evictActiveAcademicTerm();
    void evictSchoolData(String telaSchoolNumber);
    void evictSchoolClasses(String telaSchoolNumber , String academicTerm);
    void evictSchoolStaffs(String telaSchoolNumber , String academicTerm);
    void evictSchoolTermClockIns(String telaSchoolNumber , String academicTerm);
    void evictSchoolTermClockOuts(String telaSchoolNumber , String academicTerm);
    void evictSubjects(String telaSchoolNumber , String academicTerm);
    void evictLearnerEnrollments(String telaSchoolNumber , String academicTerm);
    void evictLearnerAttendance(String telaSchoolNumber , String academicTerm);
    void evictStaffDailyTimeAttendanceSupervision(String telaSchoolNumber , String academicTerm);
    void evictStaffDailyTimetableTaskSupervision(String telaSchoolNumber , String academicTerm);
    void evictStaffDailyTimetables(String telaSchoolNumber , String academicTerm);
    void evictDistricts();
    void evictSchoolTimetables(String telaSchoolNumber , String academicTerm);
}
