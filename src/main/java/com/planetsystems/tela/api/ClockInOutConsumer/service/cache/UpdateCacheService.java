package com.planetsystems.tela.api.ClockInOutConsumer.service.cache;



import com.planetsystems.tela.api.ClockInOutConsumer.dto.*;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.supervision.StaffDailyAttendanceTaskSupervisionDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.StaffDailyTimetableDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.TimetableDTO;

import java.util.List;

public interface UpdateCacheService {

    void updatedCacheActiveAcademicTerm(AcademicTermDTO academicTermDTO);
    void updatedCacheSchoolData(SchoolDTO schoolDTO);
//    List<ClassDTO> updatedCacheSchoolClasses(SchoolDTO schoolDTO);
//    List<StaffDTO> updatedCacheSchoolStaffs(SchoolDTO schoolDTO);
//    List<ClockInDTO> updatedCacheSchoolTermClockIns(SchoolDTO schoolDTO);
//    List<ClockOutDTO> updatedCacheSchoolTermClockOuts(SchoolDTO schoolDTO);
//    List<IdNameCodeDTO> updatedCacheSubjects(SchoolDTO schoolDT);
//    List<LearnerHeadCountDTO> updatedCacheLearnerEnrollments(SchoolDTO schoolDTO);
//    List<LearnerAttendanceDTO> updatedCacheLearnerAttendance(SchoolDTO schoolDTO);
//    List<StaffDailyTimeAttendanceDTO> updatedCacheStaffDailyTimeAttendanceSupervision(SchoolDTO schoolDTO, String dateParam);
//    List<StaffDailyAttendanceTaskSupervisionDTO> updatedCacheStaffDailyTimetableTaskSupervision(SchoolDTO schoolDTO , String dateParam);
//
//    List<StaffDailyTimetableDTO> updatedCacheStaffDailyTimetables(SchoolDTO schoolDTO);
//    List<DistrictDTO> updatedCacheDistricts();
//
//    TimetableDTO updatedCacheSchoolTimetables(SchoolDTO schoolDTO);
}
