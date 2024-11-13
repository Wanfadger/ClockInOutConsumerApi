package com.planetsystems.tela.api.ClockInOutConsumer.service.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheEvictServiceImpl implements CacheEvictService {
    @Override
    @CacheEvict(value = CacheKeys.ACTIVE_ACADEMIC_TERM , cacheManager = "monthCacheManager" , allEntries = true)
    public void evictActiveAcademicTerm() {

    }

    @Override
    @CacheEvict(value = CacheKeys.SCHOOL , key = "#telaSchoolNumber" , allEntries = true)
    public void evictSchoolData(String telaSchoolNumber) {

    }

    @Override
    @CacheEvict(value = CacheKeys.CLASSES , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}" , allEntries = true)
    public void evictSchoolClasses(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    @CacheEvict(value = CacheKeys.STAFFS , key = "#telaSchoolNumber" , allEntries = true)
    public void evictSchoolStaffs(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    @CacheEvict(value = CacheKeys.CLOCKINS , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}" , allEntries = true)
    public void evictSchoolTermClockIns(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    @CacheEvict(value = CacheKeys.CLOCKOUTS , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}" , allEntries = true)
    public void evictSchoolTermClockOuts(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    @CacheEvict(value = CacheKeys.SUBJECTS)
    public void evictSubjects(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    @CacheEvict(value = CacheKeys.LEARNER_HEADCOUNTS , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}" , allEntries = true)
    public void evictLearnerEnrollments(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    @CacheEvict(value = CacheKeys.LEARNER_ATTENDANCES , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}" , allEntries = true)
    public void evictLearnerAttendance(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    @CacheEvict(value = CacheKeys.STAFF_DAILY_TIME_ATTENDANCES , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}" , allEntries = true)
    public void evictStaffDailyTimeAttendanceSupervision(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    @CacheEvict(value = CacheKeys.STAFF_DAILY_TASK_SUPERVISIONS , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}", allEntries = true)
    public void evictStaffDailyTimetableTaskSupervision(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    @CacheEvict(value = CacheKeys.STAFF_DAILY_TIMETABLES , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}", allEntries = true)
    public void evictStaffDailyTimetables(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    @CacheEvict(value = CacheKeys.DISTRICTS)
    public void evictDistricts() {

    }

    @Override
    @CacheEvict(value = CacheKeys.SCHOOL_TIMETABLE , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}", allEntries = true)
    public void evictSchoolTimetables(String telaSchoolNumber, String academicTerm) {

    }
}
