package com.planetsystems.tela.api.ClockInOutConsumer.Repository;


import com.planetsystems.tela.api.ClockInOutConsumer.model.LearnerEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LearnerEnrollmentRepository extends JpaRepository<LearnerEnrollment, String> {

    @Query(value = """
            SELECT LE FROM LearnerEnrollment AS LE
            JOIN FETCH LE.schoolClass AS SC
            WHERE LE.enrollmentStatus <> 8 AND SC.status <> 8
            AND SC.school.id =:schoolId AND SC.academicTerm.id =:termId
            """)
    List<LearnerEnrollment> allBySchool_term(String schoolId, String termId);


//    @EntityGraph(value = "learnerEnrollments-with-schoolClass-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerEnrollment> findAllByStatusNot(Status status);
//    @EntityGraph(value = "learnerEnrollments-with-schoolClass-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<LearnerEnrollment> findByStatusNotAndId(Status status , String id);
//
//    boolean existsByStatusNotAndSchoolClass_IdAndSchoolClass_AcademicTerm_Id(Status status , String classId , String termId);
//    @EntityGraph(value = "learnerEnrollments-with-schoolClass-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<LearnerEnrollment> findByStatusNotAndSchoolClass_Id(Status status , String classId);
//    @EntityGraph(value = "learnerEnrollments-with-schoolClass-graph" , type = EntityGraph.EntityGraphType.FETCH)
//
//    List<LearnerEnrollment> findAllByStatusNotAndSchoolClass_School_District_RolledOut(Status status , boolean rolledOut);
//    @EntityGraph(value = "learnerEnrollments-with-schoolClass-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerEnrollment> findAllByStatusNotAndSchoolClass_AcademicTerm_IdAndSchoolClass_School_Id(Status status  , String termId , String schoolId);
//    @EntityGraph(value = "learnerEnrollments-with-schoolClass-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerEnrollment> findAllByStatusNotAndSchoolClass_AcademicTerm_Id(Status status  , String termId);
//    @EntityGraph(value = "learnerEnrollments-with-schoolClass-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerEnrollment> findAllByStatusNotAndSchoolClass_AcademicTerm_IdAndSchoolClass_School_District_Region_Id(Status status  , String termId , String regionId);
//    @EntityGraph(value = "learnerEnrollments-with-schoolClass-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerEnrollment> findAllByStatusNotAndSchoolClass_AcademicTerm_IdAndSchoolClass_School_District_Id(Status status  , String termId , String districtId);
//    @EntityGraph(value = "learnerEnrollments-with-schoolClass-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    List<LearnerEnrollment> findAllByStatusNotAndSchoolClass_School_Id(Status status  , String schoolId);
//
//    @EntityGraph(value = "learnerEnrollments-with-schoolClass-graph" , type = EntityGraph.EntityGraphType.FETCH)
//    Optional<LearnerEnrollment> findByStatusNotAndSchoolClass_AcademicTerm_IdAndSchoolClass_Id(Status status  , String termId , String classId);
//
//    @Query("SELECT E FROM LearnerEnrollment E " +
//            " WHERE E.status <>:status AND E.schoolClass.id =:schoolClassId")
//    Optional<LearnerEnrollment> findBySchoolClass(Status status, String schoolClassId);
//
////    @Query("SELECT E FROM LearnerEnrollment E " +
////            "JOIN FETCH E.schoolClass SC " +
////            "JOIN FETCH SC.school S " +
////            " WHERE E.status <>:status AND S.district.id =:districtId AND S.schoolLevel =:schoolLevel and S.schoolOwnership =:schoolOwnership ")
//    @Query(nativeQuery = true ,
//            value = "SELECT LE.\"totalBoys\" as learnerEnrollmentTotalBoys ,  LE.\"totalGirls\"  as learnerEnrollmentTotalGirls , LE.\"submissionDate\"  as learnerEnrollmentSubmissionDate , " +
//                    "SC.\"name\" AS SchoolClassName ," +
//                    "S.\"name\" as schoolName ,S.id as schoolId ,S.district_id AS districtId FROM \"LearnerEnrollments\" AS LE\n" +
//                    "INNER JOIN \"SchoolClasses\" AS SC ON SC.id = LE.\"schoolClass_id\"\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = SC.school_id\n" +
//                    "WHERE LE.status <>:#{#status.ordinal()} AND SC.status <>:#{#status.ordinal()}  AND S.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} " +
//                    "AND S.district_id =:districtId")
//    List<LearnerEnrollmentProjection2> findAllByDistrict_SchoolLevel_SchoolOwnershipWithSchool_SchoolClass(Status status, String districtId , SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true ,
//            value ="SELECT count(*) FROM \"LearnerEnrollments\" AS LE\n" +
//                    "INNER JOIN \"SchoolClasses\" AS SC ON SC.id = LE.\"schoolClass_id\"\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = SC.school_id\n" +
//                    "WHERE LE.status <>:#{#status.ordinal()} AND SC.status <>:#{#status.ordinal()}  AND S.status <>:#{#status.ordinal()} " +
//                    "AND LE.\"submissionDate\" =:localDate AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} " +
//                    "AND S.district_id =:districtId ")
//    long dbCountAllByDistrict_Date_SchoolLevel_SchoolOwnershipWithSchool_SchoolClass(Status status, String districtId, LocalDate localDate, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true ,
//            value ="SELECT LE.\"totalBoys\" as learnerEnrollmentTotalBoys ,  LE.\"totalGirls\"  as learnerEnrollmentTotalGirls , LE.\"submissionDate\"  as learnerEnrollmentSubmissionDate , " +
//                    "SC.\"name\" AS SchoolClassName ," +
//                    "S.\"name\" as schoolName ,S.id as schoolId ,S.district_id AS districtId FROM \"LearnerEnrollments\" AS LE\n" +
//                    "INNER JOIN \"SchoolClasses\" AS SC ON SC.id = LE.\"schoolClass_id\"\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = SC.school_id\n" +
//                    "WHERE LE.status <>:#{#status.ordinal()} AND SC.status <>:#{#status.ordinal()}  AND S.status <>:#{#status.ordinal()} " +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()}")
//    List<LearnerEnrollmentProjection2> findAllBySchoolLevel_SchoolOwnershipWithSchool_SchoolClass(Status status, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//@Query(nativeQuery = true, value= """
//        SELECT SUM(le."totalBoys") as boys, SUM(le."totalGirls") AS girls
//        FROM "LearnerEnrollments" AS le
//        INNER JOIN "SchoolClasses" AS sc ON\s
//        sc.id = le."schoolClass_id"\s
//        INNER JOIN "AcademicTerms" AS at ON\s
//        at.id = sc."academicTerm_id"\s
//        INNER JOIN "Schools" sch ON\s
//        sch.id = sc.school_id\s
//        WHERE at."activationStatus" IN (6)
//        AND sch."schoolOwnership" =:#{#schoolOwnership.ordinal()}
//        AND sch."schoolLevel" =:#{#schoolLevel.ordinal()}
//        AND le.status IN (6)
//        """)
//NationalLearnerHeadCountProjection findNationalLearnerHeadCountSummaryBySchoolLevel_SchoolOwnership(SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true, value= """
//        SELECT SUM(le."totalBoys") as boys, SUM(le."totalGirls") AS girls
//        FROM "SNLearnerEnrollments" AS le
//        INNER JOIN "SchoolClasses" AS sc ON\s
//        sc.id = le."schoolClass_id"\s
//        INNER JOIN "AcademicTerms" AS at ON\s
//        at.id = sc."academicTerm_id"\s
//        INNER JOIN "Schools" sch ON\s
//        sch.id = sc.school_id\s
//        WHERE at."activationStatus" IN (6)
//        AND sch."schoolOwnership" =:#{#schoolOwnership.ordinal()}
//        AND sch."schoolLevel" =:#{#schoolLevel.ordinal()}
//        AND le.status IN (6)
//        """)
//    SNNationalLearnerHeadCountProjection findSNNationalLearnerHeadCountSummaryBySchoolLevel_SchoolOwnership(SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true,value= """
//            SELECT reg.name,
//            reg.id,
//            (SELECT COALESCE(SUM(le."totalBoys"), 0) as boys FROM "LearnerEnrollments" AS le
//            INNER JOIN "SchoolClasses" AS sc ON\s
//            sc.id = le."schoolClass_id"\s
//            INNER JOIN "AcademicTerms" AS at ON\s
//            at.id = sc."academicTerm_id"\s
//            INNER JOIN "Schools" sch ON\s
//            sch.id = sc.school_id\s
//            INNER JOIN "Districts" dst ON\s
//            dst.id = sch.district_id
//            INNER JOIN "Regions" rg ON\s
//            rg.id = dst.region_id\s
//            WHERE at."activationStatus" IN (6)
//            AND sch."schoolOwnership" =:#{#schoolOwnership.ordinal()}
//            AND sch."schoolLevel" =:#{#schoolLevel.ordinal()}
//            AND le.status IN (6)
//            AND rg.status <> 8
//            AND dst.status <> 8
//            AND sch.status <> 8
//            AND sc.status <> 8\s
//            AND rg.id = reg.id ) AS boys,\s
//
//            (SELECT COALESCE(SUM(le."totalGirls"), 0) AS girls FROM "LearnerEnrollments" AS le
//            INNER JOIN "SchoolClasses" AS sc ON\s
//            sc.id = le."schoolClass_id"\s
//            INNER JOIN "AcademicTerms" AS at ON\s
//            at.id = sc."academicTerm_id"\s
//            INNER JOIN "Schools" sch ON\s
//            sch.id = sc.school_id\s
//            INNER JOIN "Districts" dst ON\s
//            dst.id = sch.district_id
//            INNER JOIN "Regions" rg ON\s
//            rg.id = dst.region_id\s
//            WHERE at."activationStatus" IN (6)
//           AND sch."schoolOwnership" =:#{#schoolOwnership.ordinal()}
//            AND sch."schoolLevel" =:#{#schoolLevel.ordinal()}
//            AND le.status IN (6)
//            AND rg.status <> 8
//            AND dst.status <> 8
//            AND sch.status <> 8
//            AND sc.status <> 8\s
//            AND rg.id = reg.id ) AS girls,
//            (SELECT COALESCE(SUM(le."totalBoys" + le."totalGirls"), 0) as total FROM "LearnerEnrollments" AS le
//            INNER JOIN "SchoolClasses" AS sc ON\s
//            sc.id = le."schoolClass_id"\s
//            INNER JOIN "AcademicTerms" AS at ON\s
//            at.id = sc."academicTerm_id"\s
//            INNER JOIN "Schools" sch ON\s
//            sch.id = sc.school_id\s
//            INNER JOIN "Districts" dst ON\s
//            dst.id = sch.district_id
//            INNER JOIN "Regions" rg ON\s
//            rg.id = dst.region_id\s
//            WHERE at."activationStatus" IN (6)
//            AND sch."schoolOwnership" =:#{#schoolOwnership.ordinal()}
//            AND sch."schoolLevel" =:#{#schoolLevel.ordinal()}
//            AND le.status IN (6)
//            AND rg.status <> 8
//            AND dst.status <> 8
//            AND sch.status <> 8
//            AND sc.status <> 8\s
//            AND rg.id = reg.id ) AS total
//
//            FROM "Regions" reg\s
//            WHERE reg.status <> 8
//
//            GROUP BY reg.name,reg.id
//            ORDER BY reg.name ASC
//
//
//            """)
//    List<RegionalLearnerHeadCountProjection> findRegionalLearnerHeadCountSummaryBySchoolLevel_SchoolOwnership(SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true,value= """
//            SELECT reg.name,
//            reg.id,
//            (SELECT COALESCE(SUM(le."totalBoys"), 0) as boys FROM "SNLearnerEnrollments" AS le
//            INNER JOIN "SchoolClasses" AS sc ON\s
//            sc.id = le."schoolClass_id"\s
//            INNER JOIN "AcademicTerms" AS at ON\s
//            at.id = sc."academicTerm_id"\s
//            INNER JOIN "Schools" sch ON\s
//            sch.id = sc.school_id\s
//            INNER JOIN "Districts" dst ON\s
//            dst.id = sch.district_id
//            INNER JOIN "Regions" rg ON\s
//            rg.id = dst.region_id\s
//            WHERE at."activationStatus" IN (6)
//            AND sch."schoolOwnership" =:#{#schoolOwnership.ordinal()}
//            AND sch."schoolLevel" =:#{#schoolLevel.ordinal()}
//            AND le.status IN (6)
//            AND rg.status <> 8
//            AND dst.status <> 8
//            AND sch.status <> 8
//            AND sc.status <> 8\s
//            AND rg.id = reg.id ) AS boys,\s
//
//            (SELECT COALESCE(SUM(le."totalGirls"), 0) AS girls FROM "SNLearnerEnrollments" AS le
//            INNER JOIN "SchoolClasses" AS sc ON\s
//            sc.id = le."schoolClass_id"\s
//            INNER JOIN "AcademicTerms" AS at ON\s
//            at.id = sc."academicTerm_id"\s
//            INNER JOIN "Schools" sch ON\s
//            sch.id = sc.school_id\s
//            INNER JOIN "Districts" dst ON\s
//            dst.id = sch.district_id
//            INNER JOIN "Regions" rg ON\s
//            rg.id = dst.region_id\s
//            WHERE at."activationStatus" IN (6)
//            AND sch."schoolOwnership" =:#{#schoolOwnership.ordinal()}
//            AND sch."schoolLevel" =:#{#schoolLevel.ordinal()}
//            AND le.status IN (6)
//            AND rg.status <> 8
//            AND dst.status <> 8
//            AND sch.status <> 8
//            AND sc.status <> 8\s
//            AND rg.id = reg.id ) AS girls,
//            (SELECT COALESCE(SUM(le."totalBoys" + le."totalGirls"), 0) as total FROM "SNLearnerEnrollments" AS le
//            INNER JOIN "SchoolClasses" AS sc ON\s
//            sc.id = le."schoolClass_id"\s
//            INNER JOIN "AcademicTerms" AS at ON\s
//            at.id = sc."academicTerm_id"\s
//            INNER JOIN "Schools" sch ON\s
//            sch.id = sc.school_id\s
//            INNER JOIN "Districts" dst ON\s
//            dst.id = sch.district_id
//            INNER JOIN "Regions" rg ON\s
//            rg.id = dst.region_id\s
//            WHERE at."activationStatus" IN (6)
//            AND sch."schoolOwnership" =:#{#schoolOwnership.ordinal()}
//            AND sch."schoolLevel" =:#{#schoolLevel.ordinal()}
//            AND le.status IN (6)
//            AND rg.status <> 8
//            AND dst.status <> 8
//            AND sch.status <> 8
//            AND sc.status <> 8\s
//            AND rg.id = reg.id ) AS total
//
//            FROM "Regions" reg\s
//            WHERE reg.status <> 8
//
//            GROUP BY reg.name,reg.id
//            ORDER BY reg.name ASC
//            """)
//    List<RegionalSNLearnerHeadCountProjection> findRegionalSNLearnerHeadCountSummaryBySchoolLevel_SchoolOwnership(SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//

}
