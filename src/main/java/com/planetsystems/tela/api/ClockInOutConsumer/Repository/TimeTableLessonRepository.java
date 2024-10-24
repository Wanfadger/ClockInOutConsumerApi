package com.planetsystems.tela.api.ClockInOutConsumer.Repository;


import com.planetsystems.tela.api.ClockInOutConsumer.model.TimeTableLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeTableLessonRepository extends JpaRepository<TimeTableLesson, String> {

    @Query("""
            SELECT TL FROM TimeTableLesson AS TL 
            WHERE TL.status <> 8 AND TL.timeTable.id =:timetableId
            """)
    List<TimeTableLesson> allByTimetable(String timetableId);

    @Query("""
            SELECT TL FROM TimeTableLesson AS TL 
            JOIN FETCH TL.timeTable AS T
            WHERE TL.status <> 8 AND T.status <> 8 
            AND T.academicTerm.id =:termId AND T.school.id =:schoolId AND TL.schoolClass.id =:classId  
            """)
    List<TimeTableLesson> allByTerm_School_Class( String termId, String schoolId , String classId);

    //    List<TimeTableLesson> findAllByStatusNot(Status status);
//
//    List<TimeTableLesson> findAllByStatus(Status status);
//
//    @EntityGraph(value = "timeTable-lesson-detail-graph", type = EntityGraph.EntityGraphType.FETCH)
//    Optional<TimeTableLesson> findByStatusNotAndId(Status status, String id);
//
//    @EntityGraph(value = "timeTable-lesson-detail-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<TimeTableLesson> findAllByStatusNotAndTimeTable_Id(Status status, String timeTableId);
//
//    @EntityGraph(value = "timeTable-lesson-detail-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<TimeTableLesson> findAllByStatusNotAndTimeTable_School_IdAndTimeTable_AcademicTerm_Id(Status status, String schoolId, String termId);
//
//    @EntityGraph(value = "timeTable-lesson-detail-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<TimeTableLesson> findAllByStatusNotAndSchoolClass_Id(Status status, String classId);
//
//
//
//    @Query(value = """
//            SELECT TL FROM TimeTableLesson  AS TL
//            JOIN FETCH TL.schoolClass AS SC
//            LEFT JOIN FETCH TL.subject AS SUB
//            LEFT JOIN FETCH TL.schoolStaff AS ST
//            JOIN FETCH ST.generalUserDetail AS G
//            WHERE TL.status <>:status AND SUB.status <>:status AND SC.status <> :status AND ST.status <>:status AND G.status <>:status
//            AND TL.timeTable.id =:timetableId AND SC.id=:classId
//            """)
//    List<TimeTableLesson> findAllByTimeTable_IdAndSchoolClass_Id(Status status, String timetableId, String classId);
//
//    @Query(value = """
//            SELECT TL FROM TimeTableLesson  AS TL
//            WHERE
//             TL.schoolStaff = '' OR TL.subject = ''
//             AND TL.timeTable.id =:timetableId AND TL.schoolClass.id=:classId
//            """)
//    List<TimeTableLesson> findAllWithStringSubject_Staff(String timetableId, String classId);


}
