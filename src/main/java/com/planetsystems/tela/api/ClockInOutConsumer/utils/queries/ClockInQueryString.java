package com.planetsystems.tela.api.ClockInOutConsumer.utils.queries;

public interface ClockInQueryString {

    String INSERT_CLOCKIN_QUERY = """
                INSERT INTO "ClockIns"   (status , "clockInDate" , "clockInTime" , "clockedStatus" , "comment" , latitude ,
                longitude , "academicTerm_id" , school_id , "schoolStaff_id" , "clockinType" , displacement)
                VALUES (:status ,:clockInDate ,:clockInTime ,:clockedStatus ,:comment ,:latitude ,:longitude ,:academicTerm_id , :school_id ,:schoolStaff_id ,:clockinType ,:displacement)
                """;

    String AllClockInsBetween = """
            SELECT C.id AS clockInId , C."academicTerm_id" AS termId , C.school_id AS schoolId , C."schoolStaff_id" AS staffId  ,
                   C."clockInDate" , C."clockInTime" , C.longitude , C.latitude , C.comment , C."clockinType" , C.displacement
            FROM "ClockIns" AS C
            WHERE C.status <> 8 AND  C."clockInDate" BETWEEN ?::DATE and ?::DATE
            """;

    String AllClockInByDate = """
            SELECT C.id AS clockInId , C."academicTerm_id" AS termId , C.school_id AS schoolId , C."schoolStaff_id" AS staffId  ,
                   C."clockInDate" , C."clockInTime" , C.longitude , C.latitude , C.comment , C."clockinType" , C.displacement
            FROM "ClockIns" AS C
            WHERE C.status <> 8 AND  C."clockInDate" = ?::DATE
            """;


    String AllClockInsWithTermBetween = """
            SELECT C.id AS clockInId , C."clockInDate" , C."clockInTime" , C.longitude , C.latitude , C.comment , C."clockinType" AS clockInType , C.displacement,
                   C."academicTerm_id" AS termId , T.term , T."endDate" AS tEndDate , T."startDate" AS tStartDate ,T."academicYear_id" AS yearId ,
                   C.school_id AS schoolId , C."schoolStaff_id" AS staffId
            FROM "ClockIns" AS C
                     INNER JOIN "AcademicTerms" T on T.id = C."academicTerm_id"
            WHERE C.status <> 8 AND T.status <> 8 AND C."clockInDate" BETWEEN ?::DATE and ?::DATE
            """;


    String AllClockInsWithTermByDate = """
            SELECT C.id AS clockInId , C."clockInDate" , C."clockInTime" , C.longitude , C.latitude , C.comment , C."clockinType" AS clockInType , C.displacement,
                   C."academicTerm_id" AS termId , T.term , T."endDate" AS tEndDate , T."startDate" AS tStartDate ,T."academicYear_id" AS yearId ,
                   C.school_id AS schoolId , C."schoolStaff_id" AS staffId
            FROM "ClockIns" AS C
                     INNER JOIN "AcademicTerms" T on T.id = C."academicTerm_id"
            WHERE C.status <> 8 AND T.status <> 8 AND C."clockInDate" = ?::DATE
            """;

    String AllClockInsWithTerm_SchoolBetween = """
            SELECT C.id AS clockInId , C."clockInDate" , C."clockInTime" , C.longitude , C.latitude , C.comment , C."clockinType" , C.displacement,C."schoolStaff_id" AS staffId,
                   C."academicTerm_id" AS termId , T.term , T."endDate" AS tEndDate , T."startDate" AS tStartDate ,T."academicYear_id" AS yearId ,
                   C.school_id AS schoolId , S.name AS schoolName , S."schoolOwnership" , S."schoolLevel" , S."schoolType" , S."deviceNumber" , S."telaSchoolNumber" , S.district_id AS districtId
                 
            FROM "ClockIns" AS C
                     INNER JOIN "AcademicTerms" T on T.id = C."academicTerm_id"
                     INNER JOIN "Schools" S on S.id = C.school_id
            WHERE C.status <> 8 AND T.status <> 8 AND S.status <> 8
              AND C."clockInDate" BETWEEN ?::DATE and ?::DATE
            """;


