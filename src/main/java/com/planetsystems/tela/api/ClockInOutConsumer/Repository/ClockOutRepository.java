package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.ClockOut;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;
import java.time.*;
import java.util.*;


@Repository
public interface ClockOutRepository extends JpaRepository<ClockOut, String> {


    @EntityGraph(value = "clockOut-clockIn-graph", type = EntityGraph.EntityGraphType.FETCH)
    List<ClockOut> findAllByStatusNotAndClockIn_AcademicTerm_IdAndClockIn_School_IdAndClockOutDateOrderByClockOutDateAsc(Status status, String academicTermId, String schoolId, LocalDate clockOutDate);

    @EntityGraph(value = "clockOut-clockIn-graph", type = EntityGraph.EntityGraphType.FETCH)
    Optional<ClockOut> findByStatusNotAndClockIn_SchoolStaff_IdAndClockOutDate(Status status, String StaffId, LocalDate clockOutDate);

    @EntityGraph(value = "clockOut-clockIn-graph", type = EntityGraph.EntityGraphType.FETCH)
    List<ClockOut> findAllByStatusNotAndClockIn_SchoolStaff_IdAndClockOutDateBetween(Status status, String StaffId, LocalDate from, LocalDate to);

    @EntityGraph(value = "clockOut-clockIn-graph", type = EntityGraph.EntityGraphType.FETCH)
    List<ClockOut> findAllByStatusNotAndClockOutDate(Status status, LocalDate date);

    Optional<ClockOut> findByStatusNotAndId(Status status, String clockOutId);

    boolean existsByStatusNotAndClockOutDateAndClockIn_Id(Status status, LocalDate clockOutDate, String clockInId);


    boolean existsByStatusNotAndClockIn_Id(Status status, String clockInId);

    boolean existsByStatusNotAndClockIn_IdAndClockOutDate(Status status, String clockInId, LocalDate localDate);


    @Query(nativeQuery = true,
            value =
                    "select C.id as id " +
                            "from \"ClockOuts\" C \n" +
                            "where \n" +
                            "C.\"clockIn_id\"=:clockInId \n" +
                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
                            "AND C.\"clockOutDate\" =:date \n")
    List<String> getStaffClockOutByDate(@Param("status") Status status, @Param("clockInId") String clockInId, @Param("date") LocalDate date);


}
