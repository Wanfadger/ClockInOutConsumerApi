package com.planetsystems.tela.api.ClockInOutConsumer.service.consumer;

import com.planetsystems.tela.api.ClockInOutConsumer.model.*;

import java.util.List;

public interface SynchronizeMobileDataService {

    void publishSchoolData(School school , AcademicTerm academicTerm);
    void publishSchoolClasses(School school, AcademicTerm academicTerm);
    void publishSchoolStaffs(School school, AcademicTerm academicTerm);
    void publishSchoolClockIns(School school, AcademicTerm academicTerm);
    void publishSubjects(School school, AcademicTerm academicTerm);
    void publishLearnerEnrollments(School school, AcademicTerm academicTerm);



}
