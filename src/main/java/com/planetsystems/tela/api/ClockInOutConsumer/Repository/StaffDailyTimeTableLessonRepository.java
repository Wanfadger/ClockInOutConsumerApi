package com.planetsystems.tela.api.ClockInOutConsumer.Repository;


import com.planetsystems.tela.api.ClockInOutConsumer.model.StaffDailyTimeTable;
import com.planetsystems.tela.api.ClockInOutConsumer.model.StaffDailyTimeTableLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffDailyTimeTableLessonRepository extends JpaRepository<StaffDailyTimeTableLesson, String> {
    @Query("""
            SELECT SDTL FROM StaffDailyTimeTableLesson  AS SDTL
            JOIN FETCH SDTL.schoolClass AS SC
            JOIN FETCH SDTL.subject AS SUB
            WHERE SDTL.status <> 8 AND SC.status <> 8 AND SUB.status <> 8 
            
            AND SDTL.staffDailyTimeTable IN :staffDailyTimeTables
            """)
    List<StaffDailyTimeTableLesson> allIn(List<StaffDailyTimeTable> staffDailyTimeTables);

//    List<StaffDailyTimeTableLesson> findAllByStatusNot(Status status);
//    List<StaffDailyTimeTableLesson> findAllByStatus(Status status);
//
//    Optional<StaffDailyTimeTableLesson> findByStatusNotAndId(Status status , String id);
//
//    @EntityGraph(value = "staff-daily-timeTable-lesson-detail-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<StaffDailyTimeTableLesson> findAllByStatusNotAndStaffDailyTimeTable_SchoolStaff_IdAndLessonDate(Status status , String staffId , LocalDate lessonDate);
//
//    @EntityGraph(value = "staff-daily-timeTable-lesson-detail-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<StaffDailyTimeTableLesson> findAllByStatusNotAndSchoolClass_School_IdAndLessonDate(Status status , String staffId , LocalDate lessonDate);
//
//    @EntityGraph(value = "staff-daily-timeTable-lesson-detail-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<StaffDailyTimeTableLesson> findAllByStatusNotAndStaffDailyTimeTable_IdAndStaffDailyTimeTable_SchoolStaff_IdAndLessonDate(Status status , String staffDailyTimetableId , String staffId , LocalDate lessonDate);
//
//    @EntityGraph(value = "staff-daily-timeTable-lesson-detail-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<StaffDailyTimeTableLesson> findByStatusNotAndStaffDailyTimeTable_SchoolStaff_IdAndSchoolClass_IdAndSubject_IdAndLessonDate
//    (Status status , String staffId , String classId , String subjectId , Date lessonDate);
//
//
//    @EntityGraph(value = "staff-daily-timeTable-lesson-detail-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<StaffDailyTimeTableLesson> findAllByStatusNotAndStaffDailyTimeTable_SchoolStaff_IdAndSchoolClass_IdAndSubject_IdAndLessonDate
//            (Status status , String staffId , String classId , String subjectId , Date lessonDate);
//
//    Optional<StaffDailyTimeTableLesson> findByStatusNotAndStaffDailyTimeTable_SchoolStaff_IdAndSchoolClass_IdAndSubject_IdAndLessonDateAndStartTimeAndEndTime
//            (Status status , String staffId , String classId , String subjectId , Date lessonDate, LocalTime startTime,
//      LocalTime endTime);
//
//
//    List<StaffDailyTimeTableLesson> findAllByStatusNotAndStaffDailyTimeTable_SchoolStaff_IdAndSchoolClass_IdAndSubject_IdAndLessonDateAndStartTimeAndEndTime
//            (Status status , String staffId , String classId , String subjectId , Date lessonDate, LocalTime startTime,
//             LocalTime endTime);
//

}
