package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.Region;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegionRepository extends JpaRepository<Region, String> {
	
    boolean existsByStatusNotAndNameIgnoreCase(Status status , String name);
    
//    @EntityGraph(value = "region-district-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<RegionProjection> findByStatusNotAndNameIgnoreCase(Status status , String name);
//    List<RegionProjection> findAllByStatusNot(Status status);
//    Optional<RegionProjection> findByStatusNotAndId(Status status , String id);
//    Optional<RegionProjection> findByStatusNotAndCodeIgnoreCase(Status deleted, String code);
//
//
//    @EntityGraph(value = "region-district-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<RegionWithDistrictProjection> getAllByStatusNot(Status status);
}
