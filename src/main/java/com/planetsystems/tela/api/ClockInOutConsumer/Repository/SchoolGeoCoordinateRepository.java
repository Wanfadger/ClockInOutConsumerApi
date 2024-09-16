package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.SchoolGeoCoordinate;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.time.*;
import java.util.*;


@Repository
public interface SchoolGeoCoordinateRepository extends JpaRepository<SchoolGeoCoordinate, String> {


    Optional<SchoolGeoCoordinate> findByStatusNotAndSchool_Id(Status status,String schoolId);

    List<SchoolGeoCoordinate> findAllByStatusNotAndSchool_District_IdAndAndSchool_SchoolOwnership(Status status, String districtId, SchoolOwnership schoolOwnership);
}
