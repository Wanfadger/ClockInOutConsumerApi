package com.planetsystems.tela.api.ClockInOutConsumer.Repository;


import com.planetsystems.tela.api.ClockInOutConsumer.model.StaffDailyAttendanceSupervision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

;import java.time.LocalDate;
import java.util.List;

@Repository
public interface StaffDailyAttendanceSupervisionRepository extends JpaRepository<StaffDailyAttendanceSupervision, String> {


    @Query("""
            SELECT SAS FROM StaffDailyAttendanceSupervision AS SAS
            JOIN FETCH SAS.schoolStaff AS ST
            WHERE SAS.status <> 8 AND ST.status <> 8
            AND ST.school.id =:schoolId AND SAS.supervisionDate BETWEEN :termStartDate AND :termEndDate
            """)
    List<StaffDailyAttendanceSupervision> allByTerm_School(LocalDate termStartDate , LocalDate termEndDate ,String schoolId );

    @Query("""
            SELECT SAS FROM StaffDailyAttendanceSupervision AS SAS
            JOIN FETCH SAS.schoolStaff AS ST
            WHERE SAS.status <> 8 AND ST.status <> 8
            AND ST.school.id =:schoolId AND SAS.supervisionDate =:supervisionDate
            """)
    List<StaffDailyAttendanceSupervision> allByTerm_School(LocalDate supervisionDate , String schoolId);

    @Query("""
            SELECT SAS FROM StaffDailyAttendanceSupervision AS SAS
            JOIN FETCH SAS.schoolStaff AS ST
            WHERE SAS.status <> 8 AND ST.status <> 8
            AND ST.school.id =:schoolId AND SAS.supervisionDate =:localDate
            """)
    List<StaffDailyAttendanceSupervision> allByDate_School(LocalDate localDate, String id);

    //    @EntityGraph(value = "staff-daily-attendance-supervision-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<StaffDailyAttendanceSupervision> findAllByStatusNot(Status status);
//
//    Optional<StaffDailyAttendanceSupervision> findByStatusNotAndId(Status status, String id);
//
//    @EntityGraph(value = "staff-daily-attendance-supervision-graph", type = EntityGraph.EntityGraphType.FETCH)
//    List<StaffDailyAttendanceSupervision> findAllByStatusNotAndSchoolStaff_School_IdAndSupervisionDate(Status status, String schoolId, LocalDate supervisionDate);
//
//    @EntityGraph(value = "staff-daily-attendance-supervision-graph", type = EntityGraph.EntityGraphType.FETCH)
//    Optional<StaffDailyAttendanceSupervision> findByStatusNotAndSupervisionDateAndSchoolStaff_Id(Status status, LocalDate supervisionDate, String staffId);
//
//    boolean existsByStatusNotAndSupervisionDateAndSchoolStaff_Id(Status status, LocalDate supervisionDate, String staffId);
//
//
//    @Query(nativeQuery = true,
//            value = "SELECT SD.id AS staffDailyAttendanceSupervisionId , SD.comment AS staffDailyAttendanceSupervisionComment , \n" +
//                    "SD.\"supervisionDate\" AS staffDailyAttendanceSupervisionDate , SD.\"supervisionTime\" AS staffDailyAttendanceSupervisionTime, \n" +
//                    "SD.\"attendanceStatus\" AS staffDailyAttendanceSupervisionAttendanceStatus , S.id AS schoolId , S.name as schoolName\n" +
//                    "\n" +
//                    "from \"StaffDailyAttendanceSupervisions\" as SD\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS SUP ON SUP.id = SD.\"schoolStaff_id\"\n" +
//                    "INNER JOIN \"Schools\" AS S ON S.id = SUP.school_id\n" +
//                    "INNER JOIN \"SchoolStaffs\" AS ST ON ST.id = SD.\"schoolStaff_id\"\n" +
//                    "WHERE SD.status <>:#{#status.ordinal()} AND SUP.status <>:#{#status.ordinal()} AND ST.status <>:#{#status.ordinal()}\n" +
//                    "AND S.\"schoolLevel\" =:#{#schoolLevel.ordinal()} AND S.\"schoolOwnership\" =:#{#schoolOwnership.ordinal()} \n" +
//                    "AND SD.\"supervisionDate\" =:supervisionDate AND S.id =:schoolId ")
//    List<StaffDailyAttendanceSupervisionProjection> findAllBySchool_Date_SchoolLevel_SchoolOwnership(Status status, String schoolId, LocalDate supervisionDate, SchoolLevel schoolLevel, SchoolOwnership schoolOwnership);
//
//
//    @Query(nativeQuery = true,
//            value = """
//                    select
//                        sgud."firstName" as staffFirstName,
//                        sgud."lastName" as staffLastName,
//                        CASE
//                            WHEN  sss."staffType" =0 THEN 'Teacher'
//                            WHEN  sss."staffType" =1 THEN 'Headteacher'
//                            WHEN  sss."staffType" =2 THEN 'Deputy headteacher'
//                            WHEN  sss."staffType" =3 THEN 'Smc'
//                            WHEN  sss."staffType" =4 THEN 'Principal'
//                            WHEN  sss."staffType" =5 THEN 'Bursar'
//                            WHEN  sss."staffType" =6 THEN 'Librarian'
//                            WHEN  sss."staffType" =7 THEN 'Lab technician'
//                            ELSE 'Others'
//                            END AS staffType,
//                        cin."clockInTime",
//                        CASE
//                            WHEN  SAS."attendanceStatus" =0 THEN 'Present'
//                            ELSE 'Absent'
//                            END AS attendanceStatus,
//                        SAS."comment",
//                        SAS."supervisionDate",
//                        SAS."supervisionTime"
//                    from "StaffDailyAttendanceSupervisions" as SAS
//                    inner join  "SchoolStaffs"  as sss
//                    on SAS."schoolStaff_id"= sss.id
//                    inner join "GeneralUserDetails" as sgud
//                    on sss."generalUserDetail_id" = sgud.id
//                    left join "ClockIns" as cin
//                    on sss.id = cin."schoolStaff_id"
//                    where
//                    sss."school_id"=:schoolId
//                    and SAS."supervisionDate"=:date
//                    and cin."clockInDate"=:date
//                    """)
//    List<StaffAttendanceSupervisionProjection> findAllBySchoolDate(@Param("schoolId") String schoolId, @Param("date") LocalDate date);

}
