package com.planetsystems.tela.api.ClockInOutConsumer.Repository;


import com.planetsystems.tela.api.ClockInOutConsumer.model.District;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.time.*;
import java.util.*;


@Repository
public interface DistrictRepository extends JpaRepository<District, String> {

    @EntityGraph(attributePaths = {"region"})
    List<District> findAllByStatusNot(Status status);

//    List<DistrictProjection> findAllByStatusNot(Status status);
//	List<DistrictProjection> findAllByStatusNotAndRegion_IdOrderByNameAsc(Status status , String id);
//	List<DistrictProjection> findAllByStatusNotAndRolledOutOrderByNameAsc(Status status , boolean rollOut);
//	Optional<DistrictProjection> findByStatusNotAndId(Status status , String id);
//
//	boolean existsByStatusNotAndNameIgnoreCase(Status status , String name);
//
//    Optional<DistrictProjection> findByStatusNotAndNameIgnoreCase(Status status , String name);
//
//    boolean existsByStatusNotAndNameIgnoreCaseAndRegion_Id(Status status ,String name, String regionId);
//
//    Optional<DistrictProjection> findByStatusNotAndCodeIgnoreCase(Status deleted, String districtCode);
//
//
//    @EntityGraph(value = "districts-region-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<DistrictWithRegionProjection> getAllByStatusNotOrderByCodeAsc(Status status);
//
//    @EntityGraph(value = "districts-region-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    DistrictWithRegionProjection getByStatusNotAndNameIgnoreCase(Status status , String name);
//
//    @EntityGraph(value = "districts-region-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<District> getAllByStatusNotAndRegion_IdOrderByNameAsc(Status status , String id);
//
//    @EntityGraph(value = "districts-region-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    DistrictWithRegionProjection getByStatusNotAndRegion_Id(Status status , String id);
//
//    @EntityGraph(value = "districts-region-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<District> getAllByStatusNotOrderByNameAsc(Status status);
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT D.id AS districtId , D.name AS districtName , R.id AS regionId , R.name AS regionName FROM \"Districts\" AS D \n " +
//            "INNER JOIN \"Regions\" AS R ON R.id = D.region_id\n" +
//            "WHERE D.status <>:#{#status.ordinal()} AND R.status <>:#{#status.ordinal()}  AND D.id =:districtId ")
//    DistrictProjection2 findByIdWithRegion(Status status , String districtId);
}
