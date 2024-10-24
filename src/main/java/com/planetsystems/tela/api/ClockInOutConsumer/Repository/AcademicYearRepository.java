package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.AcademicYear;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, String> {
    // academic year only
    boolean existsByStatusNotAndNameIgnoreCase(Status status , String name);
//    List<AcademicYearProjection> findAllByStatusNot(Status status);
//    Optional<AcademicYearProjection> findByStatusNotAndId(Status status , String id);
//    Optional<AcademicYearProjection> findByStatusNotAndCodeIgnoreCase(Status deleted, String code);
//    Optional<AcademicYearProjection> findByStatusNotAndCode(Status status , String code);

//    @EntityGraph(value = "academicYear-Term-Graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<AcademicYear> findByStatusNotAndNameIgnoreCase(Status status , String name);
//
////    @EntityGraph(value = "academicYear-Term-Graph" , type = EntityGraph.EntityGraphType.FETCH)
//
//
//    @EntityGraph(value = "academicYear-Term-Graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<AcademicYear> findByStatusNotAndCode(Status status , String code);
//
//    @EntityGraph(value = "academicYear-Term-Graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<AcademicYear> findAllByStatus(Status status);
//
////    @EntityGraph(value = "academicYear-Term-Graph" , type = EntityGraph.EntityGraphType.FETCH)
//
//    @EntityGraph(value = "academicYear-Term-Graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<AcademicYear> findByStatusNotAndCodeIgnoreCase(Status deleted, String code);

//Y.status , Y.code , Y.endDate , Y.name, Y.startDate , Y.activationStatus
    // JPQL
//    @Query("SELECT Y FROM AcademicYears Y WHERE Y.status !=:status")
//    List<AcademicYearProjection> findAllAcademicYearsOnly(Status status);
//
//    @Query("SELECT Y FROM AcademicYears Y WHERE Y.status !=:status")
//    List<AcademicYearProjection> findAllAcademicYearsOnlyById(Status status , String id);

}
