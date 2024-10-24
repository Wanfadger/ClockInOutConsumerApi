package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.AcademicTerm;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicTermRepository extends JpaRepository<AcademicTerm, String> {

//    @EntityGraph(attributePaths = "{academicYear}" , type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = """
   SELECT A FROM AcademicTerms AS A 
   JOIN FETCH A.academicYear AS Y
   WHERE A.status <> 8 AND A.activationStatus =:activationStatus
""")
    Optional<AcademicTerm> activeAcademicTerm(Status activationStatus);


//    @EntityGraph(value = "academicTerm-year-Graph", type =EntityGraph.EntityGraphType.FETCH )
//    List<AcademicTermWithYearProjection> findAllByStatusNot(Status status);
//
//    @EntityGraph(value = "academicTerm-year-Graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<AcademicTermWithYearProjection> findAllByStatus(Status status);
//
////    @EntityGraph(value = "partial-academicTerm-Graph" , type = EntityGraph.EntityGraphType.FETCH)
//
//    @EntityGraph(value = "academicTerm-year-Graph", type = EntityGraph.EntityGraphType.FETCH)
//    Optional<AcademicTermWithYearProjection> findByStatusNotAndId(Status status, String id);
//
////    @EntityGraph(value = "partial-academicTerm-Graph" , type = EntityGraph.EntityGraphType.FETCH)
//
//    @EntityGraph(value = "academicTerm-year-Graph", type = EntityGraph.EntityGraphType.FETCH)
//    Optional<AcademicTermWithYearProjection> findByStatusNotAndCodeIgnoreCase(Status status, String code);
//
//    // checks if term exits by year , name and status
//
//    boolean existsByStatusNotAndTermAndAcademicYear_Id(Status termStatus, String term, String yearId);
//
////    @EntityGraph(value = "partial-academicTerm-Graph" , type = EntityGraph.EntityGraphType.FETCH)
//
//    @EntityGraph(value = "academicTerm-year-Graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<AcademicTermWithYearProjection> findAllByStatusNotAndAcademicYear_Id(Status status, String yearId);
//
//    //    @EntityGraph(value = "partial-academicTerm-Graph" , type = EntityGraph.EntityGraphType.FETCH)
//    @EntityGraph(value = "academicTerm-year-Graph", type = EntityGraph.EntityGraphType.FETCH)
//    Optional<AcademicTermWithYearProjection> findByActivationStatusAndStatusNot(Status activattionStatus, Status status);
//
////    Optional<AcademicTermProjection> findByStatusNotAndActivationStatus(Status status, Status activattionStatus);
//
//    @Query(value = "select id from \"AcademicTerms\" s where \"status\" <> 8 and \"activationStatus\"=6", nativeQuery = true)
//    Optional<String> findActiveAcademicTermIdByStatusNotDeleted();
//
//    //    @EntityGraph(value = "partial-academicTerm-Graph" , type = EntityGraph.EntityGraphType.FETCH)
//    @EntityGraph(value = "academicTerm-year-Graph", type = EntityGraph.EntityGraphType.FETCH)
//    Optional<AcademicTermWithYearProjection> findByStatusNotAndCodeIgnoreCaseAndAcademicYear_CodeAndAcademicYear_StatusNot(Status termStatus, String termCode, String yearCode, Status yearStatus);
}
