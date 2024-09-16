package com.planetsystems.tela.api.ClockInOutConsumer.Repository;


import com.planetsystems.tela.api.ClockInOutConsumer.model.LoginAudit;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.time.*;
import java.util.*;


@Repository 
public interface LoginAuditRepository extends JpaRepository<LoginAudit , String> {
    List<LoginAudit> findAllByLoginTimeBetween(Date from , Date to);

    List<LoginAudit> findAllByStatusNot(Status status);
    List<LoginAudit> findAllByStatus(Status status);
    Optional<LoginAudit> findByStatusNotAndId(Status status , String id);
}
