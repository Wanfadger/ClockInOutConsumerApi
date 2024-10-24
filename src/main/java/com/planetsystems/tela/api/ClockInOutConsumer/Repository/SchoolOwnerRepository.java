package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.SchoolOwner;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.time.*;
import java.util.*;


@Repository
public interface SchoolOwnerRepository extends JpaRepository<SchoolOwner, String> {

    @Query("""
            SELECT SO FROM SchoolOwner SO 
            JOIN FETCH SO.school AS SCH
            JOIN FETCH SO.userGroup G
            """)
    List<SchoolOwner> findAllWithGroup_School();

    Optional<SchoolOwner> findByStatusNotAndEmail(Status status , String email);

    @Query("""
            SELECT SO FROM SchoolOwner SO 
            JOIN FETCH SO.school AS SCH
            JOIN FETCH SO.userGroup AS  G
            JOIN FETCH SCH.district AS D
            WHERE SO.email =:email
            """)
    Optional<SchoolOwner> findWithGroup_School_DistrictByUsername(String email);


}
