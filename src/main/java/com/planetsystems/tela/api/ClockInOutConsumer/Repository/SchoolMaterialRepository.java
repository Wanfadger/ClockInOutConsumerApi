package com.planetsystems.tela.api.ClockInOutConsumer.Repository;


import com.planetsystems.tela.api.ClockInOutConsumer.model.SchoolMaterial;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.time.*;
import java.util.*;


@Repository
public interface SchoolMaterialRepository extends JpaRepository<SchoolMaterial,String> {

    boolean existsByStatusNotAndMaterialNameIgnoreCase(Status status  ,String name);
    List<SchoolMaterial> findAllByStatusNot(Status status);
    List<SchoolMaterial> findAllByStatus(Status status);
    Optional<SchoolMaterial> findByStatusNotAndId(Status status , String id);



}
