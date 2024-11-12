package com.planetsystems.tela.api.ClockInOutConsumer.service.cache;


import com.planetsystems.tela.api.ClockInOutConsumer.Repository.*;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.ClockInProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.IdProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.*;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.supervision.StaffDailyAttendanceTaskSupervisionDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.ClassTimetableDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.StaffDailyTimetableDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.TimeTableLessonDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.TimetableDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.exception.TelaNotFoundException;
import com.planetsystems.tela.api.ClockInOutConsumer.model.*;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.LearnerType;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.SchoolLevel;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.SubjectClassification;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.Convertor;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.TelaDatePattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheServiceImpl implements CacheService{

    final AcademicTermRepository academicTermRepository;
    final SchoolRepository schoolRepository;
    final SchoolClassRepository schoolClassRepository;
    final SchoolStaffRepository schoolStaffRepository;
    final ClockInRepository clockInRepository;
    final SubjectRepository subjectRepository;
    final LearnerEnrollmentRepository learnerEnrollmentRepository;
    final SNLearnerEnrollmentRepository snLearnerEnrollmentRepository;
    final TimeTableRepository timeTableRepository;
    final TimeTableLessonRepository timeTableLessonRepository;
    final StaffDailyTimeTableRepository staffDailyTimeTableRepository;
    final StaffDailyTimeTableLessonRepository staffDailyTimeTableLessonRepository;
    final LearnerAttendanceRepository learnerAttendanceRepository;
    final SNLearnerAttendanceRepository snLearnerAttendanceRepository;
    final DistrictRepository districtRepository;
    final ClockOutRepository clockOutRepository;
    final StaffDailyAttendanceSupervisionRepository staffDailyAttendanceSupervisionRepository;
    final StaffDailyAttendanceTaskSupervisionRepository staffDailyAttendanceTaskSupervisionRepository;


    @Override
    @Cacheable(value = CacheKeys.ACTIVE_ACADEMIC_TERM , cacheManager = "monthCacheManager")
    public AcademicTermDTO cacheActiveAcademicTerm() {
        AcademicTerm academicTerm = academicTermRepository.activeAcademicTerm(Status.ACTIVE).orElseThrow(() -> new TelaNotFoundException("Active term not found"));
        AcademicTermDTO academicTermDTO = AcademicTermDTO.builder()
                .id(academicTerm.getId())
                .name(academicTerm.getTerm())
                .year(academicTerm.getAcademicYear().getName())
                .startDate(academicTerm.getStartDate().format(TelaDatePattern.datePattern))
                .endDate(academicTerm.getEndDate().format(TelaDatePattern.datePattern))
                .build();
        return academicTermDTO;
    }


    @Override
    @Cacheable(value = CacheKeys.SCHOOL , key = "#telaSchoolNumber")
    public SchoolDTO cacheSchoolData(String telaSchoolNumber) {
       try {
           School school = schoolRepository.byTelaNumberOrDeviceNumber(Status.DELETED , telaSchoolNumber , telaSchoolNumber).orElseThrow(() -> new TelaNotFoundException("School not found"));

           AcademicTermDTO academicTermDTO = cacheActiveAcademicTerm();

           SchoolDTO schoolDTO = SchoolDTO.builder()
                   .id(school.getId())
                   .academicTerm(academicTermDTO)
                   .district(new IdNameDTO(school.getDistrict().getId(), school.getDistrict().getName()))
                   .name(school.getName())
                   .telaSchoolNumber(school.getTelaSchoolUID())
                   .phoneNumber(school.getDeviceNumber())
                   .schoolLevel(school.getSchoolLevel().getLevel())
                   .schoolOwnership(school.getSchoolOwnership().getSchoolOwnership())
                   .licensed(school.getLicensed() != null ? school.getLicensed() : false)
                   .build();


           Optional<SchoolGeoCoordinate> optionalSchoolGeoCoordinate = school.getSchoolGeoCoordinateList().parallelStream().findFirst();

           if (optionalSchoolGeoCoordinate.isPresent()) {
               SchoolGeoCoordinate schoolGeoCoordinate = optionalSchoolGeoCoordinate.get();
               schoolDTO.setGeoCoordinate(GeoCoordinateDTO.builder()
                       .id(schoolGeoCoordinate.getId())
                       .pinClockActivated(schoolGeoCoordinate.isPinClockActivated())
                       .maxDisplacement(schoolGeoCoordinate.getDisplacement())
                       .geoFenceActivated(schoolGeoCoordinate.isGeoFenceActivated())
                       .longitude(schoolGeoCoordinate.getLongtitude())
                       .latitude(schoolGeoCoordinate.getLatitude())
                       .build());
           }


           return schoolDTO;
       }catch (Exception e){
           e.printStackTrace();
       }
       return null;
    }

    @Override
    @Cacheable(value = CacheKeys.CLASSES , key = "{'school='+#schoolDTO.telaSchoolNumber+',term='+#schoolDTO.academicTerm.id}")
    public MQResponseDto<List<ClassDTO>> cacheSchoolClasses(SchoolDTO schoolDTO) {
            List<ClassDTO> classDTOS = schoolClassRepository
                    .findAllByStatusNotAndAcademicTerm_IdAndSchool_Id(Status.DELETED, schoolDTO.getAcademicTerm().getId(), schoolDTO.getId())
                    .parallelStream().map(schoolClass -> {
                        ClassDTO dto = new ClassDTO();
                        dto.setName(schoolClass.getName());
                        dto.setId(schoolClass.getId());
                        if (schoolClass.getParentSchoolClass() != null) {
                            dto.setParentSchoolClassId(schoolClass.getParentSchoolClass().getId());
                        } else {
                            dto.setParentSchoolClassId(null);
                        }

                        return dto;
                    }).sorted(Comparator.comparing(ClassDTO::getName))
                    .collect(Collectors.toList());

            if (classDTOS.isEmpty()) {
                List<SchoolClass> defaultClasses = this.generateDefaultClasses(SchoolLevel.getSchoolLevel(schoolDTO.getSchoolLevel()))
                        .stream().map(schoolClass -> {
                            schoolClass.setCreatedDateTime(LocalDateTime.now());
                            schoolClass.setUpdatedDateTime(LocalDateTime.now());
                            schoolClass.setAcademicTerm(new AcademicTerm(schoolDTO.getAcademicTerm().getId()));
                            schoolClass.setSchool(new School(schoolDTO.getId()));
                            schoolClass.setStatus(Status.ACTIVE);
                            return schoolClass;
                        }).collect(Collectors.toList());

                // create new
                schoolClassRepository.saveAll(defaultClasses);

                classDTOS = schoolClassRepository
                        .findAllByStatusNotAndAcademicTerm_IdAndSchool_Id(Status.DELETED, schoolDTO.getAcademicTerm().getId(), schoolDTO.getId())
                        .parallelStream().map(schoolClass -> {
                            ClassDTO dto = new ClassDTO();
                            dto.setName(schoolClass.getName());
                            dto.setId(schoolClass.getId());
                            if (schoolClass.getParentSchoolClass() != null) {
                                dto.setParentSchoolClassId(schoolClass.getParentSchoolClass().getId());
                            } else {
                                dto.setParentSchoolClassId(null);
                            }

                            return dto;
                        }).sorted(Comparator.comparing(ClassDTO::getName))
                        .collect(Collectors.toList());

            }

            MQResponseDto<List<ClassDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.CLASSES);
            responseDto.setData(classDTOS);
            return responseDto;
    }

    private List<SchoolClass> generateDefaultClasses(SchoolLevel level) {
        IntStream priStream = IntStream.rangeClosed(1, 7);
        IntStream secStream = IntStream.rangeClosed(1, 6);
        IntStream caStream = IntStream.rangeClosed(1, 2);

        switch (level) {
            case PRIMARY:
                List<SchoolClass> pri = priStream.mapToObj(i -> {
                    SchoolClass dto = new SchoolClass();
                    String name = "P." + i;
                    dto.setName(name);
                    dto.setCode(name);
                    dto.setClassLevel(true);
                    dto.setHasStreams(false);

                    return dto;
                }).collect(Collectors.toList());

                return pri;

            case SECONDARY:
                List<SchoolClass> sec = secStream.mapToObj(i -> {
                    SchoolClass dto = new SchoolClass();
                    String name = "S." + i;
                    dto.setCode(name);
                    dto.setName(name);
                    dto.setClassLevel(true);
                    dto.setHasStreams(false);
                    return dto;
                }).collect(Collectors.toList());

                return sec;


            case CAI:
                List<SchoolClass> collect = caStream.mapToObj(i -> {
                    SchoolClass dto = new SchoolClass();
                    String name = "Year " + i;
                    dto.setCode(name);
                    dto.setName(name);
                    dto.setClassLevel(true);
                    dto.setHasStreams(false);
                    return dto;
                }).collect(Collectors.toList());

                return collect;


            default:
                System.out.println("Unknown School Level");
                return Collections.emptyList();
        }

    }

    @Override
    @Cacheable(value = CacheKeys.STAFFS , key = "#schoolDTO.telaSchoolNumber")
    public MQResponseDto<List<StaffDTO>> cacheSchoolStaffs(SchoolDTO schoolDTO) {
        List<StaffDTO> staffDTOList = schoolStaffRepository.findAllBySchoolWithSchool_StaffDetail(Status.DELETED, schoolDTO.getId())
                .parallelStream()
                .map(schoolStaff -> {
                    StaffDTO staffDTO = new StaffDTO();
                    staffDTO.setId(schoolStaff.getId());
                    staffDTO.setOnPayRoll(schoolStaff.isRegistered() ? "Yes" : "No");


                    GeneralUserDetail generalUserDetail = schoolStaff.getGeneralUserDetail();
                    if (generalUserDetail != null) {


                        if (generalUserDetail.getFirstName() != null) {
                            staffDTO.setFirstName(generalUserDetail.getFirstName());
                        }

                        if (generalUserDetail.getLastName() != null) {
                            staffDTO.setLastName(generalUserDetail.getLastName());
                        }


                        if (generalUserDetail.getDob() != null) {
                            staffDTO.setDob(generalUserDetail.getDob().format(TelaDatePattern.datePattern));
                        } else {
                            staffDTO.setDob("");
                        }

                        if (generalUserDetail.getEmail() != null) {
                            staffDTO.setEmailAddress(generalUserDetail.getEmail());
                        } else {
                            staffDTO.setEmailAddress("");
                        }

                        if (schoolStaff.getStaffCode() != null) {
                            staffDTO.setEmployeeNumber(schoolStaff.getStaffCode());
                        } else {
                            staffDTO.setEmployeeNumber("");
                        }


                        if (generalUserDetail.getGender() != null) {
                            staffDTO.setGender(generalUserDetail.getGender().getGender());
                        } else {
                            staffDTO.setGender("");
                        }

                        if (generalUserDetail.getNameAbbrev() != null) {
                            staffDTO.setInitials(generalUserDetail.getNameAbbrev());
                        } else {
                            staffDTO.setInitials("");
                        }


                        staffDTO.setLicensed(schoolStaff.isRegistered());

                        if (generalUserDetail.getNationalId() != null) {
                            staffDTO.setNationalId(generalUserDetail.getNationalId());
                        } else {
                            staffDTO.setNationalId("");
                        }

                        if (generalUserDetail.getPhoneNumber() != null) {
                            staffDTO.setPhoneNumber(generalUserDetail.getPhoneNumber());
                        } else {
                            staffDTO.setPhoneNumber(schoolStaff.getStaffCode());
                        }

                        if (schoolStaff.getStaffType() != null) {
                            staffDTO.setRole(schoolStaff.getStaffType().getStaffType());
                        } else {
                            staffDTO.setRole("");
                        }

                        staffDTO.setHasSpecialNeeds((schoolStaff.getSpecialNeeds() == null || schoolStaff.getSpecialNeeds()) ? "true" : "false");
                        staffDTO.setStaffType(schoolStaff.isTeachingstaff() ? "Teaching" : "Non-Teaching");

                        staffDTO.setExpectedHours(schoolStaff.getExpectedHours() > 8 ? 8 : schoolStaff.getExpectedHours());
                    }

                    return staffDTO;
                })
                .sorted(Comparator.comparing(StaffDTO::getFirstName))
                .toList();
        MQResponseDto<List<StaffDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.STAFFS);
        responseDto.setData(staffDTOList);

        return responseDto;
    }

    @Override
    @Cacheable(value = CacheKeys.CLOCKINS , key = "{'school='+#schoolDTO.id+',term='+#schoolDTO.academicTerm.id}")
    public MQResponseDto<List<ClockInDTO>> cacheSchoolTermClockIns(SchoolDTO schoolDTO) {
        List<ClockInProjection> schoolDateClockIns = clockInRepository.nativeAllByTerm_School(schoolDTO.getAcademicTerm().getId(), schoolDTO.getId());


        System.out.println("schoolDateClockIns " + schoolDateClockIns.size());
        List<ClockInDTO> clockInDTOS = schoolDateClockIns.parallelStream().map(clockIn -> {

                    LocalDateTime clockInDateTime = LocalDateTime.of(clockIn.getClockInDate(), clockIn.getClockInTime());

                    ClockInDTO clockInDTO = ClockInDTO.builder()
                            .id(clockIn.getId())
                            .displacement(clockIn.getDisplacement())
                            .clockInDateTime(clockInDateTime.format(TelaDatePattern.dateTimePattern24))
                            .clockInType(clockIn.getClockinType())
                            .staffId(clockIn.getStaffId())
                            .academicTermId(schoolDTO.getAcademicTerm().getId())
                            .longitude(clockIn.getLongitude())
                            .latitude(clockIn.getLatitude())
                            .telaSchoolNumber(schoolDTO.getTelaSchoolNumber())
                            .build();

                    return clockInDTO;
                })
                .sorted(Comparator.comparing(ClockInDTO::getClockInDateTime))
                .toList();

        MQResponseDto<List<ClockInDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.CLOCKINS);
        responseDto.setData(clockInDTOS);
        return responseDto;
    }

    @Override
    @Cacheable(value = CacheKeys.CLOCKOUTS , key = "{'school='+#schoolDTO.id+',term='+#schoolDTO.academicTerm.id}")
    public MQResponseDto<List<ClockOutDTO>> cacheSchoolTermClockOuts(SchoolDTO schoolDTO) {
        List<ClockOut> schoolClockOuts  = clockOutRepository.allByTerm_SchoolWithStaff(schoolDTO.getAcademicTerm().getId(), schoolDTO.getId());
        List<ClockOutDTO> clockOutDTOS = schoolClockOuts.parallelStream().map(clockOut -> {

                    LocalDateTime clockOutDateTime = LocalDateTime.of(clockOut.getClockOutDate(), clockOut.getClockOutTime());

                    ClockIn clockIn = clockOut.getClockIn();
                    ClockOutDTO clockOutDTO = ClockOutDTO.builder()
                            .clockInId(clockIn.getId())
                            .telaSchoolNumber(schoolDTO.getTelaSchoolNumber())
                            .academicTermId(schoolDTO.getAcademicTerm().getId())
                            .staffId(clockIn.getSchoolStaff().getId())
                            .id(clockOut.getId())
                            .clockOutType(clockOut.getClockOutType())
                            .clockOutDateTime(clockOutDateTime.format(TelaDatePattern.dateTimePattern24))
                            .reason(clockOut.getComment())
                            .latitude(clockIn.getLatitude())
                            .longitude(clockIn.getLongitude())
                            .comment(clockOut.getComment())
                            .displacement(clockOut.getDisplacement())
                            .build();

                    return clockOutDTO;
                })
                .sorted(Comparator.comparing(ClockOutDTO::getClockOutDateTime))
                .toList();

        MQResponseDto<List<ClockOutDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.CLOCKOUTS);
        responseDto.setData(clockOutDTOS);

        return responseDto;
    }

    @Override
    @Cacheable(value = CacheKeys.SUBJECTS)
    public MQResponseDto<List<IdNameCodeDTO>> cacheSubjects(SchoolDTO schoolDTO) {

        SchoolLevel schoolLevel = SchoolLevel.getSchoolLevel(schoolDTO.getSchoolLevel());
        SubjectClassification subjectClassification = SubjectClassification.getSubjectClassification(schoolLevel.getLevel());
        List<IdNameCodeDTO> subjectDTOS = subjectRepository.findAllBySubjectClassificationNotNullAndStatusNotAndSubjectClassification(Status.DELETED, subjectClassification)
                .parallelStream().map(subject -> new IdNameCodeDTO(subject.getId(), subject.getName(), subject.getCode()))
                .sorted(Comparator.comparing(IdNameCodeDTO::code))
                .toList();

        MQResponseDto<List<IdNameCodeDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.SUBJECTS);
        responseDto.setData(subjectDTOS);
        return responseDto;
    }

    @Override
    @Cacheable(value = CacheKeys.LEARNER_HEADCOUNTS , key = "{'school='+#schoolDTO.id+',term='+#schoolDTO.academicTerm.id}")
    public MQResponseDto<List<LearnerHeadCountDTO>> cacheLearnerEnrollments(SchoolDTO schoolDTO) {
        List<LearnerHeadCountDTO> generalLearnerHeadCountDTOS = learnerEnrollmentRepository.allBySchool_term(schoolDTO.getId(), schoolDTO.getAcademicTerm().getId()).parallelStream()
                .map(enrollment -> {
                    LearnerHeadCountDTO learnerHeadCountDTO = LearnerHeadCountDTO.builder()
                            .staffId(enrollment.getSchoolStaff() != null ? enrollment.getSchoolStaff().getId() : "")
                            .classId(enrollment.getSchoolClass().getId())
                            .learnerType(LearnerType.GENERAL.getType())
                            .totalFemale(enrollment.getTotalGirls())
                            .totalMale(enrollment.getTotalBoys())
                            .submissionDate(enrollment.getSubmissionDate().format(TelaDatePattern.datePattern))
                            .build();

                    return learnerHeadCountDTO;
                }).collect(Collectors.toList());

        List<LearnerHeadCountDTO> snLearnerHeadCountDTOS = snLearnerEnrollmentRepository.allBySchool_term(schoolDTO.getId(), schoolDTO.getAcademicTerm().getId()).parallelStream()
                .map(enrollment -> {
                    LearnerHeadCountDTO learnerHeadCountDTO = LearnerHeadCountDTO.builder()
                            .staffId(enrollment.getSchoolStaff() != null ? enrollment.getSchoolStaff().getId() : "")
                            .classId(enrollment.getSchoolClass().getId())
                            .learnerType(LearnerType.SPECIAL_NEEDS.getType())
                            .totalFemale(enrollment.getTotalGirls())
                            .totalMale(enrollment.getTotalBoys())
                            .submissionDate(enrollment.getSubmissionDate().format(TelaDatePattern.datePattern))
                            .build();

                    return learnerHeadCountDTO;
                }).toList();
        generalLearnerHeadCountDTOS.addAll(snLearnerHeadCountDTOS);

        MQResponseDto<List<LearnerHeadCountDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.LEARNER_HEADCOUNTS);
        responseDto.setData(generalLearnerHeadCountDTOS);
        return responseDto;
    }

    @Override
    @Cacheable(value = CacheKeys.LEARNER_ATTENDANCES , key = "{'school='+#schoolDTO.id+',term='+#schoolDTO.academicTerm.id}")
    public  MQResponseDto<List<LearnerAttendanceDTO>> cacheLearnerAttendance(SchoolDTO schoolDTO) {
        log.info("publishLearnerAttendance");
        List<LearnerAttendance> learnerAttendanceList = learnerAttendanceRepository.allByTerm_School(schoolDTO.getAcademicTerm().getId(), schoolDTO.getId());
        List<SNLearnerAttendance> snLearnerAttendanceList = snLearnerAttendanceRepository.allByTerm_School(schoolDTO.getAcademicTerm().getId(), schoolDTO.getId());

        List<LearnerAttendanceDTO> generalLearnerAttendanceDTOS = learnerAttendanceList.parallelStream().map(learnerAttendance -> {
                    LearnerAttendanceDTO learnerAttendanceDTO = LearnerAttendanceDTO.builder()
                            .staffId(learnerAttendance.getSchoolStaff().getId())
                            .classId(learnerAttendance.getSchoolClass().getId())
                            .comment(learnerAttendance.getComment())
                            .maleAbsent(learnerAttendance.getBoysAbsent())
                            .femaleAbsent(learnerAttendance.getGirlsAbsent())
                            .malePresent(learnerAttendance.getBoysPresent())
                            .femalePresent(learnerAttendance.getGirlsPresent())
                            .learnerType(LearnerType.GENERAL.getType())
                            .submissionDate(learnerAttendance.getAttendanceDate().format(TelaDatePattern.datePattern))
                            .build();
                    return learnerAttendanceDTO;
                }).sorted(Comparator.comparing(LearnerAttendanceDTO::getSubmissionDate))
                .collect(Collectors.toList());

        List<LearnerAttendanceDTO> snLearnerAttendanceDTOS = snLearnerAttendanceList.parallelStream().map(learnerAttendance -> {
                    LearnerAttendanceDTO snLearnerAttendanceDTO = LearnerAttendanceDTO.builder()
                            .staffId(learnerAttendance.getSchoolStaff().getId())
                            .classId(learnerAttendance.getSchoolClass().getId())
                            .comment(learnerAttendance.getComment())
                            .maleAbsent(learnerAttendance.getBoysAbsent())
                            .femaleAbsent(learnerAttendance.getGirlsAbsent())
                            .malePresent(learnerAttendance.getBoysPresent())
                            .femalePresent(learnerAttendance.getGirlsPresent())
                            .learnerType(LearnerType.GENERAL.getType())
                            .submissionDate(learnerAttendance.getAttendanceDate().format(TelaDatePattern.datePattern))
                            .build();
                    return snLearnerAttendanceDTO;
                }).sorted(Comparator.comparing(LearnerAttendanceDTO::getSubmissionDate))
                .toList();

        generalLearnerAttendanceDTOS.addAll(snLearnerAttendanceDTOS);


        MQResponseDto<List<LearnerAttendanceDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.LEARNER_ATTENDANCES);
        responseDto.setData(generalLearnerAttendanceDTOS);

        return responseDto;

    }

    @Override
    @Cacheable(value = CacheKeys.STAFF_DAILY_TIME_ATTENDANCES , key = "{'school='+#schoolDTO.id+',term='+#schoolDTO.academicTerm.id}")
    public MQResponseDto<List<StaffDailyTimeAttendanceDTO>> cacheStaffDailyTimeAttendanceSupervision(SchoolDTO schoolDTO, String dateParam) {

        List<StaffDailyAttendanceSupervision> staffDailyAttendanceSupervisions = staffDailyAttendanceSupervisionRepository
                .allByTermDates_School(LocalDate.parse(schoolDTO.getAcademicTerm().getStartDate() , TelaDatePattern.datePattern), LocalDate.parse(schoolDTO.getAcademicTerm().getEndDate() , TelaDatePattern.datePattern)
                        , schoolDTO.getId());


        List<StaffDailyTimeAttendanceDTO> staffDailyTimeAttendanceDTOS = staffDailyAttendanceSupervisions.parallelStream().map(attendanceSupervision -> {

            LocalDateTime supervisionDateTime = LocalDateTime.of(attendanceSupervision.getSupervisionDate(), attendanceSupervision.getSupervisionTime());

            StaffDailyTimeAttendanceDTO staffDailyTimeAttendanceDTO = StaffDailyTimeAttendanceDTO.builder()
                    .id(attendanceSupervision.getId())
                    .staffId(attendanceSupervision.getSchoolStaff().getId())
                    .supervisorId(attendanceSupervision.getSupervisor().getId())
                    .supervisorComment(attendanceSupervision.getComment())
                    .supervisionStatus(attendanceSupervision.getAttendanceStatus().getAttendance())
                    .supervisionDateTime(supervisionDateTime.format(TelaDatePattern.dateTimePattern24))
                    .build();
            return staffDailyTimeAttendanceDTO;
        }).sorted(Comparator.comparing(StaffDailyTimeAttendanceDTO::getSupervisionDateTime)).toList();


        MQResponseDto<List<StaffDailyTimeAttendanceDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.STAFF_DAILY_TIME_ATTENDANCES);
        responseDto.setData(staffDailyTimeAttendanceDTOS);
        return  responseDto;
    }

    @Override
    @Cacheable(value = CacheKeys.STAFF_DAILY_TASK_SUPERVISIONS , key = "{'school='+#schoolDTO.id+',term='+#schoolDTO.academicTerm.id}")
    public MQResponseDto<List<StaffDailyAttendanceTaskSupervisionDTO>> cacheStaffDailyTimetableTaskSupervision(SchoolDTO schoolDTO , String dateParam) {
        LocalDate startDate = LocalDate.parse(schoolDTO.getAcademicTerm().getStartDate(), TelaDatePattern.datePattern);
        LocalDate endDate = LocalDate.parse(schoolDTO.getAcademicTerm().getEndDate(), TelaDatePattern.datePattern);


        List<StaffDailyAttendanceSupervision> termStaffDailyAttendanceSupervisions = staffDailyAttendanceSupervisionRepository
                .allByTermDates_School(startDate, endDate, schoolDTO.getId());
        List<StaffDailyAttendanceTaskSupervision> termStaffDailyAttendanceTaskSupervisions = staffDailyAttendanceTaskSupervisionRepository.allIn(termStaffDailyAttendanceSupervisions);


        List<StaffDailyAttendanceTaskSupervisionDTO> staffDailyAttendanceTaskSupervisionDTOS = termStaffDailyAttendanceSupervisions.parallelStream().flatMap(attendanceSupervision -> termStaffDailyAttendanceTaskSupervisions.parallelStream()
                        .filter(taskSupervision -> attendanceSupervision.getId().equals(taskSupervision.getStaffDailyAttendanceSupervision().getId()))
                        .map(taskSupervision -> {

                            StaffDailyAttendanceTaskSupervisionDTO dto = StaffDailyAttendanceTaskSupervisionDTO.builder()
                                    .id(taskSupervision.getId())
                                    .timeAttendanceId(attendanceSupervision.getId())
                                    .lessonTaskId(taskSupervision.getStaffDailyTimeTableLesson().getId())
                                    .supervisorId(attendanceSupervision.getSupervisor().getId())
                                    .supervisionDate(attendanceSupervision.getSupervisionDate().format(TelaDatePattern.datePattern))
                                    .supervisionTime(attendanceSupervision.getSupervisionTime().format(TelaDatePattern.timePattern24))
                                    .timeStatus(taskSupervision.getTeachingTimeStatus().getSupervision())
                                    .supervisionComment(taskSupervision.getComment())
                                    .build();
                            return dto;
                        })
                ).sorted(Comparator.comparing(StaffDailyAttendanceTaskSupervisionDTO::getSupervisionDate))
                .toList();



        MQResponseDto<List<StaffDailyAttendanceTaskSupervisionDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.STAFF_DAILY_TASK_SUPERVISIONS);
        responseDto.setData(staffDailyAttendanceTaskSupervisionDTOS);
        log.info("STAFF_DAILY_TASK_SUPERVISION published for {} {} {} ", schoolDTO.getAcademicTerm().getName(), schoolDTO.getName(), staffDailyAttendanceTaskSupervisionDTOS.size());

        return responseDto;
    }

    @Override
    @Cacheable(value = CacheKeys.STAFF_DAILY_TIMETABLES , key = "{'school='+#schoolDTO.id+',term='+#schoolDTO.academicTerm.id}")
    public MQResponseDto<List<StaffDailyTimetableDTO>> cacheStaffDailyTimetables(SchoolDTO schoolDTO) {

        List<StaffDailyTimeTable> termStaffDailyTimeTables = staffDailyTimeTableRepository.allByTerm_School(schoolDTO.getAcademicTerm().getId(), schoolDTO.getId());
        List<StaffDailyTimeTableLesson> termStaffDailyTimeTableLessons = staffDailyTimeTableLessonRepository.allIn(termStaffDailyTimeTables);

        List<StaffDailyTimetableDTO> staffDailyTimetableDTOS = termStaffDailyTimeTables.parallelStream().flatMap(dailyTimeTable -> termStaffDailyTimeTableLessons.parallelStream()
                .filter(dailyLesson -> dailyTimeTable.getId().equals(dailyLesson.getStaffDailyTimeTable().getId()))
                .map(dailyLesson -> {
                    StaffDailyTimetableDTO dto = StaffDailyTimetableDTO.builder()
                            .id(dailyLesson.getId())
                            .timeAttendanceId(dailyTimeTable.getId())
                            .subjectId(dailyLesson.getSubject().getId())
                            .classId(dailyLesson.getSchoolClass().getId())
                            .startTime(dailyLesson.getStartTime().format(TelaDatePattern.timePattern24))
                            .endTime(dailyLesson.getEndTime().format(TelaDatePattern.timePattern24))
                            .actionStatus(dailyLesson.getDailyTimeTableLessonStatus().getAttendance())
                            .submissionDate(dailyLesson.getLessonDate().format(TelaDatePattern.datePattern))
                            .staffId(dailyTimeTable.getSchoolStaff().getId())
                            .comment(dailyTimeTable.getComment())
                            .build();
                    return dto;
                })
        ).sorted(Comparator.comparing(StaffDailyTimetableDTO::getStaffId)).toList();


        MQResponseDto<List<StaffDailyTimetableDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.STAFF_DAILY_TIMETABLES);
        responseDto.setData(staffDailyTimetableDTOS);

        return responseDto;

    }

    @Override
    @Cacheable(value = CacheKeys.DISTRICTS)
    public MQResponseDto<List<DistrictDTO>> cacheDistricts() {
        List<DistrictDTO> districtDTOS = districtRepository.findAllByStatusNot(Status.DELETED)
                .parallelStream()
                .map(district -> new DistrictDTO(district.getId(), district.getName(), district.getRegion().getName()))
                .sorted(Comparator.comparing(DistrictDTO::name))
                .toList();

        MQResponseDto<List<DistrictDTO>> responseDto = new MQResponseDto<>();
        responseDto.setResponseType(ResponseType.DISTRICTS);
        responseDto.setData(districtDTOS);

        return responseDto;
    }

    @Override
    @Cacheable(value = CacheKeys.SCHOOL_TIMETABLE , key = "{'school='+#schoolDTO.id+',term='+#schoolDTO.academicTerm.id}")
    public MQResponseDto<TimetableDTO> cacheSchoolTimetables(SchoolDTO schoolDTO) {
        Optional<IdProjection> idProjectionOptional = timeTableRepository.findBySchool_IdAndAcademicTerm_Id(schoolDTO.getId(), schoolDTO.getAcademicTerm().getId());

//                .orElseThrow(() -> new TelaNotFoundException(school.getName() + "Timetable not found from " + academicTerm.getTerm()));

        if (idProjectionOptional.isPresent()) {
            IdProjection idProjection = idProjectionOptional.get();

            TimetableDTO timetableDTO = TimetableDTO.builder()
                    .schoolId(schoolDTO.getId())
                    .academicTermId(schoolDTO.getAcademicTerm().getId())
                    .id(idProjection.getId())
                    .build();

            Map<String, List<TimeTableLesson>> classLessonMap = timeTableLessonRepository.allByTimetable(idProjection.getId()).parallelStream()
                    .filter(Convertor.distinctByKey(timeTableLesson -> timeTableLesson.getStartTime() + "" + timeTableLesson.getEndTime() + "" + timeTableLesson.getLessonDay() + "" + timeTableLesson.getSubject().getId()))
                    .collect(Collectors.groupingBy(timeTableLesson -> timeTableLesson.getSchoolClass().getId()));

            List<ClassTimetableDTO> classTimetableDTOS = classLessonMap.keySet().parallelStream().map(classId -> {
                List<TimeTableLesson> classLessons = classLessonMap.get(classId);
                Optional<TimeTableLesson> firstOptional = classLessons.parallelStream().findFirst();

                ClassTimetableDTO classDTO = ClassTimetableDTO.builder()
                        .classId(classId)
                        .build();

                if (firstOptional.isPresent()) {
                    TimeTableLesson lesson = firstOptional.get();
                    classDTO.setClassEndTime(lesson.getClassEndTime() != null ? lesson.getClassEndTime().format(TelaDatePattern.timePattern24) : "");
                    classDTO.setClassStartTime(lesson.getClassStartTime() != null ? lesson.getClassStartTime().format(TelaDatePattern.timePattern24) : "");
                    classDTO.setDuration(lesson.getDuration() != null ? lesson.getDuration() : 0);
                    classDTO.setBreakStartTime(lesson.getBreakStartTime() != null ? lesson.getBreakStartTime().format(TelaDatePattern.timePattern24) : "");
                    classDTO.setBreakEndTime(lesson.getBreakStartTime() != null ? lesson.getBreakStartTime().format(TelaDatePattern.timePattern24) : "");
                    classDTO.setLunchEndTime(lesson.getBreakStartTime() != null ? lesson.getBreakStartTime().format(TelaDatePattern.timePattern24) : "");
                    classDTO.setLunchStartTime(lesson.getLunchStartTime() != null ? lesson.getLunchStartTime().format(TelaDatePattern.timePattern24) : "");
                }

                List<TimeTableLessonDTO> lessonDTOS = classLessons.parallelStream().map(tLesson -> {
                    TimeTableLessonDTO lessonDTO = TimeTableLessonDTO.builder()
                            .endTime(tLesson.getEndTime() != null ? tLesson.getEndTime().format(TelaDatePattern.timePattern24) : "")
                            .startTime(tLesson.getStartTime() != null ? tLesson.getStartTime().format(TelaDatePattern.timePattern24) : "")
                            .lessonDay(tLesson.getLessonDay().getDay())
                            .id(tLesson.getId())
                            .subjectId(tLesson.getSubject().getId())
                            .staffId(tLesson.getSchoolStaff().getId())
                            .build();
                    return lessonDTO;
                }).toList();

                classDTO.setLessons(lessonDTOS);

                return classDTO;
            }).toList();

            timetableDTO.setClassTimetables(classTimetableDTOS);

            MQResponseDto<TimetableDTO> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.SCHOOL_TIMETABLE);
            responseDto.setData(timetableDTO);

            return responseDto;
        } else {
            MQResponseDto<TimetableDTO> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.SCHOOL_TIMETABLE);
            responseDto.setData(new TimetableDTO());

            return responseDto;

        }

    }
}
