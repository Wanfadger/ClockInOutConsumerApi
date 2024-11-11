package com.planetsystems.tela.api.ClockInOutConsumer.Repository;



import com.planetsystems.tela.api.ClockInOutConsumer.model.LearnerAttendance;
import com.planetsystems.tela.api.ClockInOutConsumer.model.SNLearnerAttendance;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface SNLearnerAttendanceRepository extends JpaRepository<SNLearnerAttendance, String> {

    @Query(value = """
            SELECT LA FROM SNLearnerAttendance  AS LA
            JOIN FETCH LA.schoolClass AS SC
            WHERE LA.status <> 8 AND SC.status <> 8 
            AND LA.academicTerm.id =:termId AND SC.school.id =:schoolId
            """)
    List<SNLearnerAttendance> allByTerm_School(String termId, String schoolId);


    @Query(value = """
            SELECT LA FROM SNLearnerAttendance  AS LA
            JOIN FETCH LA.schoolClass AS SC
            WHERE LA.status <> 8 AND SC.status <> 8 
            AND LA.attendanceDate =:attendanceDate AND SC.school.id =:schoolId
            """)
    List<SNLearnerAttendance> allByDate_School(LocalDate attendanceDate, String schoolId);

//    @EntityGraph(value = "snlearnerattendance-schoolclass-academicterm-staff-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SNLearnerAttendance> findAllByStatusNot(Status status);
//
//    Optional<SNLearnerAttendance> findByStatusNotAndId(Status status , String id);
//
//    Optional<SNLearnerAttendance> findByStatusNotAndSchoolClass_IdAndAttendanceDate(Status status , String classId , LocalDate date);
//    boolean existsByStatusNotAndSchoolClass_IdAndAttendanceDate(Status status , String classId , LocalDate date);
//
//    @EntityGraph(value = "snlearnerattendance-schoolclass-academicterm-staff-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SNLearnerAttendance> findAllByStatusNotAndAcademicTerm_IdAndSchoolClass_School_IdAndAttendanceDate(Status status , String termId , String schoolId , LocalDate attendanceDate);
//
//    @EntityGraph(value = "snlearnerattendance-schoolclass-academicterm-staff-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SNLearnerAttendance> findAllByStatusNotAndAcademicTerm_IdAndSchoolClass_School_Id(Status status , String termId , String schoolId);
//
//    @EntityGraph(value = "snlearnerattendance-schoolclass-academicterm-staff-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SNLearnerAttendance> findAllByStatusNotAndSchoolClass_School_Id(Status status ,  String schoolId);
//
//    @EntityGraph(value = "snlearnerattendance-schoolclass-academicterm-staff-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SNLearnerAttendance> findAllByStatusNotAndAcademicTerm_Id(Status status , String termId);
//
//    @EntityGraph(value = "snlearnerattendance-schoolclass-academicterm-staff-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SNLearnerAttendance> findAllByStatusNotAndAcademicTerm_IdAndSchoolClass_School_District_Region_Id(Status status , String termId , String regionId);
//
//    @EntityGraph(value = "snlearnerattendance-schoolclass-academicterm-staff-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SNLearnerAttendance> findAllByStatusNotAndAttendanceDateAndSchoolClass_School_District_Id(Status status , LocalDate attendanceDate  , String districtId);
//
//    @EntityGraph(value = "snlearnerattendance-schoolclass-academicterm-staff-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SNLearnerAttendance> findAllByStatusNotAndAcademicTerm_AcademicYear_IdAndSchoolClass_School_Id(Status status , String yearId , String schoolId);
//
//    @EntityGraph(value = "snlearnerattendance-schoolclass-academicterm-staff-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SNLearnerAttendance> findAllByStatusNotAndSchoolClass_School_IdAndAttendanceDate(Status deleted, String schoolId, LocalDate attendanceDate);
//
//    @EntityGraph(value = "snlearnerattendance-schoolclass-academicterm-staff-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SNLearnerAttendance> findAllByStatusNotAndSchoolClass_School_IdAndAttendanceDateBetween(Status deleted, String schoolId, LocalDate startDate ,LocalDate endDate);
//
//    @EntityGraph(value = "snlearnerattendance-schoolclass-academicterm-staff-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SNLearnerAttendance> findAllByStatusNotAndAttendanceDate(Status deleted, LocalDate attendanceDate);

}
