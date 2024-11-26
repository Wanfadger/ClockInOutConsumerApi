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
    @CacheEvict(value = CacheKeys.ACTIVE_ACADEMIC_TERM , cacheManager = "monthCacheManager" )
    public void evictActiveAcademicTerm() {
     log.info("evictActiveAcademicTerm");
    }

    @Override
    @CacheEvict(value = CacheKeys.SCHOOL , key = "#telaSchoolNumber")
    public void evictSchoolData(String telaSchoolNumber) {
        log.info("evictSchoolData {} {} " , telaSchoolNumber , CacheKeys.SCHOOL);
    }

    @Override
    @CacheEvict(value = CacheKeys.CLASSES , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}")
    public void evictSchoolClasses(String telaSchoolNumber, String academicTerm) {
        log.info("evictSchoolClasses {}  {} " , telaSchoolNumber , academicTerm);
    }

    @Override
    @CacheEvict(value = CacheKeys.STAFFS , key = "#telaSchoolNumber" )
    public void evictSchoolStaffs(String telaSchoolNumber, String academicTerm) {
        log.info("evictSchoolStaffs {}  {} " , telaSchoolNumber , academicTerm);
    }

    @Override
    @CacheEvict(value = CacheKeys.CLOCKINS , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}" )
    public void evictSchoolTermClockIns(String telaSchoolNumber, String academicTerm) {
        log.info("evictSchoolTermClockIns {}  {} " , telaSchoolNumber , academicTerm);
    }

    @Override
    @CacheEvict(value = CacheKeys.CLOCKOUTS , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}" )
    public void evictSchoolTermClockOuts(String telaSchoolNumber, String academicTerm) {
        log.info("evictSchoolTermClockOuts {}  {} " , telaSchoolNumber , academicTerm);
    }

    @Override
    @CacheEvict(value = CacheKeys.SUBJECTS)
    public void evictSubjects(String telaSchoolNumber, String academicTerm) {
        log.info("evictSubjects {}  {} " , telaSchoolNumber , academicTerm);
    }

    @Override
    @CacheEvict(value = CacheKeys.LEARNER_HEADCOUNTS , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}" )
    public void evictLearnerEnrollments(String telaSchoolNumber, String academicTerm) {
     log.info("evictLearnerEnrollments {}  {} " , telaSchoolNumber , academicTerm);
    }

    @Override
    @CacheEvict(value = CacheKeys.LEARNER_ATTENDANCES , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}")
    public void evictLearnerAttendance(String telaSchoolNumber, String academicTerm) {
     log.info("evictLearnerAttendance {}  {} " , telaSchoolNumber , academicTerm);
    }

    @Override
    @CacheEvict(value = CacheKeys.STAFF_DAILY_TIME_ATTENDANCES , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}" )
    public void evictStaffDailyTimeAttendanceSupervision(String telaSchoolNumber, String academicTerm) {
     log.info("evictStaffDailyTimeAttendanceSupervision {}  {} " , telaSchoolNumber , academicTerm);
    }

    @Override
    @CacheEvict(value = CacheKeys.STAFF_DAILY_TASK_SUPERVISIONS , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}")
    public void evictStaffDailyTimetableTaskSupervision(String telaSchoolNumber, String academicTerm) {
     log.info("evictStaffDailyTimetableTaskSupervision {}  {} " , telaSchoolNumber , academicTerm);
    }

    @Override
    @CacheEvict(value = CacheKeys.STAFF_DAILY_TIMETABLES , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}")
    public void evictStaffDailyTimetables(String telaSchoolNumber, String academicTerm) {
     log.info("evictStaffDailyTimetables {}  {} " , telaSchoolNumber , academicTerm);
    }

    @Override
    @CacheEvict(value = CacheKeys.DISTRICTS)
    public void evictDistricts() {
     log.info("evictDistricts  ");
    }

    @Override
    @CacheEvict(value = CacheKeys.SCHOOL_TIMETABLE , key = "{'school='+#telaSchoolNumber+',term='+#academicTerm}")
    public void evictSchoolTimetables(String telaSchoolNumber, String academicTerm) {
        log.info("evictSchoolTimetables {}  {} " , telaSchoolNumber , academicTerm);
    }
}
