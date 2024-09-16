package com.planetsystems.tela.api.ClockInOutConsumer.Repository;


import com.planetsystems.tela.api.ClockInOutConsumer.model.Subject;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.SubjectClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {

    List<Subject> findAllBySubjectClassificationNotNullAndStatusNotAndSubjectClassification(Status status , SubjectClassification subjectClassification);

//
//    List<Subject> findAllByStatusNot(Status status);
//    List<Subject> findAllByStatus(Status status);
//    Optional<Subject> findByStatusNotAndId(Status status , String id);
//    Optional<Subject> findByStatusNotAndCode(Status status , String code);
//
//    // Checks if a subject exits in category
//
//    List<Subject> findAllByStatusAndSubjectCategory_Id(Status status , String categoryId);
//    List<Subject> findAllByStatusNotAndSubjectClassification(Status status , SubjectClassification classification);
//
//    boolean existsByStatusNotAndNameIgnoreCaseAndSubjectCategory_Id(Status deleted, String name ,String categoryId);
//    boolean existsByStatusNotAndNameIgnoreCaseAndSubjectClassificationAndSubjectCategory_Id(Status deleted, String name , SubjectClassification classification ,  String categoryId);
//
//    List<Subject> findAllByStatusNotAndSubjectClassificationNotNull(Status status);

}
