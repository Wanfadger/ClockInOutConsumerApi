package com.planetsystems.tela.api.ClockInOutConsumer.service.cache;



import com.planetsystems.tela.api.ClockInOutConsumer.dto.*;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.supervision.StaffDailyAttendanceTaskSupervisionDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.StaffDailyTimetableDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.TimetableDTO;

import java.util.List;

public interface CacheService {

    AcademicTermDTO cacheActiveAcademicTerm();
    SchoolDTO cacheSchoolData(String telaSchoolNumber);
    List<ClassDTO> cacheSchoolClasses(SchoolDTO schoolDTO);
    List<StaffDTO> cacheSchoolStaffs(SchoolDTO schoolDTO);
    List<ClockInDTO> cacheSchoolTermClockIns(SchoolDTO schoolDTO);
    List<ClockOutDTO> cacheSchoolTermClockOuts(SchoolDTO schoolDTO);
    List<IdNameCodeDTO> cacheSubjects(SchoolDTO schoolDT);
    List<LearnerHeadCountDTO> cacheLearnerEnrollments(SchoolDTO schoolDTO);
    List<LearnerAttendanceDTO> cacheLearnerAttendance(SchoolDTO schoolDTO);
    List<StaffDailyTimeAttendanceDTO> cacheStaffDailyTimeAttendanceSupervision(SchoolDTO schoolDTO, String dateParam);
    List<StaffDailyAttendanceTaskSupervisionDTO> cacheStaffDailyTimetableTaskSupervision(SchoolDTO schoolDTO , String dateParam);

    List<StaffDailyTimetableDTO> cacheStaffDailyTimetables(SchoolDTO schoolDTO);
    List<DistrictDTO> cacheDistricts();

    TimetableDTO cacheSchoolTimetables(SchoolDTO schoolDTO);
}
