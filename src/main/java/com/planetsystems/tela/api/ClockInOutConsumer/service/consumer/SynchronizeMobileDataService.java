package com.planetsystems.tela.api.ClockInOutConsumer.service.consumer;

import com.planetsystems.tela.api.ClockInOutConsumer.model.AcademicTerm;
import com.planetsystems.tela.api.ClockInOutConsumer.model.School;

public interface SynchronizeMobileDataService {

    void publishSchoolData(School school , AcademicTerm academicTerm);
    void publishSchoolClasses(School school, AcademicTerm academicTerm);
    void publishSchoolStaffs(School school, AcademicTerm academicTerm);
    void publishSchoolClockIns(School school, AcademicTerm academicTerm , String dateParam);
    void publishSchoolClockOuts(School school, AcademicTerm academicTerm , String dateParam);
    void publishSubjects(School school, AcademicTerm academicTerm);
    void publishLearnerEnrollments(School school, AcademicTerm academicTerm);
    void publishLearnerAttendance(School school, AcademicTerm academicTerm , String dateParam);
    void publishStaffDailyTimeAttendanceSupervision(School school, AcademicTerm academicTerm , String dateParam);
    void publishStaffDailyTimetableTaskSupervision(School school, AcademicTerm academicTerm , String dateParam);

    void publishStaffDailyTimetablespublishStaffDailyTimetables(School school, AcademicTerm academicTerm , String dateParam);
    void publishDistricts(School school);
    void publishSchoolTimetables(School school, AcademicTerm academicTerm);

}
