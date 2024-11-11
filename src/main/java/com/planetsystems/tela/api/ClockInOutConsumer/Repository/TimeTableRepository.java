package com.planetsystems.tela.api.ClockInOutConsumer.Repository;


import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.IdProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, String> {


    Optional<IdProjection> findBySchool_IdAndAcademicTerm_Id(String schoolId,String termId);

    //
//	//@EntityGraph(value = "timeTable-detail-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<TimeTable> findAllByStatusNot(Status status);
//
//    //@EntityGraph(value = "timeTable-detail-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<TimeTable> findByStatusNotAndAcademicTerm_IdAndSchool_Id(Status status , String termId , String schoolId);
//
//    Optional<TimeTable> findByStatusNotAndId(Status status, String id);
//
//   // @EntityGraph(value = "timeTable-detail-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    boolean existsByStatusNotAndAcademicTerm_IdAndSchool_Id(Status status , String termId , String schoolId);
//
//    @Query(value = "SELECT T.id FROM TimeTable T WHERE T.status <>:status AND T.academicTerm.id =:term AND T.school.id =:school ")
//    Optional<String> findIdByAcademicTermAndSchool(Status status , String term , String school);
//
//    List<TimeTable> findByStatusNotAndAcademicTerm_IdAndSchool_District_Id(Status status, String termId, String dstrictId);
//
//    @EntityGraph(value = "timeTable-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<TimeTable> findByStatusNotAndAcademicTerm_Id(Status status, String termId);
//
//    //
//    @EntityGraph(value = "timeTable-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<TimeTable> findByStatusNotAndAcademicTerm_IdAndSchool_District_Region_IdAndSchool_SchoolOwnership(Status status, String termId, String regionId, SchoolOwnership schoolOwnership);
//
//    @Query(nativeQuery = true ,
//            value = "SELECT COUNT(*) FROM \"TimeTables\" AS T \n" +
//                    "INNER JOIN \"TimeTableLessons\" AS TL ON TL.\"timeTable_id\" = T.id\n" +
//                    "WHERE T.status <>:#{#status.ordinal()} AND TL.status <>:#{#status.ordinal()} \n" +
//                    "AND T.\"academicTerm_id\" =:academicTerm  AND T.school_id =:school")
//    long countTimeTableLessonsByAcademicTerm_School(Status status, String academicTerm, String school);
//
//    @Query(nativeQuery = true ,
//            value = "SELECT * FROM \"TimeTables\" AS T \n" +
//                    "INNER JOIN \"TimeTableLessons\" AS TL ON TL.\"timeTable_id\" = T.id\n" +
//                    "WHERE T.status <>:#{#status.ordinal()} AND TL.status <>:#{#status.ordinal()} \n" +
//                    "AND T.\"academicTerm_id\" =:academicTerm  AND T.school_id =:school")
//
//    List<TimeTableProjection> findALLTimeTableByAcademicTerm_SchoolWithTimeTableLesson(Status status, String academicTerm, String school);
//
//
//    @Query(value = "SELECT T  FROM TimeTable T " +
//            "JOIN FETCH T.timeTableLessons TL " +
//            "JOIN FETCH TL.schoolClass SC " +
//            "JOIN FETCH TL.subject SUB " +
//            "JOIN FETCH TL.schoolStaff ST " +
//            "JOIN FETCH ST.generalUserDetail G " +
//            "WHERE T.status <>:status AND TL.status <>:status AND T.academicTerm.id =:academicTerm AND T.school.id =:school  ")
//    //ORDER BY TL.startTime ASC
//    Optional<TimeTable> findTimeTableByAcademicTerm_SchoolWithTimeTableLesson(Status status, String academicTerm, String school);
}