    String AllClockInsWithTerm_SchoolByDate = """
            SELECT C.id AS clockInId , C."clockInDate" , C."clockInTime" , C.longitude , C.latitude , C.comment , C."clockinType" , C.displacement,C."schoolStaff_id" AS staffId,
                   C."academicTerm_id" AS termId , T.term , T."endDate" AS tEndDate , T."startDate" AS tStartDate ,T."academicYear_id" AS yearId ,
                   C.school_id AS schoolId , S.name AS schoolName , S."schoolOwnership" , S."schoolLevel" , S."schoolType" , S."deviceNumber" , S."telaSchoolNumber" , S.district_id AS districtId
                 
            FROM "ClockIns" AS C
                     INNER JOIN "AcademicTerms" T on T.id = C."academicTerm_id"
                     INNER JOIN "Schools" S on S.id = C.school_id
            WHERE C.status <> 8 AND T.status <> 8 AND S.status <> 8
              AND C."clockInDate" = ?::DATE
            """;


    String AllClockInsWithTerm_School_StaffBetween = """
            SELECT C.id AS clockInId , C."clockInDate" , C."clockInTime" , C.longitude , C.latitude , C.comment , C."clockinType" AS clockInType , C.displacement,
                   C."academicTerm_id" AS termId , T.term , T."endDate" AS tEndDate , T."startDate" AS tStartDate ,T."academicYear_id" AS yearId ,
                   C.school_id AS schoolId , S.name AS schoolName , S."schoolOwnership" , S."schoolLevel" , S."schoolType" , S."deviceNumber" , S."telaSchoolNumber" , S.district_id AS districtId,
                   C."schoolStaff_id" AS staffId , ST."staffType" , ST.registered , ST."expectedHours" , ST."expectedDays" , G."phoneNumber" , G.email , G."lastName" , G."firstName" , G.gender
            FROM "ClockIns" AS C
                     INNER JOIN "AcademicTerms" T on T.id = C."academicTerm_id"
                     INNER JOIN "Schools" S on S.id = C.school_id
            INNER JOIN "SchoolStaffs" ST on ST.id = C."schoolStaff_id"
            INNER JOIN "GeneralUserDetails" G on G.id = ST."generalUserDetail_id"
            WHERE C.status <> 8 AND T.status <> 8 AND S.status <> 8 AND ST.status <> 8 AND G.status <> 8
              AND C."clockInDate" BETWEEN ?::DATE and ?::DATE
            """;

    String AllClockInsWithTerm_School_StaffByDate = """
            SELECT C.id AS clockInId , C."clockInDate" , C."clockInTime" , C.longitude , C.latitude , C.comment , C."clockinType" AS clockInType , C.displacement,
                   C."academicTerm_id" AS termId , T.term , T."endDate" AS tEndDate , T."startDate" AS tStartDate ,T."academicYear_id" AS yearId ,
                   C.school_id AS schoolId , S.name AS schoolName , S."schoolOwnership" , S."schoolLevel" , S."schoolType" , S."deviceNumber" , S."telaSchoolNumber" , S.district_id AS districtId,
                   C."schoolStaff_id" AS staffId , ST."staffType" , ST.registered , ST."expectedHours" , ST."expectedDays" , G."phoneNumber" , G.email , G."lastName" , G."firstName" , G.gender
            FROM "ClockIns" AS C
                     INNER JOIN "AcademicTerms" T on T.id = C."academicTerm_id"
                     INNER JOIN "Schools" S on S.id = C.school_id
            INNER JOIN "SchoolStaffs" ST on ST.id = C."schoolStaff_id"
            INNER JOIN "GeneralUserDetails" G on G.id = ST."generalUserDetail_id"
            WHERE C.status <> 8 AND T.status <> 8 AND S.status <> 8 AND ST.status <> 8 AND G.status <> 8
              AND C."clockInDate" = ?::DATE
            """;


}
