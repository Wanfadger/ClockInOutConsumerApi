package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.StaffDailyTimeTable;
import com.planetsystems.tela.api.ClockInOutConsumer.model.StaffDailyTimeTableLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StaffDailyTimeTableRepository extends JpaRepository<StaffDailyTimeTable, String> {

    @Query("""
            SELECT SDT FROM StaffDailyTimeTable  AS SDT
            JOIN FETCH SDT.schoolStaff AS ST
            WHERE SDT.status <> 8 AND SDT.academicTerm.id =:termId AND ST.school.id =:schoolId
            """)
    List<StaffDailyTimeTable> allByTerm_School(String termId, String schoolId);


    @Query("""
            SELECT SDT FROM StaffDailyTimeTable  AS SDT
            JOIN FETCH SDT.schoolStaff AS ST
            WHERE SDT.status <> 8 AND SDT.lessonDate =:localDate AND ST.school.id =:schoolId
            """)
    List<StaffDailyTimeTable> allByDate_School(LocalDate localDate, String schoolId);


//	@EntityGraph(value = "staff-daily-timetable-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<StaffDailyTimeTable> findAllByStatusNot(Status status);
//    List<StaffDailyTimeTable> findAllByStatus(Status status);
//
//    Optional<StaffDailyTimeTable> findByStatusNotAndId(Status status , String id);
//    List<StaffDailyTimeTable> findAllByStatusNotAndAcademicTerm_IdAndSchoolStaff_School_IdAndLessonDate(Status status  , String termId , String schoolId , Date lessonDate);
//
//    Optional<StaffDailyTimeTable> findByStatusNotAndSchoolStaff_IdAndLessonDate(Status status , String schoolStaffId,Date lessonDate);
//
//    List<StaffDailyTimeTable> findAllByStatusNotAndSchoolStaff_IdAndLessonDate(Status status , String schoolStaffId,Date lessonDate);


}
