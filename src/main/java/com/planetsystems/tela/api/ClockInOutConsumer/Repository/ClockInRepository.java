package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.ClockIn;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClockInRepository extends JpaRepository<ClockIn, String> {

    @Query(value = """
            SELECT CL FROM ClockIns AS CL
            JOIN FETCH CL.schoolStaff AS ST
            JOIN FETCH ST.generalUserDetail AS GD
            WHERE CL.status <> 8 AND ST.status <> 8 AND GD.status <> 8
            AND CL.clockInDate =:clockInDate AND CL.school.id =:schoolId
            """)
    List<ClockIn> allClockByDate_SchoolWithStaff(LocalDate clockInDate, String schoolId);

    @Query(value = """
            SELECT CL FROM ClockIns AS CL
            JOIN FETCH CL.schoolStaff AS ST
            JOIN FETCH ST.generalUserDetail AS GD
            WHERE CL.status <> 8 AND ST.status <> 8 AND GD.status <> 8
            AND CL.academicTerm.id =:termId AND CL.school.id =:schoolId
            """)
    List<ClockIn> allClockByTerm_SchoolWithStaff(String termId, String schoolId);

    boolean existsByStatusNotAndClockInDateAndSchoolStaff_Id(Status status , LocalDate clockInDate , String staffId);

//
//    @EntityGraph(attributePaths = "{schoolStaff.generalUserDetail}" , type = EntityGraph.EntityGraphType.FETCH)
//    List<ClockIn> findAllByStatusNotAndClockInDateAndSchool_Id(Status status , LocalDate localDate , String schoolId);
//
//    @Query(nativeQuery = true, value = "select count(1)>0 " +
//            "from \"ClockIns\" C " +
//            "where C.\"status\"<>8 " +
//            "and C.\"schoolStaff_id\" =:staffId " +
//            "and C.\"clockInDate\" =:localDate ")
//    boolean existsByStatusNotDeletedAndSchoolStaff_IdAndClockInDate(@Param("staffId") String staffId, @Param("localDate") LocalDate localDate);
//
//
//    @Query(nativeQuery = true,
//            value = "select count(1)>0 " +
//                    "from \"ClockIns\" C " +
//                    "where C.\"status\"<>8 " +
//                    "and C.\"school_id\" =:schoolId " +
//                    "and C.\"schoolStaff_id\" =:staffId " +
//                    "and C.\"clockInDate\" =:localDate ")
//    boolean existsByStatusNotDeletedAndSchoolAndSchoolStaffAndClockInDate(@Param("schoolId") String schoolId, @Param("staffId") String staffId, @Param("localDate") LocalDate localDate);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where\n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND D.id =:districtId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :start AND :end \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndDistrictAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("districtId") String districtId, @Param("start") LocalDate start, @Param("end") LocalDate end);
//
//
//    @Query(nativeQuery = true,
//            value = "select S.id as schoolId ," +
//                    "S.\"schoolLevel\"  ,C.\"clockInDate\", S.\"schoolOwnership\" \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "where\n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()} \n" +
//                    "AND S.\"schoolOwnership\"=:#{#schoolOwnership.ordinal()} \n" +
//                    "AND S.district_id =:districtId \n" +
//                    "AND C.\"clockInDate\" BETWEEN :start AND :end \n" +
//                    "ORDER BY C.\"clockInDate\" ASC")
//
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndDistrictAndClockInDateBetweenAndSchoolOwnershipOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("districtId") String districtId, @Param("start") LocalDate start, @Param("end") LocalDate end, @Param("schoolOwnership") SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where\n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND D.id =:districtId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :start AND :end \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where\n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND D.id =:districtId \n" +
//                    "AND C.\"clockInDate\" BETWEEN :start AND :end \n" +
//                    "ORDER BY C.\"clockInDate\" ASC"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndDistrictAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("districtId") String districtId, @Param("start") LocalDate start, @Param("end") LocalDate end, Pageable pageable);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id,C.latitude," +
//                            "C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , " +
//                            "S.\"schoolLevel\"  , S.\"schoolOwnership\", \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "where \n" +
//                            "C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND C.\"schoolStaff_id\" NOTNULL " +
//                            "AND S.\"schoolLevel\" NOTNULL " +
//                            "AND C.\"clockInDate\" BETWEEN :start AND :end \n" +
//                            "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInSummaryProjection> findAllByStatusNotAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("start") LocalDate start, @Param("end") LocalDate end,@Param("schoolOwnership") SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where\n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND C.\"clockInDate\" BETWEEN :start AND :end \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//
//            countQuery = "select count(*) " +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where\n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND C.\"clockInDate\" BETWEEN :start AND :end \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("start") LocalDate start, @Param("end") LocalDate end, Pageable pageable);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where\n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND D.id =:districtId \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndAcademicTermAndDistrictOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("districtId") String districtId);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where\n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND D.id =:districtId \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where\n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND T.id =:academicTermId \n" +
//                    "AND D.id =:districtId \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndAcademicTermAndDistrictOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("districtId") String districtId, Pageable pageable);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND Y.id =:academicYearId \n" +
//                            "AND S.id =:schoolId \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndAcademicYearAndSchoolOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicYearId") String academicYearId, @Param("schoolId") String schoolId);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND Y.id =:academicYearId \n" +
//                            "AND S.id =:schoolId \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND Y.id =:academicYearId \n" +
//                    "AND S.id =:schoolId \n" +
//                    "ORDER BY C.\"clockInDate\" ASC"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndAcademicYearAndSchoolOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicYearId") String academicYearId, @Param("schoolId") String schoolId, Pageable pageable);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND ST.id =:staffId \n" +
//                            "AND D.id =:districtId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :start AND :end \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndSchoolStaffAndDistrictAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("staffId") String staffId, @Param("districtId") String districtId, @Param("start") LocalDate start, @Param("end") LocalDate end);
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND ST.id =:staffId \n" +
//                            "AND D.id =:districtId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :start AND :end \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND ST.id =:staffId \n" +
//                    "AND D.id =:districtId \n" +
//                    "AND C.\"clockInDate\" BETWEEN :start AND :end \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndSchoolStaffAndDistrictAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("staffId") String staffId, @Param("districtId") String districtId, @Param("start") LocalDate start, @Param("end") LocalDate end, Pageable pageable);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +"ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND ST.id =:staffId \n" +
//                            "AND C.\"clockInDate\" =:date \n")
//    @Transactional(readOnly = true)
//    Optional<ClockInProjection> findByStatusNotAndSchoolStaffAndClockInDate
//            (@Param("status") Status status, @Param("staffId") String staffId, @Param("date") LocalDate date);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND ST.\"staffCode\" =:staffCode \n" +
//                            "AND C.\"clockInDate\" =:date \n")
//    @Transactional(readOnly = true)
//    Optional<ClockInProjection> findByStatusNotAndStaffCodeAndClockInDate
//            (@Param("status") Status status, @Param("staffCode") String staffCode, @Param("date") LocalDate date);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND C.\"schoolStaff_id\" NOTNULL " +
//                            "AND C.\"clockInDate\" =:date",
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND C.\"schoolStaff_id\" NOTNULL " +
//                    "AND C.\"clockInDate\" =:date"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndClockInDateOrderByClockInDateAsc(@Param("status") Status status, @Param("date") LocalDate date, Pageable pageable);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where\n" +
//                            "C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND C.\"schoolStaff_id\" NOTNULL " +
//                            "AND D.id =:districtId " +
//                            "AND C.\"clockInDate\" =:clockInDate" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndDistrictAndClockInDateOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("districtId") String districtId, @Param("clockInDate") LocalDate clockInDate);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select S.id as schoolId ," +
//                            "S.\"schoolLevel\"  , S.\"schoolOwnership\" \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "where \n" +
//                            "C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND C.\"schoolStaff_id\" NOTNULL " +
//                            "AND S.\"district_id\" =:districtId " +
//                            "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} \n" +
//                            "AND C.\"clockInDate\" =:clockInDate "
//
//    )
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndDistrictAndClockInDateAndSchoolOwnershipOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("districtId") String districtId, @Param("clockInDate") LocalDate clockInDate, @Param("schoolOwnership") SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where\n" +
//                            "C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND C.\"schoolStaff_id\" NOTNULL " +
//                            "AND D.id =:districtId " +
//                            "AND C.\"clockInDate\" =:clockInDate" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where\n" +
//                    "C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND C.\"schoolStaff_id\" NOTNULL " +
//                    "AND D.id =:districtId " +
//                    "AND C.\"clockInDate\" =:clockInDate "
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndDistrictAndClockInDateOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("districtId") String districtId, @Param("clockInDate") LocalDate clockInDate, Pageable pageable);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, " +
//                            "C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode ," +
//                            " S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , " +
//                            "S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , " +
//                            "S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , " +
//                            "S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , " +
//                            "ST.\"registrationNo\" , ST.\"teachingstaff\" ,ST.\"staffType\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where\n" +
//                            "C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND C.\"schoolStaff_id\" NOTNULL " +
//                            "AND S.id =:schoolId " +
//                            "AND C.\"clockInDate\" =:clockInDate " +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndSchoolAndClockInDateOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("schoolId") String schoolId, @Param("clockInDate") LocalDate clockInDate);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, " +
//                            "C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , C.\"clockinType\" ,C.\"displacement\" ,\n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode ," +
//                            " S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , " +
//                            "S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , " +
//                            "S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , " +
//                            "S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , " +
//                            "ST.\"registrationNo\" , ST.\"teachingstaff\" ,ST.\"staffType\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId , " +
//                            " G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , " +
//                            "T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , " +
//                            "Y.\"endDate\" as academicYearEndDate, \n" +
//                            "CO.\"clockOutDate\", CO.\"clockOutTime\", CO.comment as clockOutComment \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "left join \"ClockOuts\" CO on CO.\"clockIn_id\"=C.id \n" +
//                            "where\n" +
//                            "C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND C.\"schoolStaff_id\" NOTNULL " +
//                            "AND S.id =:schoolId " +
//                            "AND C.\"clockInDate\" =:clockInDate " +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInWithClockOutProjection> findAllByStatusNotAndSchoolAndClockInDateOrderByClockInDateAscWithClockouts
//            (@Param("status") Status status, @Param("schoolId") String schoolId, @Param("clockInDate") LocalDate clockInDate);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where\n" +
//                            "C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND C.\"schoolStaff_id\" NOTNULL " +
//                            "AND S.id =:schoolId " +
//                            "AND C.\"clockInDate\" =:clockInDate " +
//                            "ORDER BY C.\"clockInDate\" ASC",
//
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where\n" +
//                    "C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND C.\"schoolStaff_id\" NOTNULL " +
//                    "AND S.id =:schoolId " +
//                    "AND C.\"clockInDate\" =:clockInDate"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndSchoolAndClockInDateOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("schoolId") String schoolId, @Param("clockInDate") LocalDate clockInDate, Pageable pageable);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where\n" +
//                            "C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND C.\"schoolStaff_id\" NOTNULL")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNot(@Param("status") Status status);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where\n" +
//                            "C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND C.\"schoolStaff_id\" NOTNULL",
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where\n" +
//                    "C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND C.\"schoolStaff_id\" NOTNULL"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNot(Status status, Pageable page);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND ST.id =:staffId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndAcademicTermAndStaffAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("staffId") String staffId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
//
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" ," +
//                            " S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , " +
//                            "S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , " +
//                            "S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , " +
//                            "ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , " +
//                            "G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , " +
//                            "T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , " +
//                            "Y.\"endDate\" as academicYearEndDate \n," +
//                            "CO.id as clockoutId , CO.comment as clockOutComment , CO.\"clockOutDate\" , CO.\"clockOutTime\"\n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "left join \"ClockOuts\" CO on C.id = CO.\"clockIn_id\" \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND S.id =:schoolId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInWithClockOutProjection> findAllByStatusNotAndSchoolAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("schoolId") String schoolId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND S.id =:schoolId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND S.id =:schoolId \n" +
//                    "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndSchoolAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("schoolId") String schoolId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
//
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND ST.id =:staffId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndStaffAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("staffId") String staffId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , " +
//                            "C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , " +
//                            "S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , " +
//                            "S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , " +
//                            "S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , " +
//                            "S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , " +
//                            "ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" ," +
//                            " G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , " +
//                            "T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate ," +
//                            " Y.\"endDate\" as academicYearEndDate, \n" +
//                            "CO.id as clockoutId , CO.comment as clockOutComment , CO.\"clockOutDate\" , CO.\"clockOutTime\" \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "inner join \"ClockOuts\" CO on C.id = CO.\"clockIn_id\" \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND ST.id =:staffId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInWithClockOutProjection> findAllByStatusNotAndStaffAndClockInDateBetweenOrderByClockInDateAscWithClockout
//            (@Param("status") Status status, @Param("staffId") String staffId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND ST.id =:staffId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND ST.id =:staffId \n" +
//                    "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n")
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndStaffAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("staffId") String staffId, @Param("fromDate") LocalDate fromDate,
//             @Param("toDate") LocalDate toDate, Pageable pageable);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND Y.id =:academicYearId \n" +
//                            "AND S.id =:schoolId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndAcademicYearAndSchoolAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicYearId") String academicYearId, @Param("schoolId") String schoolId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND Y.id =:academicYearId \n" +
//                            "AND S.id =:schoolId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND Y.id =:academicYearId \n" +
//                    "AND S.id =:schoolId \n" +
//                    "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndAcademicYearAndSchoolAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicYearId") String academicYearId, @Param("schoolId") String schoolId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
//
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND Y.id =:academicYearId \n" +
//                            "AND ST.id =:staffId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndAcademicYearAndStaffAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicYearId") String academicYearId, @Param("staffId") String staffId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND Y.id =:academicYearId \n" +
//                            "AND ST.id =:staffId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND Y.id =:academicYearId \n" +
//                    "AND ST.id =:staffId \n" +
//                    "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndAcademicYearAndStaffAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicYearId") String academicYearId, @Param("staffId") String staffId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND S.id =:schoolId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndAcademicTermAndSchoolAndClockInDateBetweenOrderByClockInDateAsc
//    (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("schoolId") String schoolId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND S.id =:schoolId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND T.id =:academicTermId \n" +
//                    "AND S.id =:schoolId \n" +
//                    "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndAcademicTermAndSchoolAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("schoolId") String schoolId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
//
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND S.id =:schoolId \n" +
//                            "AND C.\"clockInDate\" =:date \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndAcademicTermAndSchoolAndClockInDateOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("schoolId") String schoolId, @Param("date") LocalDate date);
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND S.id =:schoolId \n" +
//                            "AND C.\"clockInDate\" =:date \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND T.id =:academicTermId \n" +
//                    "AND S.id =:schoolId \n" +
//                    "AND C.\"clockInDate\" =:date \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndAcademicTermAndSchoolAndClockInDateOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("schoolId") String schoolId, @Param("date") LocalDate date, Pageable pageable);
//
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND S.id =:schoolId \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndAcademicTermAndSchoolOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("schoolId") String schoolId);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND S.id =:schoolId \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND T.id =:academicTermId \n" +
//                    "AND S.id =:schoolId \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndAcademicTermAndSchoolOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("schoolId") String schoolId, Pageable pageable);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndAcademicTermOrderByClockInDateAsc(@Param("status") Status status, @Param("academicTermId") String academicTermId);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND T.id =:academicTermId \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndAcademicTermOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, Pageable pageable);
//
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND S.id =:schoolId \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndSchoolOrderByClockInDateAsc(@Param("status") Status status, @Param("schoolId") String schoolId);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND S.id =:schoolId \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND S.id =:schoolId \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndSchoolOrderByClockInDateAsc(@Param("status") Status status, @Param("schoolId") String schoolId, Pageable pageable);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndAcademicTermAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND T.id =:academicTermId \n" +
//                    "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndAcademicTermAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
//
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND D.id =:districtId \n" +
//                            "AND C.\"clockInDate\" =:date \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndAcademicTermAndDistrictAndClockInDateOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("districtId") String districtId, @Param("date") LocalDate date);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND D.id =:districtId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndAcademicTermAndDistrictAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("districtId") String districtId,
//             @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND D.id =:districtId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND T.id =:academicTermId \n" +
//                    "AND D.id =:districtId \n" +
//                    "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate \n" +
//                    "ORDER BY C.\"clockInDate\" ASC"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndAcademicTermAndDistrictAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("districtId") String districtId,
//             @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND ST.id =:staffId \n" +
//                            "AND C.\"clockInDate\" =:date \n")
//    @Transactional(readOnly = true)
//    Optional<ClockInProjection> findAllByStatusNotAndStaffAndClockInDate
//            (@Param("status") Status status, @Param("staffId") String staffId, @Param("date") LocalDate date);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "CO.id as clockoutId , CO.comment as clockOutComment , CO.\"clockOutDate\" , CO.\"clockOutTime\" ,\n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "inner join \"Clockouts\" CO on C.id = CO.\"clockIn_id\" \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND D.id =:districtId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :startDate AND :endDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByStatusNotAndAcademicTermAndDistrictAndClockInDateBetweenOrderByClockInDateAscWithClockOut
//            (@Param("status") Status status, @Param("status") String academicTermId, @Param("status") String districtId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "CO.id as clockoutId , CO.comment as clockOutComment , CO.\"clockOutDate\" , CO.\"clockOutTime\" ,\n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "inner join \"Clockouts\" CO on C.id = CO.\"clockIn_id\" \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND D.id =:districtId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :startDate AND :endDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "inner join \"Clockouts\" CO on C.id = CO.\"clockIn_id\" \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND T.id =:academicTermId \n" +
//                    "AND D.id =:districtId \n" +
//                    "AND C.\"clockInDate\" BETWEEN :startDate AND :endDate \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndAcademicTermAndDistrictAndClockInDateBetweenOrderByClockInDateAscWithClockOut
//            (@Param("status") Status status, @Param("status") String academicTermId, @Param("status") String districtId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , " +
//                            "S.name as schoolName , " +
//                            "S.code as schoolCode , " +
//                            "S.\"deviceNumber\" , " +
//                            "S.\"emisNumber\" ," +
//                            "S.\"longitude\" as schoolLongitude , " +
//                            "S.\"latitude\" as schoolLatitude , " +
//                            "S.\"attendanceTracked\" , " +
//                            "S.\"activationStatus\" , " +
//                            "S.\"schoolLevel\" , " +
//                            "S.\"schoolGenderCategory\" , " +
//                            "S.\"schoolType\" , " +
//                            "S.\"schoolOwnership\" , " +
//                            "S.licensed , " +
//                            "S.\"rolloutPhase\" , " +
//                            "S.\"telaSchoolNumber\" , " +
//                            "S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , " +
//                            "ST.\"teachingstaff\" , " +
//                            "ST.\"staffType\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days, " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" ," +
//                            " G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , " +
//                            "T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "CO.id as clockoutId , CO.comment as clockOutComment , CO.\"clockOutDate\" , CO.\"clockOutTime\" ,\n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , " +
//                            "Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "right join \"ClockOuts\" CO on C.id = CO.\"clockIn_id\" \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND S.id =:schoolId \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInWithClockOutProjection> findAllByStatusNotAndAcademicTermAndSchoolOrderByClockInDateAscWithClockOutIn
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("schoolId") String schoolId);
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "CO.id as clockoutId , CO.comment as clockOutComment , CO.\"clockOutDate\" , CO.\"clockOutTime\" ,\n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "inner join \"Clockouts\" CO on C.id = CO.\"clockIn_id\" \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND T.id =:academicTermId \n" +
//                            "AND S.id =:schoolId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :startDate AND :endDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "inner join \"Clockouts\" CO on C.id = CO.\"clockIn_id\" \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND T.id =:academicTermId \n" +
//                    "AND S.id =:schoolId \n" +
//                    "AND C.\"clockInDate\" BETWEEN :startDate AND :endDate \n")
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByStatusNotAndAcademicTermAndSchoolOrderByClockInDateAscWithClockOutIn
//            (@Param("status") Status status, @Param("academicTermId") String academicTermId, @Param("schoolId") String schoolId, Pageable pageable);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND R.id =:regionId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :startDate AND :endDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC")
//    @Transactional(readOnly = true)
//    List<ClockInProjection> findAllByRegionAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("regionId") String regionId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id ,C.latitude,C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
//                            "S.id as schoolId , S.name as schoolName , S.code as schoolCode , S.\"deviceNumber\" , S.\"emisNumber\" , S.\"longitude\" as schoolLongitude , S.\"latitude\" as schoolLatitude , S.\"attendanceTracked\" , S.\"activationStatus\" , S.\"schoolLevel\" , S.\"schoolGenderCategory\" , S.\"schoolType\" , S.\"schoolOwnership\" , S.licensed , S.\"rolloutPhase\" , S.\"telaSchoolNumber\" , S.\"telaSchoolUID\",  \n" +
//                            "ST.id as staffId , ST.\"staffCode\" , ST.\"nationality\" , ST.\"registered\" , ST.\"registrationNo\" , ST.\"teachingstaff\" , " +
//                            "ST.\"expectedHours\" as hours, " +
//                            "ST.\"expectedDays\" as days , " +
//                            "G.\"firstName\" ,G.\"lastName\" , G.id as generalUserDetailId ,  G.dob , G.gender , G.\"phoneNumber\" , G.\"nationalId\" , G.\"nameAbbrev\" , G.email," +
//                            "T.id as academicTermId , T.term  , T.\"startDate\" as academicTermStartDate , T.\"endDate\" as academicTermEndDate , T.code as academicTermCode , T.\"assessmentPeriodType\" , " +
//                            "SG.name as schoolCategoryName, \n" +
//                            "D.id as districtId , D.name as districtName, \n" +
//                            "R.id as regionId , R.name as regionName, \n" +
//                            "Y.id as academicYearId , Y.name as academicYearName, Y.\"startDate\" as academicYearStartDate , Y.\"endDate\" as academicYearEndDate \n" +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"Schools\" S on C.school_id=S.id \n" +
//                            "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                            "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                            "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND R.id =:regionId \n" +
//                            "AND C.\"clockInDate\" BETWEEN :startDate AND :endDate \n" +
//                            "ORDER BY C.\"clockInDate\" ASC",
//            countQuery = "select count(*) \n" +
//                    "from \"ClockIns\" C \n" +
//                    "inner join \"Schools\" S on C.school_id=S.id \n" +
//                    "inner join \"SchoolCategories\" SG on S.\"schoolCategory_id\"=SG.id \n" +
//                    "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
//                    "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
//                    "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                    "inner join \"GeneralUserDetails\" G on ST.\"generalUserDetail_id\"=G.id \n" +
//                    "inner join \"AcademicTerms\" T on C.\"academicTerm_id\"=T.id \n" +
//                    "inner join \"AcademicYears\" Y on T.\"academicYear_id\"=Y.id \n" +
//                    "where \n" +
//                    "C.\"schoolStaff_id\" NOTNULL \n" +
//                    "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                    "AND R.id =:regionId \n" +
//                    "AND C.\"clockInDate\" BETWEEN :startDate AND :endDate \n"
//    )
//    @Transactional(readOnly = true)
//    Page<ClockInProjection> findAllByRegionAndClockInDateBetweenOrderByClockInDateAsc
//            (@Param("status") Status status, @Param("regionId") String regionId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
//
//
//    //@EntityGraph(value = "clockIn-staff-detail-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<ClockIn> findAllByStatusNotAndSchool_District_IdAndSchool_SchoolLevelAndSchool_SchoolOwnershipAndClockInDateBetweenOrderByClockInDateAsc(Status status, String districtId, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership, LocalDate start, LocalDate end);
//
//    //@EntityGraph(value = "clockIn-staff-detail-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<ClockIn> findAllByStatusNotAndAcademicTerm_IdAndSchool_District_IdAndSchool_SchoolLevelAndSchool_SchoolOwnershipAndClockInDateBetweenOrderByClockInDateAsc(Status status, String academicTermId, String districtId, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership, LocalDate fromDate, LocalDate toDate);
//
//
////    @EntityGraph(value = "clockIn-graph", type = EntityGraph.EntityGraphType.FETCH)
////    List<ClockIn> findAllByStatusNotAndSchool_District_IdAndClockInDateAndSchool_SchoolOwnershipOrderByClockInDateAsc
////            (Status status, String districtId, LocalDate clockInDate, SchoolOwnership schoolOwnership);
//
//
//    //
//    @EntityGraph(value = "clockin_base_graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<ClockIn> findAllByStatusNotAndSchoolStaff_StaffCodeAndClockInDate
//    (Status status, String staffCode, LocalDate date);
//
//
//    @Query(nativeQuery = true,
//            value =
//                    "select C.id as id " +
//                            "from \"ClockIns\" C \n" +
//                            "inner join \"SchoolStaffs\" ST on C.\"schoolStaff_id\"=ST.id \n" +
//                            "where \n" +
//                            "C.\"schoolStaff_id\" NOTNULL \n" +
//                            "AND C.\"status\" !=:#{#status.ordinal()}\n" +
//                            "AND ST.\"staffCode\" =:staffCode \n" +
//                            "AND C.\"clockInDate\" =:date \n")
//    @Transactional(readOnly = true)
//    List<String> getStaffClockInByDate
//            (@Param("status") Status status, @Param("staffCode") String staffCode, @Param("date") LocalDate date);
//
//    @Transactional
//    @Modifying(clearAutomatically = true)
//    @Query(nativeQuery = true,
//            value = "UPDATE \"ClockIns\" SET status = 8 WHERE id = :id")
//    void deleteStaffClockInByDate(@Param("id") String id);
//
//
//
//    @Query(nativeQuery = true,
//            value ="select\n" +
//                    "\"ClockIns\".id as id,\n" +
//                    "\"Schools\".id as schoolId,\n" +
//                    "\"Schools\".name as schoolName,\n" +
//                    "\"Schools\".\"schoolLevel\" as schoolLevel,\n" +
//                    "\"Schools\".\"schoolOwnership\" as schoolOwnership,\n" +
//                    "\"SchoolStaffs\".id as staffId,\n" +
//                    "\"staffType\",\n" +
//                    "\"gender\", \n" +
//                    "\"clockInDate\",\n" +
//                    "\"clockInTime\"\n" +
//                    "from \"ClockIns\"\n" +
//                    "left join \"Schools\" on \"ClockIns\".school_id=\"Schools\".id\n" +
//                    "left join \"SchoolStaffs\" ON \"SchoolStaffs\".school_id = \"Schools\".id\n" +
//                    "left join \"GeneralUserDetails\" ON \"SchoolStaffs\".\"generalUserDetail_id\" = \"GeneralUserDetails\".id\n" +
//                    "where \"Schools\".\"schoolOwnership\"=:#{#schoolOwnership.ordinal()} \n" +
//                    "and \"Schools\".district_id=:districtId \n" +
//                    "and \"ClockIns\".\"clockInDate\"=:clockInDate \n" +
//                    "and \"ClockIns\".\"status\" !=:#{#status.ordinal()}\n"+
//                    "and \"SchoolStaffs\".\"staffType\" NOTNULL \n"
//
//    )
//    List<SchoolStaffClockInProjection> findDistrictSchoolStaffClockInSummary
//            (@Param("status") Status status, @Param("districtId") String districtId,  @Param("clockInDate") LocalDate clockInDate, @Param("schoolOwnership") SchoolOwnership schoolOwnership);
//
//
////    @Query(nativeQuery = true,
////            value ="select\n" +
////                    "\"ClockIns\".id as id,\n" +
////                    "\"Schools\".id as schoolId,\n" +
////                    "\"Schools\".name as schoolName,\n" +
////                    "\"Schools\".\"schoolLevel\" as schoolLevel,\n" +
////                    "\"Schools\".\"schoolOwnership\" as schoolOwnership,\n" +
////                    //"\"Districts\".\"id\" as districtId,\n" +
////                    //"\"Districts\".\"name\" as districtName,\n" +
////                    "\"SchoolStaffs\".id as staffId,\n" +
////                    "\"staffType\",\n" +
////                    //"\"gender\", \n" +
////                    "\"clockInDate\",\n" +
////                    "\"clockInTime\"\n" +
////                    "from \"ClockIns\"\n" +
////                    "inner join \"Schools\" on \"ClockIns\".school_id=\"Schools\".id\n" +
////                    //"inner join \"Districts\" on \"Schools\".district_id=\"Districts\".id\n" +
////                    "inner join \"SchoolStaffs\" ON \"SchoolStaffs\".school_id = \"Schools\".id\n" +
////                    //"inner join \"GeneralUserDetails\" ON \"SchoolStaffs\".\"generalUserDetail_id\" = \"GeneralUserDetails\".id\n" +
////                    "where \"Schools\".\"schoolOwnership\"=:#{#schoolOwnership.ordinal()} \n" +
////                    "and \"ClockIns\".\"clockInDate\"=:clockInDate \n" +
////                    "and \"ClockIns\".\"status\" !=:#{#status.ordinal()}\n"+
////                    "and \"SchoolStaffs\".\"staffType\" NOTNULL \n"
////
////    )
//
//    @Query(nativeQuery = true,
//            value ="select \n" +
//                    "\t\t\t\t\t\"ClockIns\".id as id,\n" +
//                    "                    \"Schools\".id as schoolId,\n" +
//                    "                    \"Schools\".name as schoolName,\n" +
//                    "                    \"Schools\".\"schoolLevel\" as schoolLevel,\n" +
//                    "                    \"Schools\".\"schoolOwnership\" as schoolOwnership,\n" +
//                    "                    \"Districts\".id as districtId,\n" +
//                    "                    \"Districts\".name as districtName,\n" +
//                    "                    \"SchoolStaffs\".id as staffId,\n" +
//                    "                    \"staffType\",\n" +
//                    "                    \"gender\",\n" +
//                    "                    \"clockInDate\",\n" +
//                    "                    \"clockInTime\"\n" +
//                    "                    from \"ClockIns\"\n" +
//                    "                    inner join \"Schools\" on \"ClockIns\".school_id=\"Schools\".id\n" +
//                    "                    inner join \"Districts\" on \"Schools\".district_id=\"Districts\".id\n" +
//                    "                    inner join \"SchoolStaffs\" ON  \"SchoolStaffs\".school_id = \"Schools\".id\n" +
//                    "                    inner join \"GeneralUserDetails\" ON \"SchoolStaffs\".\"generalUserDetail_id\" =\"GeneralUserDetails\".id\n" +
//                    "                    where \"Schools\".\"schoolOwnership\"=0\n" +
//                    "                    and \"ClockIns\".\"clockInDate\"='2023-06-02'\n" +
//                    "                    and \"ClockIns\".\"status\" !=8\n" +
//                    "                    and \"SchoolStaffs\".\"staffType\" NOTNULL"
//
//    )
//    List<SchoolStaffClockInProjection> findSchoolStaffClockInSummary
//            (@Param("status") Status status,  @Param("clockInDate") LocalDate clockInDate, @Param("schoolOwnership") SchoolOwnership schoolOwnership);
//
//    /*
//      clockins
//      staff , profile
//      school
//      district
//      clockin
//
//     */
//
////    @Query("SELECT C FROM ClockIns C " +
////            " JOIN FETCH C.school S "+
////            " JOIN FETCH C.schoolStaff ST "+
////            " JOIN FETCH ST.generalUserDetail "+
////            " JOIN FETCH C.clockOut CK "+
////            "WHERE S.status <>:status AND  S.district.id =:districtId AND S.schoolOwnership =:schoolOwnership AND C.clockInDate BETWEEN :fromDate AND :toDate AND S.schoolLevel =:schoolLevel")
//
//    @Query(nativeQuery = true ,
//            value = "SELECT C.id as clockInId, C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", S.id as schoolId  , S.name as schoolName , ST.\"staffType\" as staffType , ST.id as staffId , " +
//
//                    "CK.\"clockOutTime\" , CK.\"clockOutDate\"  FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id =  C.\"schoolStaff_id\"\n" +
//                    "INNER JOIN \"GeneralUserDetails\" AS G ON G.id = ST.\"generalUserDetail_id\"\n" +
//                    "LEFT JOIN \"ClockOuts\" AS CK ON CK.\"clockIn_id\" = C.id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()} AND G.status <>:#{#status.ordinal()} " +
//                    "AND S.district_id =:districtId AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}  AND ST.\"staffType\" NOTNULL " +
//                    "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate ")
//    List<ClockInProjection2> findAllByDistrict_FromDate_To_SchoolLevel_SchoolOwnershipWithSchool_SchoolStaff_ClockOut(Status status, String districtId, LocalDate fromDate, LocalDate toDate, SchoolOwnership schoolOwnership , SchoolLevel schoolLevel);
//
//
////    @Query("SELECT C FROM ClockIns C " +
////            " JOIN FETCH C.school S "+
////            " JOIN FETCH C.schoolStaff ST "+
////            " JOIN FETCH ST.generalUserDetail "+
////            " JOIN FETCH C.clockOut CK "+
////            "WHERE S.status <>:status AND S.schoolOwnership =:schoolOwnership AND  C.academicTerm.id =:termId AND S.district.id =:districtId  AND S.schoolLevel =:schoolLevel")
//@Query(nativeQuery = true ,
//        value = "SELECT C.id as clockInId, C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", S.id as schoolId  , S.name as schoolName , ST.\"staffType\" as staffType , ST.id as staffId," +
//                "CK.\"clockOutTime\" , CK.\"clockOutDate\" FROM \"ClockIns\" AS C\n" +
//                "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id = C.\"schoolStaff_id\"\n" +
//                "INNER JOIN \"GeneralUserDetails\" AS G ON G.id = ST.\"generalUserDetail_id\"\n" +
//                "LEFT JOIN \"ClockOuts\" AS CK ON CK.\"clockIn_id\" = C.id\n" +
//                "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()} AND G.status <>:#{#status.ordinal()} " +
//                "AND S.district_id =:districtId AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}  AND ST.\"staffType\" NOTNULL " +
//                "AND C.\"academicTerm_id\" =:academicTermId ")
//    List<ClockInProjection2> findAllByAcademicTerm_District_SchoolLevel_SchoolOwnershipWithSchool_SchoolStaffDetail_ClockOut(Status status, String academicTermId , String districtId , SchoolOwnership schoolOwnership , SchoolLevel schoolLevel);
//
//
////    @Query("SELECT C FROM ClockIns C " +
////            " JOIN FETCH C.school S "+
////            "WHERE S.status <>:status AND S.schoolOwnership =:schoolOwnership  AND S.district.id =:districtId  AND C.clockInDate =:clockInDate")
//
//    @Query(nativeQuery = true ,
//            value = "SELECT C.id as clockInId, C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", " +
//                    "S.id as schoolId  , S.name as schoolName , S.\"schoolLevel\"  " +
//                    "FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} " +
//                    "AND S.district_id =:districtId AND C.\"clockInDate\" =:clockInDate ")
//    List<ClockInProjection2> findAllByDistrict_Date_SchoolOwnershipWithSchool(Status status, String districtId, LocalDate clockInDate, SchoolOwnership schoolOwnership);
//
//
////    @Query("SELECT C FROM ClockIns C " +
////            " JOIN FETCH C.school S "+
////            "WHERE S.status <>:status AND S.schoolOwnership =:schoolOwnership  AND S.district.id =:districtId  AND C.clockInDate BETWEEN :fromDate AND :endDate")
//
//    @Query(nativeQuery = true ,
//            value = "SELECT C.id as clockInId, C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", S.id as schoolId  , S.name as schoolName , S.\"schoolLevel\"  FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} " +
//                    "AND S.district_id =:districtId AND C.\"clockInDate\" BETWEEN :fromDate AND :endDate " )
//    List<ClockInProjection2> findAllByDistrict_DateFrom_To_SchoolOwnershipWithSchool(Status status, String districtId, LocalDate fromDate, LocalDate endDate, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true ,
//            value = "SELECT C.id as clockInId, C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", S.id as schoolId  , S.name as schoolName , S.\"schoolLevel\", " +
//                    "CK.\"clockOutTime\" , CK.\"clockOutDate\" FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "LEFT JOIN \"ClockOuts\" AS CK ON CK.\"clockIn_id\" = C.id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()}  " +
//                    "AND S.district_id =:districtId AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} " +
//                    "AND C.\"academicTerm_id\" =:academicTermId " )
//    List<ClockInProjection2> findAllByAcademicTerm_District_SchoolLevel_SchoolOwnershipWithSchool_ClockOut(Status status, String academicTermId, String districtId, SchoolOwnership schoolOwnership, SchoolLevel schoolLevel);
//
//
//@Query(nativeQuery = true ,
//        value = "SELECT C.id as clockInId, C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", S.id as schoolId  , S.name as schoolName , S.\"schoolLevel\" , " +
//                "CK.\"clockOutTime\" , CK.\"clockOutDate\" FROM \"ClockIns\" AS C\n" +
//                "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                "LEFT JOIN \"ClockOuts\" AS CK ON CK.\"clockIn_id\" = C.id\n" +
//                "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()}  " +
//                "AND S.district_id =:districtId AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} " +
//                "AND C.\"academicTerm_id\" =:academicTermId  AND C.\"clockInDate\" BETWEEN :startDate AND :endDate " )
//    List<ClockInProjection2> findAllByAcademicTerm_District_FromDate_To_SchoolLevel_SchoolOwnershipWithSchool_ClockOut(Status status, String academicTermId, String districtId, SchoolOwnership schoolOwnership, SchoolLevel schoolLevel, LocalDate startDate , LocalDate endDate);
//
//
//    @Query(nativeQuery = true ,
//            value = "SELECT C.id as clockInId, C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", S.id as schoolId  , S.name as schoolName , S.\"schoolLevel\", " +
//                    "CK.\"clockOutTime\" , CK.\"clockOutDate\" FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"ClockOuts\" AS CK ON CK.\"clockIn_id\" = C.id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()}  " +
//                    "AND S.district_id =:districtId AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} " +
//                    "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate " )
//    List<ClockInProjection2> findAllByDistrict_FromDate_To_SchoolLevel_SchoolOwnershipWithSchool_ClockOut(Status status, String districtId, LocalDate fromDate, LocalDate toDate, SchoolOwnership schoolOwnership, SchoolLevel schoolLevel);
//
//
//    @Query(nativeQuery = true ,
//            value = "SELECT C.id as clockInId, C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", S.id as schoolId  , S.name as schoolName , S.\"schoolLevel\", ST.\"staffType\" as staffType , ST.id as staffId ," +
//                    "CK.\"clockOutTime\" , CK.\"clockOutDate\" FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id = C.\"schoolStaff_id\"\n" +
//                    "LEFT JOIN \"ClockOuts\" AS CK ON CK.\"clockIn_id\" = C.id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()} AND CK.status <>:#{#status.ordinal()}\n" +
//                    "AND S.district_id =:districtId AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}  AND ST.\"staffType\" NOTNULL " +
//                    "AND C.\"clockInDate\" =:date ")
//    List<ClockInProjection2> findAllByDistrict_Date_SchoolLevel_SchoolOwnershipWithSchool_SchoolStaff_ClockOut(Status status, String districtId, LocalDate date, SchoolOwnership schoolOwnership, SchoolLevel schoolLevel);
//
//
//    @Query(nativeQuery = true ,
//            value = "SELECT C.id as clockInId, C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", S.id as schoolId  , S.name as schoolName , S.\"schoolLevel\", " +
//                    "ST.\"staffType\" as staffType , ST.id as staffId , G.gender , CK.\"clockOutTime\" , CK.\"clockOutDate\" FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id = C.\"schoolStaff_id\"\n" +
//                    "INNER JOIN \"GeneralUserDetails\" AS G ON G.id = ST.\"generalUserDetail_id\"\n" +
//                    "LEFT JOIN \"ClockOuts\" AS CK ON CK.\"clockIn_id\" = C.id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()} AND G.status <>:#{#status.ordinal()}  " +
//                    "AND S.district_id =:districtId AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}  AND ST.\"staffType\" NOTNULL " +
//                    "AND C.\"clockInDate\" =:date ")
//    List<ClockInProjection2> findAllByDistrict_Date_SchoolOwnership_SchoolLevelWithSchool_SchoolStaffDetail_ClockOut(Status status, String districtId, LocalDate date, SchoolOwnership schoolOwnership, SchoolLevel schoolLevel);
//
//
//    @Query(nativeQuery = true ,
//            value = "SELECT C.id as clockInId, C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", S.id as schoolId  , S.name as schoolName , S.\"schoolLevel\" FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} " +
//                    "AND S.district_id =:districtId AND C.\"clockInDate\" =:localDate " +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}")
//    List<ClockInProjection2> findAllByDistrict_Date_SchoolLevel_OwnershipWithSchool(Status status, String districtId, LocalDate localDate, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//
//    @Query(nativeQuery = true ,
//            value = "SELECT C.id as clockInId, C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", S.id as schoolId  , S.name as schoolName , S.\"schoolLevel\", ST.\"staffType\" as staffType , ST.id as staffId FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id = C.\"schoolStaff_id\"\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()} " +
//                    "AND S.district_id =:district AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}  AND ST.\"staffType\" NOTNULL " +
//                    "AND C.\"clockInDate\" =:localDate ")
//    List<ClockInProjection2> findAllByDistrict_Date_SchoolLevel_SchoolOwnershipWithSchool_SchoolStaff(Status status, String district , LocalDate localDate, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//
//    @Query(nativeQuery = true ,
//            value = "SELECT  ST.id AS staffId , S.id AS schoolId , D.id AS districtId , D.name AS districtName  FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "RIGHT JOIN \"Districts\" AS D ON S.district_id=D.id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id = C.\"schoolStaff_id\"\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND D.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()} " +
//                    "AND C.\"clockInDate\" =:localDate AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND ST.\"staffType\" NOTNULL")
//
//    List<ClockInProjection2> findAllByDate_SchoolLevel_SchoolOwnershipWithDistrict_School_SchoolStaff(Status status, SchoolLevel schoolLevel, LocalDate localDate, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT  C.id as clockInId ,C.\"clockInDate\" as clockInDate ,C.\"clockInTime\" , " +
//                    "C.\"clockinType\" as clockInType , S.id as schoolId , S.name as schoolName,S.\"schoolLevel\" as schoolLevel FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"Districts\" AS D ON D.id = S.district_id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND D.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}  \n" +
//                    "AND C.\"clockInDate\" =:clockInDate"
//    )
//    List<ClockInProjection2> findAllByDate_SchoolOwnershipWithDistrict_School(Status status,LocalDate clockInDate,SchoolOwnership schoolOwnership);
//
//    @Query(nativeQuery = true,
//            value = "SELECT COUNT(DISTINCT C.\"schoolStaff_id\") FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id = C.\"schoolStaff_id\"\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} " +
//                    "AND C.\"clockInDate\" =:localDate AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND ST.\"staffType\" NOTNULL AND ST.\"staffType\" =:#{#staffType.ordinal()}"
//
//    )
//    long countAllBySchoolLevel_Date_StaffType_SchoolOwnership(Status status, SchoolLevel schoolLevel, LocalDate localDate, SchoolOwnership schoolOwnership , StaffType staffType);
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT COUNT(*) FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id = C.\"schoolStaff_id\"\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()}  " +
//                    "AND C.\"clockInDate\" =:localDate AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND ST.\"staffType\" NOTNULL"
//
//    )
//    long countAllBySchoolLevel_Date_SchoolOwnership(Status status, SchoolLevel schoolLevel, LocalDate localDate, SchoolOwnership schoolOwnership);
//
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT  C.id as clockInId ,C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", C.\"clockinType\" as clockInType , S.id as schoolId , S.name as schoolName  FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()}  " +
//                    "AND C.\"clockInDate\" =:clockInDate AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND S.district_id =:districtId "
//    )
//    List<ClockInProjection2> findAllByDistrict_Date_SchoolLevel_SchoolOwnershipWithSchool(Status status, String districtId, LocalDate clockInDate, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT  C.id as clockInId ,C.\"clockInDate\" as clockInDate ,C.\"clockInTime\" , C.\"clockinType\" as clockInType , S.id as schoolId , S.name as schoolName , G.gender " +
//                    "FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id = C.\"schoolStaff_id\"\n" +
//                    "INNER JOIN \"GeneralUserDetails\" AS G ON G.id = ST.\"generalUserDetail_id\"\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()}  AND G.status <>:#{#status.ordinal()}   " +
//                    "AND C.\"clockInDate\" BETWEEN :startDate AND :endDate AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND S.district_id =:districtId "
//    )
//    List<ClockInProjection2> findAllByDistrict_From_Date_To_SchoolLevel_SchoolOwnershipWithSchool_SchoolStaffDetail(Status status, String districtId, LocalDate startDate, LocalDate endDate, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
////    @Query(nativeQuery = true,
////            value =
////                    "select C.id as id,C.latitude," +
////                            "C.longitude,C.comment, C.\"clockInDate\", C.\"clockInTime\" , C.\"clockedStatus\" , \n" +
////                            "S.id as schoolId , S.name as schoolName , " +
////                            "S.\"schoolLevel\"  , S.\"schoolOwnership\", \n" +
////                            "D.id as districtId , D.name as districtName, \n" +
////                            "R.id as regionId , R.name as regionName \n" +
////                            "from \"ClockIns\" C \n" +
////                            "inner join \"Schools\" S on C.school_id=S.id \n" +
////                            "inner join \"Districts\" D on S.\"district_id\"=D.id \n" +
////                            "inner join \"Regions\" R on D.\"region_id\"=R.id \n" +
////                            "where \n" +
////                            "C.\"status\" !=:#{#status.ordinal()}\n" +
////                            "AND C.\"schoolStaff_id\" NOTNULL " +
////                            "AND S.\"schoolLevel\" NOTNULL " +
////                            "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} \n" +
////                            "AND C.\"clockInDate\" =:date")
////    @Transactional(readOnly = true)
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT C.id as clockInId , C.\"schoolStaff_id\" as staffId,ST.\"staffType\" as staffType FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id = C.\"schoolStaff_id\"\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()} " +
//                    "AND C.\"clockInDate\" =:localDate AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} AND ST.\"staffType\" NOTNULL"
//
//    )
//    List<ClockInProjection2> findAllBySchoolLevel_Date_SchoolOwnershipWithSchool_SchoolStaff(Status status, SchoolLevel schoolLevel, LocalDate localDate, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT C.id as clockInId , C.\"schoolStaff_id\" as staffId,ST.\"staffType\" as staffType FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id = C.\"schoolStaff_id\"\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()} " +
//                    "AND S.id =:schoolId AND C.\"clockInDate\" =:clockInDate  AND ST.\"staffType\" NOTNULL"
//
//    )
//    List<ClockInProjection2> findAllBySchool_DateWithSchool_SchoolStaff(Status status, String schoolId, LocalDate clockInDate);
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT  C.id as clockInId ,C.\"clockInDate\" as clockInDate ,C.\"clockInTime\" , C.\"clockinType\" as clockInType , G.gender, C.\"schoolStaff_id\" as staffId , ST.\"staffType\" as staffType , " +
//                    "G.\"firstName\" , G.\"lastName\" , G.\"phoneNumber\" , CK.\"clockOutTime\" " +
//                    "FROM \"ClockIns\" AS C \n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id = C.\"schoolStaff_id\"\n" +
//                    "INNER JOIN \"GeneralUserDetails\" AS G ON G.id = ST.\"generalUserDetail_id\"\n " +
//                    "LEFT JOIN \"ClockOuts\" AS CK ON CK.\"clockIn_id\" = C.id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()} " +
//                    "AND S.id =:school AND C.\"clockInDate\" BETWEEN  :fromDate AND :toDate  AND ST.\"staffType\" =:#{#staffType.ordinal()}"
//
//    )
//    List<ClockInProjection2> findAllBySchool_From_To_Date_StaffTypeWithSchool_SchoolStaffDetail_Clockout(Status status, String school, LocalDate fromDate, LocalDate toDate, StaffType staffType);
//
//
//    @Query(nativeQuery = true ,
//            value = "SELECT C.id as clockInId, C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", S.id as schoolId  , S.name as schoolName , ST.\"staffType\" as staffType , ST.id as staffId , " +
//                    "CK.\"clockOutTime\" , CK.\"clockOutDate\",D.\"id\" as districtId,  D.\"name\" as districtName  FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"Districts\" AS D ON D.id = S.district_id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id =  C.\"schoolStaff_id\"\n" +
//                    "INNER JOIN \"GeneralUserDetails\" AS G ON G.id = ST.\"generalUserDetail_id\"\n" +
//                    "LEFT JOIN \"ClockOuts\" AS CK ON CK.\"clockIn_id\" = C.id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()} AND G.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}  AND ST.\"staffType\" NOTNULL " +
//                    "AND C.\"clockInDate\" BETWEEN :fromDate AND :toDate ")
//    List<ClockInProjection2> findAllByFromDate_To_SchoolLevel_SchoolOwnershipWithSchool_SchoolStaff_ClockOut(Status status, LocalDate fromDate, LocalDate toDate, SchoolOwnership schoolOwnership , SchoolLevel schoolLevel);
//
//
//
//
//    @Query(nativeQuery = true ,
//            value = "SELECT C.id as clockInId," +
//                    "C.\"clockInDate\" as clockInDate ," +
//                    "C.\"clockInTime\"," +
//                    "S.id as schoolId  ," +
//                    "C.\"schoolStaff_id\" as staffId ," +
//                    "CK.\"clockOutTime\" ," +
//                    "CK.\"clockOutDate\"," +
//                    "D.\"id\" as districtId," +
//                    "D.\"name\" as districtName " +
//                    "FROM \"ClockIns\" AS C \n" +
//                    "LEFT JOIN \"ClockOuts\" AS CK ON CK.\"clockIn_id\" = C.id\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"Districts\" AS D ON D.id = S.district_id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} \n" +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} \n" +
//                    "AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} \n " +
//                    "AND C.\"academicTerm_id\" =:termId")
//    List<ClockInProjection2> findAllByTerm_SchoolLevel_SchoolOwnershipWithSchool_SchoolStaff_ClockOut(Status status, String termId, SchoolOwnership schoolOwnership , SchoolLevel schoolLevel);
//
//
//    @Query(nativeQuery = true ,
//            value = "SELECT C.id as clockInId, C.\"clockInDate\" as clockInDate ,C.\"clockInTime\", S.id as schoolId  , S.name as schoolName , S.\"schoolLevel\" , " +
//                    "CK.\"clockOutTime\" , CK.\"clockOutDate\" FROM \"ClockIns\" AS C\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "LEFT JOIN \"ClockOuts\" AS CK ON CK.\"clockIn_id\" = C.id\n" +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()}  " +
//                    "AND S.district_id =:districtId AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} " +
//                    "AND C.\"academicTerm_id\" =:academicTermId  AND C.\"clockInDate\" BETWEEN :startDate AND :endDate " )
//    List<ClockInProjection2> findAllByAcademicTerm_FromDate_To_SchoolLevel_SchoolOwnershipWithSchool_ClockOut(Status status, String academicTermId, SchoolOwnership schoolOwnership, SchoolLevel schoolLevel, LocalDate startDate , LocalDate endDate);
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT  C.id as clockInId ,C.\"clockInDate\" as clockInDate ,C.\"clockInTime\" , C.\"clockinType\" as clockInType , G.gender, C.\"schoolStaff_id\" as staffId , ST.\"staffType\" as staffType , " +
//                    "G.\"firstName\" , G.\"lastName\" , G.\"phoneNumber\" , S.id as schoolId  , S.name as schoolName , S.\"schoolLevel\"  " +
//                    "FROM \"ClockIns\" AS C \n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = C.school_id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id = C.\"schoolStaff_id\"\n" +
//                    "INNER JOIN \"GeneralUserDetails\" AS G ON G.id = ST.\"generalUserDetail_id\"\n " +
//                    "WHERE C.status <>:#{#status.ordinal()} AND S.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()} " +
//                    "AND S.district_id =:districtId AND C.\"clockInDate\" =:clockInDate  AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}"
//
//    )
//
//    List<ClockInProjection2> findAllByDistrict_Date_OwnershipWithSchool_SchoolStaff(Status status, String districtId, LocalDate clockInDate, SchoolOwnership schoolOwnership);
}
