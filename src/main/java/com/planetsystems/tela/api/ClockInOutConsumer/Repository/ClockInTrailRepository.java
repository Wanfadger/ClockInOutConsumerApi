package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.ClockInTrail;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.time.*;
import java.util.*;

@Repository
public interface ClockInTrailRepository extends JpaRepository<ClockInTrail, String> {

    boolean existsByStaffCodeAndClockInDate(String staffCode , LocalDate clockInDate);
}
