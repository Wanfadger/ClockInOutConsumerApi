package com.planetsystems.tela.api.ClockInOutConsumer.Repository;


import com.planetsystems.tela.api.ClockInOutConsumer.model.SubjectCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectCategoryRepository extends JpaRepository<SubjectCategory, String> {
//    boolean existsByStatusNotAndNameIgnoreCase(Status status , String name);
//    List<SubjectCategory> findAllByStatusNot(Status status);
//    List<SubjectCategory> findAllByStatus(Status status);
//    Optional<SubjectCategory> findByStatusNotAndId(Status status , String id);
//    Optional<SubjectCategory> findByStatusNotAndCodeIgnoreCase(Status status , String code);
}
