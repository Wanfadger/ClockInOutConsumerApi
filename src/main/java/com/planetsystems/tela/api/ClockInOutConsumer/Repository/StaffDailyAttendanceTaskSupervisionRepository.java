package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.StaffDailyAttendanceSupervision;
import com.planetsystems.tela.api.ClockInOutConsumer.model.StaffDailyAttendanceTaskSupervision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StaffDailyAttendanceTaskSupervisionRepository extends JpaRepository<StaffDailyAttendanceTaskSupervision, String> {

    @Query("""
            SELECT SDATS FROM StaffDailyAttendanceTaskSupervision AS SDATS
            WHERE SDATS.status <> 8 AND SDATS.staffDailyAttendanceSupervision IN :termStaffDailyAttendanceSupervisions
            """)
    List<StaffDailyAttendanceTaskSupervision> allIn(List<StaffDailyAttendanceSupervision> termStaffDailyAttendanceSupervisions);


//    List<StaffDailyAttendanceTaskSupervision> findAllByStatusNot(Status status);
//
//    List<StaffDailyAttendanceTaskSupervision> findAllByStatus(Status status);
//
//    Optional<StaffDailyAttendanceTaskSupervision> findByStatusNotAndId(Status status, String id);
//
//
//    Optional<StaffDailyAttendanceTaskSupervision> findByStatusNotAndStaffDailyTimeTableLesson_IdAndStaffDailyAttendanceSupervision_Id(Status status, String staffDailyTimeTableLessonId, String staffDailyAttendanceSupervisionId);
//
//    @Query(nativeQuery = true,
//            value = "SELECT * from \"StaffDailyAttendanceTaskSupervisions\" AS SDT\n" +
//                    "INNER JOIN \"StaffDailyAttendanceSupervisions\" AS SD ON SD.id = SDT.\"staffDailyAttendanceSupervision_id\"\n" +
//                    "WHERE SDT.status <>:#{#status.ordinal()} AND SD.status <>:#{#status.ordinal()}\n" +
//                    "AND SD.id IN :staffDailyAttendanceSupervisionId ")
//    List<StaffDailyAttendanceTaskSupervision> findAllStaffDailyAttendanceTaskSupervisionInStaffDailyAttendanceSupervision(Status status, List<String> staffDailyAttendanceSupervisionId);
//
//    @Query(nativeQuery = true, value = """
//            SELECT
//                scl.school_id AS schoolId,
//                scl.name AS schoolClass,
//                sjts.name AS subject,
//                sdtl."startTime",
//                sdtl."endTime",
//                sdtl."lessonDate",
//                CASE
//                    WHEN sdtl."dailyTimeTableLessonStatus" = 0 THEN 'Present'
//                    ELSE 'Absent'
//                    END AS staffTeachingStatus,
//                gud."firstName",
//                gud."lastName",
//                CASE sst."staffType"
//                    WHEN 0 THEN 'Teacher'
//                    WHEN 1 THEN 'Headteacher'
//                    WHEN 2 THEN 'Deputy headteacher'
//                    WHEN 3 THEN 'Smc'
//                    WHEN 4 THEN 'Principal'
//                    WHEN 5 THEN 'Bursar'
//                    WHEN 6 THEN 'Librarian'
//                    WHEN 7 THEN 'Lab technician'
//                    ELSE 'Others'
//                    END AS role,
//                CASE sdats."teachingStatus"
//                    WHEN 0 THEN 'Taught according to timetable'
//                    WHEN 1 THEN 'Not taught according to timetable'
//                    WHEN 2 THEN 'Taught beyond time'
//                    WHEN 3 THEN  'Taught out of time'
//                    ELSE 'Not supervised'
//                    END AS teachingStatus,
//                CASE sdats."teachingTimeStatus"
//                    WHEN 0 THEN 'Taught according to timetable'
//                    WHEN 1 THEN 'Not taught according to timetable'
//                    WHEN 2 THEN 'Taught beyond time'
//                    WHEN 3 THEN  'Taught out of time'
//                    ELSE 'Not supervised'
//                    END AS teachingTimeStatus,
//                sdats.comment
//            FROM "StaffDailyTimeTableLessons" AS sdtl
//                     INNER JOIN "Subjects" AS sjts ON sdtl.subject_id = sjts.id
//                     INNER JOIN "SchoolClasses" AS scl ON sdtl."schoolClass_id" = scl.id
//                     INNER JOIN "StaffDailyTimeTables" AS sdt ON sdtl."staffDailyTimeTable_id" = sdt.id
//                     INNER JOIN "SchoolStaffs" AS sst ON sdt."schoolStaff_id" = sst.id
//                     INNER JOIN "GeneralUserDetails" AS gud ON sst."generalUserDetail_id" = gud.id
//                     LEFT JOIN "StaffDailyAttendanceTaskSupervisions" AS sdats ON sdtl.id = sdats."staffDailyTimeTableLesson_id"
//            WHERE
//                sdtl."lessonDate" =:date
//              AND
//                scl.school_id=:schoolId
//            """)
//    List<DailyAttendanceTaskSupervisionProjection> findSchoolSupervisionsByDate(@Param("schoolId") String schoolId, @Param("date") LocalDate date);
}