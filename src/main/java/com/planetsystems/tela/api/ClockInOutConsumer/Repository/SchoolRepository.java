package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.IdProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.model.School;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, String> {

    @Query(value = """
                 SELECT S FROM School AS S
                 JOIN FETCH S.district
                 LEFT JOIN S.schoolGeoCoordinateList AS SG
                 WHERE S.status <> :status AND S.id= :id
            """)
    Optional<School> findByStatusNotAndId(Status status, String id);

    Optional<IdProjection> findByTelaSchoolUIDAndStatusNot(String telaSchoolUID, Status status);


    @Query("""
             SELECT S.id FROM School  AS S WHERE 
             S.status <> :status AND S.telaSchoolUID = :telaSchoolUID OR S.deviceNumber=:deviceNumber
             """)
    Optional<IdProjection> idByStatusAndTelaSchoolUID(Status status , String telaSchoolUID);

//    @EntityGraph(attributePaths = "{district}", type = EntityGraph.EntityGraphType.FETCH)
//    Optional<School> findByStatusNotAndTelaSchoolUID(Status status, String telaSchoolUID);
//
//
//

//
//    List<SchoolProjection> findAllByStatusNot(Status status);
//
//    List<SchoolProjection> findAllByStatusNotAndSchoolLevel(Status status, SchoolLevel schoolLevel);
//
////    @EntityGraph(attributePaths = "{district_id}", type = EntityGraph.EntityGraphType.FETCH)
//
//
//    Optional<SchoolProjection> findByStatusNotAndNameIgnoreCase(Status status, String name);
//
//    boolean existsByStatusNotAndNameIgnoreCaseAndSchoolCategory_IdAndDistrict_Id(Status schoolStatus, String schoolName, String categoryId, String districtId);
//
//    boolean existsByStatusNotAndNameIgnoreCaseAndDistrict_Id(Status schoolStatus, String schoolName, String districtId);
//
//    List<SchoolProjection> findAllByStatusNotAndSchoolCategory_StatusAndDistrict_StatusNotAndDistrict_RolledOutOrderByNameAsc(Status schoolStatusNot, Status categoryStatusNot, Status districtStatusNot, boolean rolleOut);
//
//    //    @EntityGraph(value = "school-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolProjection> findAllByStatusNotAndSchoolCategory_IdAndDistrict_IdAndDistrict_RolledOutAndSchoolOwnershipOrderByNameAsc(Status schoolStatusNot, String categoryId, String districtId, boolean rolleOut, SchoolOwnership schoolOwnership);
//
//    //    @EntityGraph(value = "school-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolProjection> findAllByStatusNotAndDistrict_IdAndDistrict_RolledOutAndSchoolLevelNotNullOrderByNameAsc(Status schoolStatusNot, String districtId, boolean rolleOut);
//
//    List<SchoolProjection> findAllByStatusNotAndDistrict_IdAndDistrict_RolledOutAndSchoolLevelNotNullAndSchoolOwnershipOrderByNameAsc
//            (Status schoolStatusNot, String districtId, boolean rolleOut, SchoolOwnership schoolOwnership);
//
//    List<SchoolWithDistrictProjection> findAllByStatusNotAndDistrict_RolledOutAndDistrict_IdOrderByNameAsc(Status status, boolean rolleOut, String districtId);
//
//    Optional<SchoolWithDistrictProjection> findByStatusNotAndDeviceNumberAndDistrict_RolledOut(Status status, String deviceNo, boolean rolleOut);
//
//    //    @EntityGraph(value = "school-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<SchoolProjection> findByStatusNotAndDeviceNumber(Status schoolStatusNot, String deviceNo);
//
//    //    @EntityGraph(value = "school-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<SchoolProjection> findByStatusNotAndTelaSchoolUIDIgnoreCase(Status schoolStatusNot, String telaSchoolUID);
//
//    boolean existsByStatusNotAndTelaSchoolUIDIgnoreCase(Status status, String telaSchoolUID);
//
//    //    @EntityGraph(value = "school-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<SchoolProjection> findByStatusNotAndTelaSchoolNumberIgnoreCase(Status schoolStatusNot, String telaNumber);
//
//    //    @EntityGraph(value = "school-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<SchoolProjection> findByStatusNotAndNameIgnoreCaseAndDistrict_Id(Status status, String schoolName, String district_id);
//
//    //    @EntityGraph(value = "school-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolProjection> findAllByStatusNotAndDistrict_Region_IdOrderByNameAsc(Status schoolStatusNot, String regionId);
//
//    List<SchoolProjection> findByStatusNotOrderByNameAsc(Status schoolStatusNot);
//
//    //    @EntityGraph(value = "school-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<SchoolProjection> findByStatusNotAndCodeIgnoreCase(Status deleted, String code);
//
//    //    @EntityGraph(value = "school-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<SchoolProjection> findByStatusNotAndEmisNumber(Status deleted, String emis);
//
//    //    @EntityGraph(value = "school-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolProjection> findByStatusNotAndSchoolCategory_IdOrderByNameAsc(Status status, String categoryId);
//
//
//    //    @EntityGraph(value = "partial-school-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolProjection> findAllByStatusNotAndSchoolOwnershipAndSchoolLevelNotNull(Status status, SchoolOwnership schoolOwnership);
//
//    List<SchoolProjection> findAllByStatusNotAndSchoolOwnershipNull(Status status);
//
//    // school with District ,category
////    @EntityGraph(value = "school-district-graph" , type = EntityGraph.EntityGraphType.FETCH)
//
//
//    //    @EntityGraph(value = "school-district-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    @Query("SELECT S  FROM School S " +
//            "INNER JOIN  S.district d " +
//            "WHERE S.status !=:status AND S.id=:id")
//    @Transactional(readOnly = true)
//    Optional<SchoolWithDistrictProjection> schoolByIdWithDistrict(Status status, String id);
//
//
//    // school with District, region, category
//    @Query("SELECT S  FROM School S " +
//            "INNER JOIN  S.district d " +
//            "INNER JOIN d.region r " +
//            "WHERE S.status !=:status AND S.id=:id")
//    @Transactional(readOnly = true)
//    Optional<SchoolWithDistrictRegionProjection> schoolByIdWithDistrictRegion(Status status, String id);
//
//    @Query("SELECT  S  FROM School S " +
//            "INNER JOIN  S.schoolCategory sc " +
//            "INNER JOIN  S.district d " +
//            "INNER JOIN  d.region r " +
//            "WHERE S.status !=:status")
//    List<SchoolWithDistrictRegionProjection> schoolsWithDistrictRegion(Status status);
//
//    @Query("SELECT  S  FROM School S " +
//            "INNER JOIN S.schoolCategory sc " +
//            "INNER JOIN FETCH S.district d " +
//            "INNER JOIN d.region r " +
//            "WHERE S.status !=:status and S.schoolOwnership=:schoolOwnership")
//    List<SchoolWithDistrictRegionProjection> schoolsWithDistrictRegion(Status status, SchoolOwnership schoolOwnership);
//
//
//    @EntityGraph(value = "school-district-region-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<School> findAllByStatusNotAndDistrict_IdAndDistrict_RolledOutAndSchoolLevelAndSchoolOwnershipOrderByNameAsc(Status schoolStatusNot, String districtId, boolean rolleOut, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    List<SchoolProjection> findAllByStatusNotAndDistrict_IdAndDistrict_RolledOutAndSchoolOwnershipAndSchoolLevelOrderByNameAsc(Status schoolStatusNot, String districtId, boolean rolleOut, SchoolOwnership schoolOwnership, SchoolLevel schoolLevel);
//
//
//    @EntityGraph(value = "school-district-region-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<School> findAllByStatusNotAndDistrict_RolledOutAndSchoolOwnershipAndSchoolLevelNotNull(Status schoolStatusNot, boolean rolledOut, SchoolOwnership schoolOwnership);
//
//
//    @EntityGraph(value = "school-district-region-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<School> findAllByStatusNotAndDistrict_Region_IdAndDistrict_RolledOutAndSchoolOwnershipAndSchoolLevelNotNull(Status schoolStatusNot, String regionId, boolean rolledOut, SchoolOwnership schoolOwnership);
//
//
//    //Optional<SchoolWithDistrictProjection> findByStatusNotAndDeviceNumberAndDistrict_RolledOut(Status status,String deviceNo ,   boolean rolleOut );
//
//
//    //    @Query("SELECT S FROM School  S " +
////            "where S.status <>:status  AND S.district.id =:districtId AND S.schoolOwnership =:schoolOwnership ")
//    @Query(nativeQuery = true,
//            value = "SELECT S.id AS schoolId ,S.code AS schoolCode, S.name as schoolName , S.\"schoolLevel\"  , S.\"schoolOwnership\" , S.\"deviceNumber\" as schoolDeviceNumber ," +
//                    " S.longitude as schoolLongitude, S.latitude as schoolLatitude , S.district_id AS districtId FROM \"Schools\" AS S\n" +
//                    "WHERE S.status <>:#{#status.ordinal()} \n" +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}  AND S.district_id =:districtId ")
//    List<SchoolProjection2> findAllByDistrict_SchoolOwnership(Status status, String districtId, SchoolOwnership schoolOwnership);
//
//    @Query(nativeQuery = true,
//            value = "SELECT count(*) FROM \"Schools\" AS S\n" +
//                    "WHERE S.status <>:#{#status.ordinal()} \n" +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.district_id =:districtId")
//    long countAllByDistrict_SchoolLevel_SchoolOwnership(Status status, String districtId, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT S.id AS schoolId ,S.code AS schoolCode, S.name as schoolName , S.\"schoolLevel\"  , S.\"schoolOwnership\" , S.\"deviceNumber\" as schoolDeviceNumber ," +
//                    " S.longitude as schoolLongitude, S.latitude as schoolLatitude , S.district_id AS districtId FROM \"Schools\" AS S\n" +
//                    "WHERE S.status <>:#{#status.ordinal()} \n" +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()}")
//    List<SchoolProjection2> findAllBySchoolLevel_SchoolOwnership(Status status, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT S.id AS schoolId , S.name as schoolName , S.\"schoolLevel\"  , S.\"schoolOwnership\" , S.\"deviceNumber\" as schoolDeviceNumber ," +
//                    " S.longitude as schoolLongitude, S.latitude as schoolLatitude , S.district_id AS districtId FROM \"Schools\" AS S\n" +
//                    "WHERE S.status <>:#{#status.ordinal()} \n" +
//                    "AND S.district_id =:districtId \n" +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()}")
//    List<SchoolProjection2> findAllBySchoolLevel_SchoolOwnership_district(Status status, String districtId, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT S.id AS schoolId, S.code AS schoolCode, S.name as schoolName , S.\"schoolLevel\" ," +
//                    "S.\"schoolOwnership\" , S.\"deviceNumber\" as schoolDeviceNumber\n ," +
//                    "S.longitude as schoolLongitude, S.latitude as schoolLatitude\n , " +
//                    "S.\"telaSchoolUID\" as schoolTelaSchoolUID\n," +
//                    "D.id AS districtId , D.name as districtName\n" +
//                    "FROM \"Schools\" AS S\n" +
//                    "INNER JOIN \"Districts\" AS D ON D.id = S.district_id\n" +
//                    "WHERE S.status <>:#{#status.ordinal()} And D.status <>:#{#status.ordinal()} \n" +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()}")
//    List<SchoolProjection2> findAllBySchoolLevel_SchoolOwnershipWithDistrict(Status status, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//    @Query(nativeQuery = true,
//            value = "SELECT S.id AS schoolId ,S.code AS schoolCode, S.name as schoolName , S.\"schoolLevel\"  , S.\"schoolOwnership\" , S.\"deviceNumber\" as schoolDeviceNumber ," +
//                    " S.longitude as schoolLongitude, S.latitude as schoolLatitude , " +
//                    "D.id AS districtId , D.name as districtName " +
//                    "FROM \"Schools\" AS S\n" +
//                    "INNER JOIN \"Districts\" AS D ON D.id = S.district_id\n" +
//                    "WHERE S.status <>:#{#status.ordinal()} And D.status <>:#{#status.ordinal()} \n" +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} ")
//    List<SchoolProjection2> findAllBySchoolOwnershipWithDistrict(Status status, SchoolOwnership schoolOwnership);
//
//
//    Optional<School> findAllByStatusNotAndDeviceNumber(Status status, String deviceNumber);
//
//    @Query(nativeQuery = true,
//            value = "SELECT D.id AS districtId , S.code AS schoolCode, D.\"name\" AS districtName , R.id AS regionId , R.\"name\" AS regionName\n" +
//                    ", S.\"name\" AS schoolName , S.id as schoolId FROM \"Schools\" AS S\n" +
//                    "INNER JOIN \"Districts\" AS D ON D.id = S.district_id\n" +
//                    "INNER JOIN \"Regions\" AS R ON R.id = D.region_id\n" +
//                    "WHERE S.status <>:#{#status.ordinal()} AND D.status <>:#{#status.ordinal()} AND R.status <>:#{#status.ordinal()} AND S.id =:schoolId"
//    )
//    SchoolProjection2 findByIdWithLocalGovernment_Region(Status status, String schoolId);
//
//
//    @Query(nativeQuery = true, value = """
//            select * from "Schools" where status <> 8 AND "name" =:schoolName and "district_id" =:districtId
//            """)
//    Optional<School> findSchoolByNameDistrict_Id(@Param("schoolName") String schoolName, @Param("districtId") String districtId);
//

}
