package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.LearnerAttendance;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Repository
public interface LearnerAttendanceRepository extends JpaRepository<LearnerAttendance, String> {


    @Query(value = """
            SELECT LA FROM LearnerAttendances  AS LA
            JOIN FETCH LA.schoolClass AS SC
            WHERE LA.status <> 8 AND SC.status <> 8 
            AND LA.academicTerm.id =:termId AND SC.school.id =:schoolId
            """)
    List<LearnerAttendance> allByTerm_School(String termId, String schoolId);


    @Query(value = """
            SELECT LA FROM LearnerAttendances  AS LA
            JOIN FETCH LA.schoolClass AS SC
            WHERE LA.status <> 8 AND SC.status <> 8 
            AND LA.attendanceDate =:attendanceDate AND SC.school.id =:schoolId
            """)
    List<LearnerAttendance> allByDate_School(LocalDate attendanceDate, String schoolId);




//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerAttendance> findAllByStatusNot(Status status);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    Optional<LearnerAttendance> findByStatusNotAndId(Status status, String id);
//
//    boolean existsByStatusNotAndSchoolClass_IdAndAttendanceDate(Status status, String classId, LocalDate date);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    Optional<LearnerAttendance> findByStatusNotAndSchoolClass_IdAndAttendanceDate(Status status, String classId, LocalDate date);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerAttendance> findAllByStatusNotAndAcademicTerm_IdAndSchoolClass_IdAndAttendanceDate(Status status, String academicTermId, String schoolClassId, LocalDate attendanceDate);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerAttendance> findAllByStatusNotAndAttendanceDateAndAcademicTerm_IdAndSchoolClass_School_Id(Status status, LocalDate attendanceDate, String termId, String schoolId);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerAttendance> findAllByStatusNotAndAcademicTerm_Id(Status status, String termId);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerAttendance> findAllByStatusNotAndAttendanceDateAndAcademicTerm_Id(Status status, LocalDate attendanceDate, String termId);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerAttendance> findAllByStatusNotAndAcademicTerm_IdAndSchoolClass_School_District_Region_Id(Status status, String termId, String regionId);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerAttendance> findAllByStatusNotAndAcademicTerm_IdAndSchoolClass_School_Id(Status status, String termId, String schoolId);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerAttendance> findAllByStatusNotAndAttendanceDateAndSchoolClass_School_Id(Status status, LocalDate attendanceDate, String schoolId);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerAttendance> findAllByStatusNotAndSchoolClass_School_IdAndAttendanceDateBetween(Status status , String schoolId, LocalDate startDate, LocalDate endDate);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerAttendance> findAllByStatusNotAndSchoolClass_School_Id(Status status, String schoolId);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerAttendance> findAllByStatusNotAndAttendanceDate(Status status, LocalDate attendanceDate);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerAttendance> findAllByStatusNotAndAttendanceDateAndSchoolClass_School_District_Id(Status status, LocalDate attendanceDate, String districtId);
//
//    @EntityGraph(value = "learnerAttendance-with-staffDetails-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerAttendance> findAllByStatusNotAndAcademicTerm_AcademicYear_IdAndSchoolClass_School_Id(Status status, String yearId, String schoolId);
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT \n" +
//                    "\"LearnerAttendances\".id as id,\n" +
//                    "\"LearnerAttendances\".\"academicTerm_id\" as academicTermId,\n" +
//                    "\"girlsAbsent\",\n" +
//                    "\"girlsPresent\",\n" +
//                    "\"boysPresent\",\n" +
//                    "\"boysAbsent\",\n" +
//                    "\"Schools\".id as schoolId,\n" +
//                    "\"Schools\".name as schoolName,\n" +
//                    "\"Schools\".\"schoolLevel\" as schoolLevel,\n" +
//                    "\"Schools\".\"schoolOwnership\" as schoolOwnership\n" +
//                    "FROM  \"LearnerAttendances\"\n" +
//                    "INNER JOIN \"SchoolClasses\" on \"LearnerAttendances\".\"schoolClass_id\"=\"SchoolClasses\".id\n" +
//                    "INNER JOIN \"Schools\" ON \"SchoolClasses\".school_id=\"Schools\".id\n" +
//                    "WHERE \"attendanceDate\" =:date \n" +
//                    "and \"Schools\".district_id=:districtId \n" +
//                    "and \"LearnerAttendances\".\"status\" !=:#{#status.ordinal()}\n" +
//                    "and \"Schools\".\"schoolOwnership\"=:#{#schoolOwnership.ordinal()} \n"
//    )
//    List<LearnerAttendanceSummaryProjection> findDistrictSchoolLearnerAttendanceSummary
//            (@Param("status") Status status, @Param("districtId") String districtId, @Param("date") LocalDate date, @Param("schoolOwnership") SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT \n" +
//                    "\"LearnerAttendances\".id as id,\n" +
//                    "\"LearnerAttendances\".\"academicTerm_id\" as academicTermId,\n" +
//                    "\"girlsAbsent\",\n" +
//                    "\"girlsPresent\",\n" +
//                    "\"boysPresent\",\n" +
//                    "\"boysAbsent\",\n" +
//                    "\"Schools\".id as schoolId,\n" +
//                    "\"Schools\".name as schoolName,\n" +
//                    "\"Schools\".\"schoolLevel\" as schoolLevel,\n" +
//                    "\"Schools\".\"schoolOwnership\" as schoolOwnership\n" +
//                    "\"Districts\".\"id\" as districtId,\n" +
//                    "\"Districts\".\"name\" as districtName,\n" +
//                    "FROM  \"LearnerAttendances\"\n" +
//                    "INNER JOIN \"SchoolClasses\" on \"LearnerAttendances\".\"schoolClass_id\"=\"SchoolClasses\".id\n" +
//                    "INNER JOIN \"Schools\" ON \"SchoolClasses\".school_id=\"Schools\".id\n" +
//                    "INNER JOIN \"Districts\" on \"Schools\".district_id=\"Districts\".id\n" +
//                    "WHERE \"attendanceDate\" =:date \n" +
//                    "and \"LearnerAttendances\".\"status\" !=:#{#status.ordinal()}\n" +
//                    "and \"Schools\".\"schoolOwnership\"=:#{#schoolOwnership.ordinal()} \n"
//    )
//    List<LearnerAttendanceSummaryProjection> findSchoolLearnerAttendanceSummary
//            (@Param("status") Status status, @Param("date") LocalDate date, @Param("schoolOwnership") SchoolOwnership schoolOwnership);
//
//
////    @Query("SELECT L FROM  LearnerAttendances  L " +
////            " JOIN FETCH L.schoolClass SC " +
////            " JOIN FETCH SC.school S " +
////            " WHERE L.status <>:status AND S.district.id =:districtId AND L.attendanceDate =:attendanceDate AND S.schoolLevel =:schoolLevel AND S.schoolOwnership =:schoolOwnership")
//
//    @Query(nativeQuery = true ,
//            value = "SELECT L.\"boysAbsent\" , L.\"boysPresent\" , L.\"girlsAbsent\" , L.\"girlsPresent\" , s.id AS schoolId , S.\"name\" as schoolName , " +
//                    "SC.id as schoolClassId , SC.\"name\" as SchoolClassName FROM \"LearnerAttendances\" AS L\n" +
//                    "INNER JOIN \"SchoolClasses\" AS SC ON SC.id = L.\"schoolClass_id\"\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = SC.school_id\n" +
//                    "WHERE L.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND SC.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND S.district_id =:districtId AND L.\"attendanceDate\" =:attendanceDate "
//    )
//    List<LearnerAttendanceProjection> findAllByDistrict_Date_SchoolLevel_SchoolOwnershipWithSchool_SchoolClass(Status status, LocalDate attendanceDate, String districtId, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//
////    @Query("SELECT L FROM  LearnerAttendances  L " +
////            " JOIN FETCH L.schoolClass SC " +
////            " JOIN FETCH SC.school S " +
////            " WHERE L.status <>:status AND  L.attendanceDate =:localDate AND S.schoolLevel =:schoolLevel AND S.schoolOwnership =:schoolOwnership")
//    @Query(nativeQuery = true ,
//    value = "SELECT L.\"boysAbsent\" , L.\"boysPresent\" , L.\"girlsAbsent\" , L.\"girlsPresent\" FROM \"LearnerAttendances\" AS L\n" +
//            "INNER JOIN \"SchoolClasses\" AS SC ON SC.id = L.\"schoolClass_id\"\n" +
//            "INNER JOIN \"Schools\" AS S ON S.id = SC.school_id\n" +
//            "WHERE L.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND SC.status <>:#{#status.ordinal()} " +
//            "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND L.\"attendanceDate\" =:attendanceDate  "
//    )
//    List<LearnerAttendanceProjection> findAllSchoolLevel_Date_SchoolOwnershipWithSchool_SchoolClass(Status status, SchoolLevel schoolLevel, LocalDate attendanceDate, SchoolOwnership schoolOwnership);
//
//
//
//    @Query(nativeQuery = true ,
//            value = "SELECT  L.\"attendanceDate\" , L.\"boysAbsent\" , L.\"boysPresent\" , L.\"girlsAbsent\" , L.\"girlsPresent\" , S.district_id AS districtId FROM \"LearnerAttendances\" AS L\n" +
//                    "INNER JOIN \"SchoolClasses\" AS SC ON SC.id = L.\"schoolClass_id\"\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = SC.school_id\n" +
//                    "WHERE L.status <>:#{#status.ordinal()}  AND S.status <>:#{#status.ordinal()} AND SC.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} ")
//    List<LearnerAttendanceProjection> findAllBySchoolLevel_SchoolOwnershipWithSchool_SchoolClass(Status status, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//    @Query(nativeQuery = true ,
//            value = "SELECT  L.\"attendanceDate\" , L.\"boysAbsent\" , L.\"boysPresent\" , L.\"girlsAbsent\" , L.\"girlsPresent\" , S.district_id AS districtId FROM \"LearnerAttendances\" AS L\n" +
//                    "INNER JOIN \"SchoolClasses\" AS SC ON SC.id = L.\"schoolClass_id\"\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = SC.school_id\n" +
//                    "WHERE L.status <>:#{#status.ordinal()}  AND S.status <>:#{#status.ordinal()} AND SC.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} " +
//                    "AND L.\"attendanceDate\" BETWEEN :startDate AND :endDate AND S.district_id =:districtId ")
//    List<LearnerAttendanceProjection> findAllByDistrict_DateFrom_To_SchoolLevel_SchoolOwnershipWithSchool_SchoolClass(Status status, String districtId, LocalDate startDate, LocalDate endDate, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true ,
//            value = "SELECT L.\"boysAbsent\" , L.\"boysPresent\" , L.\"girlsAbsent\" , L.\"girlsPresent\" , s.id AS schoolId , S.\"name\" as schoolName , " +
//                    "SC.id as schoolClassId , SC.\"name\" as SchoolClassName FROM \"LearnerAttendances\" AS L\n" +
//                    "INNER JOIN \"SchoolClasses\" AS SC ON SC.id = L.\"schoolClass_id\"\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = SC.school_id\n" +
//                    "WHERE L.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND SC.status <>:#{#status.ordinal()} " +
//                    "AND S.id =:schoolId AND L.\"attendanceDate\" =:attendanceDate "
//    )
//    List<LearnerAttendanceProjection> findAllBySchool_DateWithSchool_SchoolClass(Status status, LocalDate attendanceDate, String schoolId);
//
//
//
//    @Query(nativeQuery = true ,
//            value = "SELECT L.\"attendanceDate\", L.\"boysAbsent\" , L.\"boysPresent\" , L.\"girlsAbsent\" , L.\"girlsPresent\" , s.id AS schoolId , S.\"name\" as schoolName , " +
//                    "SC.id as schoolClassId , SC.\"name\" as SchoolClassName FROM \"LearnerAttendances\" AS L\n" +
//                    "INNER JOIN \"SchoolClasses\" AS SC ON SC.id = L.\"schoolClass_id\"\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = SC.school_id\n" +
//                    "WHERE L.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND SC.status <>:#{#status.ordinal()} " +
//                    "AND S.id =:school AND L.\"academicTerm_id\" =:academicTerm "
//    )
//    List<LearnerAttendanceProjection> findAllByAcademicTermAndSchoolWithSchool_SchoolClass(Status status, String academicTerm, String school);
}

