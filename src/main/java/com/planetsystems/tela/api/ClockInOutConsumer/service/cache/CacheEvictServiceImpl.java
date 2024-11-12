package com.planetsystems.tela.api.ClockInOutConsumer.service.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheEvictServiceImpl implements CacheEvictService {
    @Override
    public void evictActiveAcademicTerm() {

    }

    @Override
    public void evictSchoolData(String telaSchoolNumber) {

    }

    @Override
    public void evictSchoolClasses(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    public void evictSchoolStaffs(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    public void evictSchoolTermClockIns(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    public void evictSchoolTermClockOuts(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    public void evictSubjects(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    public void evictLearnerEnrollments(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    public void evictLearnerAttendance(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    public void evictStaffDailyTimeAttendanceSupervision(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    public void evictStaffDailyTimetableTaskSupervision(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    public void evictStaffDailyTimetables(String telaSchoolNumber, String academicTerm) {

    }

    @Override
    public void evictDistricts() {

    }

    @Override
    public void evictSchoolTimetables(String telaSchoolNumber, String academicTerm) {

    }
}
