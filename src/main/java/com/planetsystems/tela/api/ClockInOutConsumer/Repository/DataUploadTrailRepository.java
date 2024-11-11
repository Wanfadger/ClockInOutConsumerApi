package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.DataUploadTrail;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.time.*;
import java.util.*;

@Repository
public interface DataUploadTrailRepository extends JpaRepository<DataUploadTrail, String> {
    List<DataUploadTrail> findAllByStatusNot(Status status);
    Optional<DataUploadTrail> findByStatusNotAndId(Status status , String id);
}
