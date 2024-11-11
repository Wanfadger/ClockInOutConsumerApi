package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.GeneralUserDetail;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.time.*;
import java.util.*;


@Repository
public interface GeneralUserDetailsRepository extends JpaRepository<GeneralUserDetail, String> {

    List<GeneralUserDetail> findAllByStatusNot(Status status);

    Optional<GeneralUserDetail> findByStatusNotAndEmailIgnoreCase(Status status, String email);

    @Query("""
            SELECT G FROM GeneralUserDetails  G
            JOIN FETCH G.schoolStaff ST
            JOIN FETCH ST.school S
            JOIN FETCH S.district D
            WHERE G.status <> :status AND ST.status <> :status AND S.status <> :status AND D.status <> :status
            AND G.email = :email
            """)
    Optional<GeneralUserDetail> findByStatusNotAndEmailIgnoreCaseWithStaff_School(Status status, String email);

    Optional<GeneralUserDetail> findByStatusNotAndId(Status status, String id);

}
