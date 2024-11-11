package com.planetsystems.tela.api.ClockInOutConsumer.Repository;


import com.planetsystems.tela.api.ClockInOutConsumer.model.SchoolStaff;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SchoolStaffRepository extends JpaRepository<SchoolStaff, String> {
    @Query(value = "SELECT ST FROM SchoolStaff ST " +
            "JOIN FETCH ST.generalUserDetail G " +
            "JOIN FETCH ST.school S " +
            "WHERE ST.status <>:status AND G.status <>:status AND S.id =:schoolId")
    List<SchoolStaff> findAllBySchoolWithSchool_StaffDetail(Status status , String schoolId);


//
//    // find records with
//
//    List<SchoolStaff> findAllByIdIn(List<String> staffIds);
//
//    boolean existsByStatusNotAndSchool_IdAndGeneralUserDetail_EmailOrGeneralUserDetail_NationalIdOrGeneralUserDetail_PhoneNumber
//            (Status status , String schoolId , String email , String nin , String phoneNumber);
//
//    boolean existsByStatusNotAndSchool_IdAndGeneralUserDetail_Email(Status status , String schoolId , String email);
//
//    boolean existsByStatusNotAndSchool_IdAndGeneralUserDetail_NationalId(Status status , String schoolId , String nin);
//
//    boolean existsByStatusNotAndSchool_IdAndGeneralUserDetail_PhoneNumber(Status status , String schoolId , String phoneNumber);
//
//    boolean existsByStatusNotAndStaffCodeIgnoreCase(Status status , String staffId);
//
//    @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolStaff> findAllByStatusNot(Status status);
//
//    @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<SchoolStaff> findByStatusNotAndStaffCodeIgnoreCase(Status status , String code);
//
//    @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolStaff> findAllByStatusNotAndStaffCodeIgnoreCase(Status status , String code);
//
//    @Query(value="select id from \"SchoolStaffs\" s where \"status\" <> 8 and \"staffCode\"=:code",nativeQuery=true)
//    Optional<String> findSchoolStaffIdByStatusNotDeletedAndStaffCodeIgnoreCase(@Param("code") String code);
//
//    Optional<SchoolStaff> findByStatusNotAndId(Status status , String id);
//    @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolStaff> findAllByStatusNotAndStaffInServiceStatusAndSchool_Id(Status status, StaffInServiceStatus staffInServiceStatus , String schoolId);
//
//    @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolStaff> findAllByStatusNotAndSchool_District_Id(Status status , String districtId);
//
//    @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolStaff> findAllByStatusNotAndSchool_District_IdAndSchool_District_RolledOut(Status status , String districtId , boolean roledOut);
//
//    @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolStaff> findAllByStatusNotAndSchool_District_Region_IdAndSchool_District_RolledOutAndSchool_SchoolOwnership(Status status , String regionId , boolean roledOut,SchoolOwnership schoolOwnership);
//    @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<SchoolStaff> findByStatusNotAndStaffTypeAndSchool_Id(Status status , StaffType staffType , String schoolId);
//    @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolStaff> findAllByStatusNotAndStaffCodeNull (Status status);
//
//   // @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//
//
//    @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<SchoolStaff> findByStatusNotAndSchool_Id_AndGeneralUserDetail_FirstNameIgnoreCaseAndGeneralUserDetail_LastNameIgnoreCaseOrGeneralUserDetail_PhoneNumberIgnoreCase
//            (Status status , String schoolId , String firstName , String lastName , String phoneNumber);
//
//
//    @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolStaff> findAllByStatusNotAndSchool_District_IdAndSchool_District_RolledOutAndSchool_SchoolLevelAndSchool_SchoolOwnership
//            (Status status , String districtId , boolean roledOut, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//
//    @Query(value = "SELECT ST FROM SchoolStaff ST " +
//            "JOIN FETCH ST.generalUserDetail G " +
//            "JOIN FETCH ST.school S " +
//            "WHERE ST.status <>:status")
//    List<SchoolStaff> findAllWithSchool(Status status);
//
//    @Query(value = "SELECT ST FROM SchoolStaff ST " +
//            "JOIN FETCH ST.generalUserDetail G " +
//            "JOIN FETCH ST.school S " +
//            "JOIN FETCH S.district D " +
//            "WHERE ST.status <>:status")
//    List<SchoolStaff> findAllWithSchool_District(Status status);
//
//
//
//
//    @Query(value = "SELECT ST FROM SchoolStaff ST " +
//            "JOIN FETCH ST.generalUserDetail G " +
//            "JOIN FETCH ST.school S " +
//            "WHERE ST.status <>:status AND S.district.id =:districtId")
//    List<SchoolStaff> findAllByDistrictWithSchool(Status status , String districtId);
//
//
//    @Query(value = "SELECT ST FROM SchoolStaff ST " +
//            "JOIN FETCH ST.generalUserDetail G " +
//            "JOIN FETCH ST.school S " +
//            "JOIN FETCH S.district D " +
//            "WHERE ST.status <>:status AND S.id =:schoolId")
//    List<SchoolStaff> findAllBySchoolWithSchool_District(Status status , String schoolId);
//
//
//
//
//
//
//    @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolStaff> findAllByStatusNotAndSchool_District_RolledOutAndSchool_SchoolOwnershipAndSchool_SchoolLevelNotNull(Status status , boolean roledOut, SchoolOwnership schoolOwnership);
//
//
//    @EntityGraph(value = "staff-with-details-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<SchoolStaff> findAllByStatusNotAndSchool_District_IdAndSchool_District_RolledOutAndSchool_SchoolOwnership(Status status , String districtId , boolean roledOut,SchoolOwnership schoolOwnership);
//
//
//
//
//    @Query(nativeQuery=true ,
//            value="SELECT count(*) FROM \"SchoolStaffs\" as ST where ST.status <>:#{#status.ordinal()} AND ST.school_id=:schoolId")
//    long countAllBySchool(Status status ,  String schoolId);
//
//    @Query(nativeQuery = true ,
//            value = "SELECT ST.id as staffId, ST.\"staffType\" as staffType  ,S.id as schoolId , S.\"name\" as schoolName , S.district_id as districtId FROM \"SchoolStaffs\" AS ST\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = ST.school_id\n" +
//                    "WHERE ST.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} \n" +
//                    "AND S.district_id =:districtId AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}")
//    List<SchoolStaffProjection2> findAllByDistrict_SchoolLevel_SchoolOwnershipWithSchool(Status status, String districtId , SchoolLevel schoolLevel , SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true ,
//            value ="SELECT count(*) FROM \"SchoolStaffs\" as ST \n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = ST.school_id\n" +
//                    "WHERE ST.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND ST.\"staffType\" NOTNULL" )
//    long countAllBySchoolLevel_SchoolOwnershipWithSchool(Status status, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//
//    @Query(nativeQuery = true ,
//            value ="SELECT count(*) FROM \"SchoolStaffs\" as ST \n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = ST.school_id\n" +
//                    "WHERE ST.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND ST.\"staffType\" NOTNULL \n" +
//                    "AND ST.\"staffType\" =:#{#staffType.ordinal()} \n" +
//                    "OR ST.\"staffType\" =:#{#staffType2.ordinal()}"
//    )
//    long countAllBySchoolLevel_SchoolOwnership_StaffTypeWithSchool(Status status, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership, StaffType staffType,StaffType staffType2);
//
//
//    @Query(nativeQuery = true ,
//            value ="SELECT count(*) FROM \"SchoolStaffs\" as ST \n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = ST.school_id\n" +
//                    "WHERE ST.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND ST.\"staffType\" NOTNULL " +
//                    "AND S.district_id =:districtId" )
//    long dbCountAllByDistrict_SchoolLevel_SchoolOwnershipWithSchool(Status status, String districtId, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true ,
//            value ="SELECT S.district_id AS districtId FROM \"SchoolStaffs\" as ST \n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = ST.school_id\n" +
//                    "WHERE ST.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND ST.\"staffType\" NOTNULL ")
//    List<SchoolStaffProjection2> findAllBySchoolLevel_SchoolOwnershipWithSchool(Status status, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true ,
//            value ="SELECT ST.school_id AS schoolId FROM \"SchoolStaffs\" as ST \n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = ST.school_id\n" +
//                    "WHERE ST.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND S.district_id =:districtId \n" +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND ST.\"staffType\" NOTNULL ")
//    List<SchoolStaffProjection2> findAllBySchoolLevel_SchoolOwnership_localGovernmentWithSchool(String districtId,Status status, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true ,
//            value ="SELECT ST.registered as registered,ST.\"specialNeeds\" as specialNeeds,G.gender as gender FROM \"SchoolStaffs\" as ST \n" +
//                    "INNER JOIN \"GeneralUserDetails\" AS G ON ST.\"generalUserDetail_id\" = G.id \n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = ST.school_id\n" +
//                    "INNER JOIN \"Districts\" AS D ON D.id = S.district_id\n" +
//                    "WHERE ST.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND ST.\"staffType\" NOTNULL ")
//    List<SchoolStaffProjection2> findAllBySchoolLevel_SchoolOwnershipWithStaff(Status status, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true ,
//            value ="SELECT ST.registered as registered,ST.\"specialNeeds\" as specialNeeds, D.id as districtId , D.name as districtName FROM \"SchoolStaffs\" as ST \n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = ST.school_id\n" +
//                    "INNER JOIN \"Districts\" AS D ON D.id = S.district_id\n" +
//                    "WHERE ST.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} " +
//                    "AND ST.\"staffType\" NOTNULL ")
//    List<SchoolStaffProjection2> findAllBySchoolLevel_SchoolOwnershipWithLocalGovernment_School(Status status, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    // Get staff enrollment summary by sub-region (region,total,male,female,undefined)
//
//
//    @Query(nativeQuery=true ,
//            value="SELECT reg.name,\n" +
//                    "reg.id ,\n" +
//                    "COUNT(ss.id) as total,\n" +
//                    "\n" +
//                    "( SELECT COUNT(mss.id) as male FROM \"SchoolStaffs\" AS mss\n" +
//                    "  INNER JOIN \"GeneralUserDetails\" as userd\n" +
//                    "  ON mss.\"generalUserDetail_id\"=userd.id\n" +
//                    "  INNER JOIN \"Schools\" AS sch ON\n" +
//                    "  mss.school_id = sch.id\n" +
//                    "  INNER JOIN \"Districts\" AS dist ON\n" +
//                    "  sch.district_id=dist.id\n" +
//                    "  INNER JOIN \"Regions\" AS mreg ON\n" +
//                    "   dist.region_id=reg.id\n" +
//                    "   WHERE mss.status <> 8\n" +
//                    "   AND sch.status <> 8\n" +
//                    "AND sch.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND sch.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}\n" +
//                    "   AND userd.gender=0\n" +
//                    "   AND mreg.id=reg.id ),\n" +
//                    "   \n" +
//                    "( SELECT COUNT(mss.id) as female FROM \"SchoolStaffs\" AS mss\n" +
//                    "  INNER JOIN \"GeneralUserDetails\" as userd\n" +
//                    "  ON mss.\"generalUserDetail_id\"=userd.id\n" +
//                    "  INNER JOIN \"Schools\" AS sch ON\n" +
//                    "  mss.school_id = sch.id\n" +
//                    "  INNER JOIN \"Districts\" AS dist ON\n" +
//                    "  sch.district_id=dist.id\n" +
//                    "  INNER JOIN \"Regions\" AS mreg ON\n" +
//                    "   dist.region_id=reg.id\n" +
//                    "   WHERE mss.status <> 8\n" +
//                    "   AND sch.status <> 8\n" +
//                    "AND sch.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND sch.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}\n" +
//                    "   AND userd.gender=1\n" +
//                    "   AND mreg.id=reg.id ),\n" +
//                    "   \n" +
//                    " ( SELECT COUNT(mss.id) as undefined FROM \"SchoolStaffs\" AS mss\n" +
//                    "  INNER JOIN \"GeneralUserDetails\" as userd\n" +
//                    "  ON mss.\"generalUserDetail_id\"=userd.id\n" +
//                    "  INNER JOIN \"Schools\" AS sch ON\n" +
//                    "  mss.school_id = sch.id\n" +
//                    "  INNER JOIN \"Districts\" AS dist ON\n" +
//                    "  sch.district_id=dist.id\n" +
//                    "  INNER JOIN \"Regions\" AS mreg ON\n" +
//                    "   dist.region_id=reg.id\n" +
//                    "   WHERE mss.status <> 8\n" +
//                    "   AND sch.status <> 8\n" +
//                    "AND sch.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND sch.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}\n" +
//                    "   AND userd.gender ISNULL\n" +
//                    "   AND mreg.id=reg.id )\n" +
//                    "   \n" +
//                    "FROM \"SchoolStaffs\" AS ss\n" +
//                    "INNER JOIN \"Schools\" AS sch ON\n" +
//                    "ss.school_id = sch.id\n" +
//                    "INNER JOIN \"Districts\" AS dist ON\n" +
//                    " sch.district_id=dist.id\n" +
//                    "INNER JOIN \"Regions\" AS reg ON\n" +
//                    " dist.region_id=reg.id\n" +
//                    "WHERE ss.status <> 8\n" +
//                    "AND sch.status <> 8\n" +
//                    "AND sch.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND sch.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}\n" +
//                    "AND reg.status <> 8\n" +
//                    "GROUP BY reg.name,reg.id\n" +
//                    "ORDER BY reg.name ASC")
//    List<SubregionalStaffEnrollmentProjection> findSubregionStaffSummaryBySchoolLevel_SchoolOwnership(SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
////registered
//    @Query(nativeQuery=true ,
//            value="SELECT reg.name,\n" +
//                    "reg.id ,\n" +
//                    "COUNT(ss.id) as total,\n" +
//                    "\n" +
//                    "( SELECT COUNT(mss.id) as male FROM \"SchoolStaffs\" AS mss\n" +
//                    "  INNER JOIN \"GeneralUserDetails\" as userd\n" +
//                    "  ON mss.\"generalUserDetail_id\"=userd.id\n" +
//                    "  INNER JOIN \"Schools\" AS sch ON\n" +
//                    "  mss.school_id = sch.id\n" +
//                    "  INNER JOIN \"Districts\" AS dist ON\n" +
//                    "  sch.district_id=dist.id\n" +
//                    "  INNER JOIN \"Regions\" AS mreg ON\n" +
//                    "   dist.region_id=reg.id\n" +
//                    "   WHERE mss.status <> 8\n" +
//                    "   AND mss.registered=true\n" +
//                    "   AND sch.status <> 8\n" +
//                    "AND sch.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND sch.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}\n" +
//                    "   AND userd.gender=0\n" +
//                    "   AND mreg.id=reg.id ),\n" +
//                    "   \n" +
//                    "( SELECT COUNT(mss.id) as female FROM \"SchoolStaffs\" AS mss\n" +
//                    "  INNER JOIN \"GeneralUserDetails\" as userd\n" +
//                    "  ON mss.\"generalUserDetail_id\"=userd.id\n" +
//                    "  INNER JOIN \"Schools\" AS sch ON\n" +
//                    "  mss.school_id = sch.id\n" +
//                    "  INNER JOIN \"Districts\" AS dist ON\n" +
//                    "  sch.district_id=dist.id\n" +
//                    "  INNER JOIN \"Regions\" AS mreg ON\n" +
//                    "   dist.region_id=reg.id\n" +
//                    "   WHERE mss.status <> 8\n" +
//                    "   AND mss.registered=true\n" +
//                    "   AND sch.status <> 8\n" +
//                    "AND sch.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND sch.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}\n" +
//                    "   AND userd.gender=1\n" +
//                    "   AND mreg.id=reg.id ),\n" +
//                    "   \n" +
//                    " ( SELECT COUNT(mss.id) as undefined FROM \"SchoolStaffs\" AS mss\n" +
//                    "  INNER JOIN \"GeneralUserDetails\" as userd\n" +
//                    "  ON mss.\"generalUserDetail_id\"=userd.id\n" +
//                    "  INNER JOIN \"Schools\" AS sch ON\n" +
//                    "  mss.school_id = sch.id\n" +
//                    "  INNER JOIN \"Districts\" AS dist ON\n" +
//                    "  sch.district_id=dist.id\n" +
//                    "  INNER JOIN \"Regions\" AS mreg ON\n" +
//                    "   dist.region_id=reg.id\n" +
//                    "   WHERE mss.status <> 8\n" +
//                    "   AND mss.registered=true\n" +
//                    "   AND sch.status <> 8\n" +
//                    "AND sch.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND sch.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}\n" +
//                    "   AND userd.gender ISNULL\n" +
//                    "   AND mreg.id=reg.id )\n" +
//                    "   \n" +
//                    "FROM \"SchoolStaffs\" AS ss\n" +
//                    "INNER JOIN \"Schools\" AS sch ON\n" +
//                    "ss.school_id = sch.id\n" +
//                    "INNER JOIN \"Districts\" AS dist ON\n" +
//                    " sch.district_id=dist.id\n" +
//                    "INNER JOIN \"Regions\" AS reg ON\n" +
//                    " dist.region_id=reg.id\n" +
//                    "WHERE ss.status <> 8\n" +
//                    "AND ss.registered=true\n" +
//                    "AND sch.status <> 8\n" +
//                    "AND sch.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND sch.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}\n" +
//                    "AND reg.status <> 8\n" +
//                    "GROUP BY reg.name,reg.id\n" +
//                    "ORDER BY reg.name ASC")
//    List<SubregionalStaffEnrollmentProjection> findSubregionStaffSummaryBySchoolLevel_SchoolOwnership_registered(SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery=true ,
//            value="SELECT reg.name,\n" +
//                    "reg.id ,\n" +
//                    "COUNT(ss.id) as total,\n" +
//                    "\n" +
//                    "( SELECT COUNT(mss.id) as male FROM \"SchoolStaffs\" AS mss\n" +
//                    "  INNER JOIN \"GeneralUserDetails\" as userd\n" +
//                    "  ON mss.\"generalUserDetail_id\"=userd.id\n" +
//                    "  INNER JOIN \"Schools\" AS sch ON\n" +
//                    "  mss.school_id = sch.id\n" +
//                    "  INNER JOIN \"Districts\" AS dist ON\n" +
//                    "  sch.district_id=dist.id\n" +
//                    "  INNER JOIN \"Regions\" AS mreg ON\n" +
//                    "   dist.region_id=reg.id\n" +
//                    "   WHERE mss.status <> 8\n" +
//                    "   AND mss.registered=false\n" +
//                    "   AND sch.status <> 8\n" +
//                    "AND sch.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND sch.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}\n" +
//                    "   AND userd.gender=0\n" +
//                    "   AND mreg.id=reg.id ),\n" +
//                    "   \n" +
//                    "( SELECT COUNT(mss.id) as female FROM \"SchoolStaffs\" AS mss\n" +
//                    "  INNER JOIN \"GeneralUserDetails\" as userd\n" +
//                    "  ON mss.\"generalUserDetail_id\"=userd.id\n" +
//                    "  INNER JOIN \"Schools\" AS sch ON\n" +
//                    "  mss.school_id = sch.id\n" +
//                    "  INNER JOIN \"Districts\" AS dist ON\n" +
//                    "  sch.district_id=dist.id\n" +
//                    "  INNER JOIN \"Regions\" AS mreg ON\n" +
//                    "   dist.region_id=reg.id\n" +
//                    "   WHERE mss.status <> 8\n" +
//                    "   AND mss.registered=false\n" +
//                    "   AND sch.status <> 8\n" +
//                    "AND sch.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND sch.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}\n" +
//                    "   AND userd.gender=1\n" +
//                    "   AND mreg.id=reg.id ),\n" +
//                    "   \n" +
//                    " ( SELECT COUNT(mss.id) as undefined FROM \"SchoolStaffs\" AS mss\n" +
//                    "  INNER JOIN \"GeneralUserDetails\" as userd\n" +
//                    "  ON mss.\"generalUserDetail_id\"=userd.id\n" +
//                    "  INNER JOIN \"Schools\" AS sch ON\n" +
//                    "  mss.school_id = sch.id\n" +
//                    "  INNER JOIN \"Districts\" AS dist ON\n" +
//                    "  sch.district_id=dist.id\n" +
//                    "  INNER JOIN \"Regions\" AS mreg ON\n" +
//                    "   dist.region_id=reg.id\n" +
//                    "   WHERE mss.status <> 8\n" +
//                    "   AND mss.registered=false\n" +
//                    "   AND sch.status <> 8\n" +
//                    "AND sch.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND sch.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}\n" +
//                    "   AND userd.gender ISNULL\n" +
//                    "   AND mreg.id=reg.id )\n" +
//                    "   \n" +
//                    "FROM \"SchoolStaffs\" AS ss\n" +
//                    "INNER JOIN \"Schools\" AS sch ON\n" +
//                    "ss.school_id = sch.id\n" +
//                    "INNER JOIN \"Districts\" AS dist ON\n" +
//                    " sch.district_id=dist.id\n" +
//                    "INNER JOIN \"Regions\" AS reg ON\n" +
//                    " dist.region_id=reg.id\n" +
//                    "WHERE ss.status <> 8\n" +
//                    "AND ss.registered=false\n" +
//                    "AND sch.status <> 8\n" +
//                    "AND sch.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND sch.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}\n" +
//                    "AND reg.status <> 8\n" +
//                    "GROUP BY reg.name,reg.id\n" +
//                    "ORDER BY reg.name ASC")
//    List<SubregionalStaffEnrollmentProjection> findSubregionStaffSummaryBySchoolLevel_SchoolOwnership_notregistered(SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);


}
