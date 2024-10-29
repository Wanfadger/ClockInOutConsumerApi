package com.planetsystems.tela.api.ClockInOutConsumer.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.Convertor;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.TelaDatePattern;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SynchronizeMobileDataServiceImpl implements SynchronizeMobileDataService {
    private final SchoolRepository schoolRepository;
    private final AcademicTermRepository academicTermRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SchoolStaffRepository schoolStaffRepository;
    final ClockInRepository clockInRepository;
    final ClockOutRepository clockOutRepository;
    final SubjectRepository subjectRepository;
    final LearnerEnrollmentRepository learnerEnrollmentRepository;
    final SNLearnerEnrollmentRepository snLearnerEnrollmentRepository;
    final LearnerAttendanceRepository learnerAttendanceRepository;
    final SNLearnerAttendanceRepository snLearnerAttendanceRepository;
    final DistrictRepository districtRepository;
    final StaffDailyAttendanceSupervisionRepository staffDailyAttendanceSupervisionRepository;
    final StaffDailyAttendanceTaskSupervisionRepository staffDailyAttendanceTaskSupervisionRepository;
    final TimeTableRepository timeTableRepository;
    final TimeTableLessonRepository timeTableLessonRepository;
    final StaffDailyTimeTableRepository staffDailyTimeTableRepository;
    final StaffDailyTimeTableLessonRepository staffDailyTimeTableLessonRepository;
    final JmsTemplate jmsTemplate;
    final ObjectMapper objectMapper;


    @JmsListener(destination = "${queue.synchronizeMobileData}")
    @Transactional
    public void synchronizeMobileData(@NonNull Map<String, String> queryParam) {

        try {
            String telaSchoolNumber = queryParam.get("telaSchoolNumber");
            String dateParam = queryParam.get("date");


            log.info("synchronizeMobileData started for {}", queryParam);
            IdProjection schoolIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(telaSchoolNumber, Status.DELETED).orElseThrow(() -> new TelaNotFoundException("School with " + telaSchoolNumber + " not found"));

            School school = schoolRepository.findByStatusNotAndId(Status.DELETED, schoolIdProjection.getId()).orElseThrow(() -> new TelaNotFoundException("School not found"));
            AcademicTerm academicTerm = academicTermRepository.activeAcademicTerm(Status.ACTIVE).orElseThrow(() -> new TelaNotFoundException("Active term not found"));
            // school information
            // school
            publishSchoolData(school, academicTerm);

//                 classes
            publishSchoolClasses(school, academicTerm);

            // staff
            publishSchoolStaffs(school, academicTerm);

//                subjects
            publishSubjects(school, academicTerm);

            //publishLearnerEnrollments
            publishLearnerEnrollments(school, academicTerm);

            // publishSchoolTimetables
            publishSchoolTimetables(school, academicTerm);

            //publishStaffDailyTimetables
            publishStaffDailyTimetables(school, academicTerm, dateParam);

            //publishLearnerAttendance
            publishLearnerAttendance(school, academicTerm, dateParam);


            //publishStaffDailyTimeAttendance
            publishStaffDailyTimeAttendanceSupervision(school, academicTerm, dateParam);

            //publishStaffDailyTimetableTaskSupervision
            publishStaffDailyTimetableTaskSupervision(school, academicTerm, dateParam);
            //publishDistricts
            publishDistricts(school);

            // clockins
            publishSchoolClockIns(school, academicTerm, dateParam);

            //publishSchoolClockOuts
            publishSchoolClockOuts(school, academicTerm, dateParam);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void publishSchoolData(School school, AcademicTerm academicTerm) {
        log.info(" start publish");
        AcademicTermDTO academicTermDTO = AcademicTermDTO.builder()
                .id(academicTerm.getId())
                .name(academicTerm.getTerm())
                .year(academicTerm.getAcademicYear().getName())
                .startDate(academicTerm.getStartDate().format(TelaDatePattern.datePattern))
                .endDate(academicTerm.getEndDate().format(TelaDatePattern.datePattern))
                .build();

        SchoolDTO schoolDTO = SchoolDTO.builder()
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

        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<SchoolDTO> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.SCHOOL);
            responseDto.setData(schoolDTO);
            log.info(" start publish");
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
            log.info("publishSchoolDatafor {} {} {} ", academicTerm.getTerm(), school.getName(), responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @Override
    @Async
    @Transactional
    public void publishSchoolClasses(School school, AcademicTerm academicTerm) {
        try {

            List<ClassDTO> classDTOS = schoolClassRepository
                    .findAllByStatusNotAndAcademicTerm_IdAndSchool_Id(Status.DELETED, academicTerm.getId(), school.getId())
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
                List<SchoolClass> defaultClasses = this.generateDefaultClasses(school.getSchoolLevel())
                        .stream().map(schoolClass -> {
                            schoolClass.setCreatedDateTime(LocalDateTime.now());
                            schoolClass.setUpdatedDateTime(LocalDateTime.now());
                            schoolClass.setAcademicTerm(new AcademicTerm(academicTerm.getId()));
                            schoolClass.setSchool(new School(school.getId()));
                            schoolClass.setStatus(Status.ACTIVE);
                            return schoolClass;
                        }).collect(Collectors.toList());

                // create new
                schoolClassRepository.saveAll(defaultClasses);

                classDTOS = schoolClassRepository
                        .findAllByStatusNotAndAcademicTerm_IdAndSchool_Id(Status.DELETED, academicTerm.getId(), school.getId())
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


            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<ClassDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.CLASSES);
            responseDto.setData(classDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
            log.info("CLASES PUBLISHED for {} {} {} ", academicTerm.getTerm(), school.getName(), classDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

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
    @Async
    public void publishSchoolStaffs(School school, AcademicTerm academicTerm) {
        List<StaffDTO> staffDTOList = schoolStaffRepository.findAllBySchoolWithSchool_StaffDetail(Status.DELETED, school.getId())
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


//                        if (schoolStaff.getRegistrationNo() != null) {
//                            staffDTO.setRegistrationNo(schoolStaff.getRegistrationNo());
//                        } else {
//                            staffDTO.setRegistrationNo("");
//                        }
//
//                        if (schoolStaff.getNationality() != null) {
//                            staffDTO.setNationality(schoolStaff.getNationality());
//                        } else {
//                            staffDTO.setNationality("");
//                        }

//                        staffDTO.setTeachingStaff(schoolStaff.isTeachingstaff());
                        staffDTO.setHasSpecialNeeds((schoolStaff.getSpecialNeeds() == null || schoolStaff.getSpecialNeeds()) ? "true" : "false");
                        staffDTO.setStaffType(schoolStaff.isTeachingstaff() ? "Teaching" : "Non-Teaching");

                        staffDTO.setExpectedHours(schoolStaff.getExpectedHours() > 8 ? 8 : schoolStaff.getExpectedHours());
                    }


//                    SystemResponseDTO<List<SyncFaceIdDTO>> responseDTO = staffFaceIdService.findByRefId(s.getId());
//                    Optional<SyncFaceIdDTO> optionalFaceId = responseDTO.getData().stream().findFirst();
//                    if (optionalFaceId.isPresent()) {
//                        employeeModel.setFileDownloadUri(optionalFaceId.get().getFileDownloadUri());
//                    }

                    return staffDTO;
                })
                .sorted(Comparator.comparing(StaffDTO::getFirstName))
                .toList();

        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<StaffDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.STAFFS);
            responseDto.setData(staffDTOList);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
            log.info("STAFFS PUBLISHED for {} {} {} ", academicTerm.getTerm(), school.getName(), staffDTOList.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }


    }

    @Override
    @Async
    public void publishSchoolClockIns(School school, AcademicTerm academicTerm, String dateParam) {
        List<ClockInProjection> schoolDateClockIns;
        System.out.println("school " + school.getTelaSchoolUID());
        System.out.println("academicTerm " + academicTerm.getId());
        if ("all".equalsIgnoreCase(dateParam)) {
            schoolDateClockIns = clockInRepository.nativeAllByTerm_School(academicTerm.getId(), school.getId());
        } else {
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            schoolDateClockIns = clockInRepository.nativeAllByDate_School(localDate, school.getId());
        }

        System.out.println("schoolDateClockIns " + schoolDateClockIns.size());
        List<ClockInDTO> clockInDTOS = schoolDateClockIns.parallelStream().map(clockIn -> {

                    LocalDateTime clockInDateTime = LocalDateTime.of(clockIn.getClockInDate(), clockIn.getClockInTime());

                    ClockInDTO clockInDTO = ClockInDTO.builder()
                            .id(clockIn.getId())
                            .displacement(clockIn.getDisplacement())
                            .clockInDateTime(clockInDateTime.format(TelaDatePattern.dateTimePattern24))
                            .clockInType(clockIn.getClockinType())
                            .staffId(clockIn.getStaffId())
                            .academicTermId(academicTerm.getId())
                            .longitude(clockIn.getLongitude())
                            .latitude(clockIn.getLatitude())
                            .telaSchoolNumber(school.getTelaSchoolUID())
                            .build();

                    return clockInDTO;
                })
                .sorted(Comparator.comparing(ClockInDTO::getClockInDateTime))
                .toList();

        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<ClockInDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.CLOCKINS);
            responseDto.setData(clockInDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
            log.info("CLOCKINS PUBLISHED for {} {} {} ", academicTerm.getTerm(), school.getName(), clockInDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @Override
    @Async
    public void publishSubjects(School school, AcademicTerm academicTerm) {

        SchoolLevel schoolLevel = school.getSchoolLevel();
        SubjectClassification subjectClassification = SubjectClassification.getSubjectClassification(schoolLevel.getLevel());
        List<IdNameCodeDTO> subjectDTOS = subjectRepository.findAllBySubjectClassificationNotNullAndStatusNotAndSubjectClassification(Status.DELETED, subjectClassification)
                .parallelStream().map(subject -> new IdNameCodeDTO(subject.getId(), subject.getName(), subject.getCode()))
                .sorted(Comparator.comparing(IdNameCodeDTO::code))
                .toList();

        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<IdNameCodeDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.SUBJECTS);
            responseDto.setData(subjectDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
            log.info("Subjects PUBLISHED for {} {} {} ", academicTerm.getTerm(), school.getName(), subjectDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @Override
    @Async
    public void publishLearnerEnrollments(School school, AcademicTerm academicTerm) {

        List<LearnerHeadCountDTO> generalLearnerHeadCountDTOS = learnerEnrollmentRepository.allBySchool_term(school.getId(), academicTerm.getId()).parallelStream()
                .map(enrollment -> {
                    LearnerHeadCountDTO learnerHeadCountDTO = LearnerHeadCountDTO.builder()
                            .staffId(enrollment.getSchoolStaff().getId())
                            .classId(enrollment.getSchoolClass().getId())
                            .learnerType(LearnerType.GENERAL.getType())
                            .totalFemale(enrollment.getTotalGirls())
                            .totalMale(enrollment.getTotalBoys())
                            .submissionDate(enrollment.getSubmissionDate().format(TelaDatePattern.datePattern))
                            .build();

                    return learnerHeadCountDTO;
                }).collect(Collectors.toList());

        List<LearnerHeadCountDTO> snLearnerHeadCountDTOS = snLearnerEnrollmentRepository.allBySchool_term(school.getId(), academicTerm.getId()).parallelStream()
                .map(enrollment -> {
                    LearnerHeadCountDTO learnerHeadCountDTO = LearnerHeadCountDTO.builder()
                            .staffId(enrollment.getSchoolStaff().getId())
                            .classId(enrollment.getSchoolClass().getId())
                            .learnerType(LearnerType.SPECIAL_NEEDS.getType())
                            .totalFemale(enrollment.getTotalGirls())
                            .totalMale(enrollment.getTotalBoys())
                            .submissionDate(enrollment.getSubmissionDate().format(TelaDatePattern.datePattern))
                            .build();

                    return learnerHeadCountDTO;
                }).toList();
        generalLearnerHeadCountDTOS.addAll(snLearnerHeadCountDTOS);

        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<LearnerHeadCountDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.LEARNER_HEADCOUNTS);
            responseDto.setData(generalLearnerHeadCountDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
            log.info("LEARNER_HEADCOUNTS PUBLISHED for {} {} {} ", academicTerm.getTerm(), school.getName(), generalLearnerHeadCountDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @Override
    @Async
    public void publishLearnerAttendance(School school, AcademicTerm academicTerm, String dateParam) {
        log.info("publishLearnerAttendance");
        List<LearnerAttendance> learnerAttendanceList;
        List<SNLearnerAttendance> snLearnerAttendanceList;
        if ("all".equalsIgnoreCase(dateParam) || dateParam == null) {
            learnerAttendanceList = learnerAttendanceRepository.allByTerm_School(academicTerm.getId(), school.getId());
            snLearnerAttendanceList = snLearnerAttendanceRepository.allByTerm_School(academicTerm.getId(), school.getId());
            learnerAttendanceRepository.allByTerm_School(academicTerm.getId(), school.getId());

        } else {
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            learnerAttendanceList = learnerAttendanceRepository.allByDate_School(localDate, school.getId());
            snLearnerAttendanceList = snLearnerAttendanceRepository.allByDate_School(localDate, school.getId());
        }


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


        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<LearnerAttendanceDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.LEARNER_ATTENDANCES);
            responseDto.setData(generalLearnerAttendanceDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
            log.info("LEARNER_HEADCOUNTS published for {} {} {} ", academicTerm.getTerm(), school.getName(), generalLearnerAttendanceDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @Override
    @Async
    public void publishStaffDailyTimeAttendanceSupervision(School school, AcademicTerm academicTerm, String dateParam) {
        log.info("publishStaffDailyTimeAttendanceSupervision {} ", dateParam);
        log.info("AcademicTerm {} ", school.getId());
        log.info("School {} ", academicTerm);
        List<StaffDailyAttendanceSupervision> staffDailyAttendanceSupervisions;
        if ("all".equalsIgnoreCase(dateParam) || dateParam == null) {
            staffDailyAttendanceSupervisions = staffDailyAttendanceSupervisionRepository.allByTerm_School(academicTerm.getStartDate(), academicTerm.getEndDate(), school.getId());
        } else {
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            staffDailyAttendanceSupervisions = staffDailyAttendanceSupervisionRepository.allByDate_School(localDate, school.getId());
        }


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


        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<StaffDailyTimeAttendanceDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.STAFF_DAILY_TIME_ATTENDANCES);
            responseDto.setData(staffDailyTimeAttendanceDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
            log.info("STAFF_DAILY_TIME_ATTENDANCES published for {} {} {} ", academicTerm.getTerm(), school.getName(), staffDailyTimeAttendanceDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @Override
    @Async
    public void publishDistricts(School school) {
        List<DistrictDTO> districtDTOS = districtRepository.findAllByStatusNot(Status.DELETED)
                .parallelStream()
                .map(district -> new DistrictDTO(district.getId(), district.getName(), district.getRegion().getName()))
                .sorted(Comparator.comparing(DistrictDTO::name))
                .toList();

        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<DistrictDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.DISTRICTS);
            responseDto.setData(districtDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
            log.info("DISTRICTS Published for {} {} ", school.getName(), districtDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @Override
    @Async
    public void publishSchoolTimetables(School school, AcademicTerm academicTerm) {
        Optional<IdProjection> idProjectionOptional = timeTableRepository.findBySchool_IdAndAcademicTerm_Id(school.getId(), academicTerm.getId());

//                .orElseThrow(() -> new TelaNotFoundException(school.getName() + "Timetable not found from " + academicTerm.getTerm()));

        if (idProjectionOptional.isPresent()) {
            IdProjection idProjection = idProjectionOptional.get();

            TimetableDTO timetableDTO = TimetableDTO.builder()
                    .schoolId(school.getId())
                    .academicTermId(academicTerm.getId())
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

            try {
                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<TimetableDTO> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.SCHOOL_TIMETABLE);
                responseDto.setData(timetableDTO);
                jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
                log.info("SCHOOL_TIMETABLE published for {} \n {} \n {}  ", academicTerm.getTerm(), school.getName(), timetableDTO);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
        } else {
            try {
                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<TimetableDTO> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.SCHOOL_TIMETABLE);
                responseDto.setData(new TimetableDTO());
                jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
                log.info("SCHOOL_TIMETABLE published for {} \n {} \n {}  ", academicTerm.getTerm(), school.getName(), new TimetableDTO());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }


    }

    @Override
    @Async
    public void publishStaffDailyTimetables(School school, AcademicTerm academicTerm, String dateParam) {
        log.info("publishStaffDailyTimetables");


        List<StaffDailyTimetableDTO> staffDailyTimetableDTOS;

        if ("all".equalsIgnoreCase(dateParam) || dateParam == null) {
            List<StaffDailyTimeTable> termStaffDailyTimeTables = staffDailyTimeTableRepository.allByTerm_School(academicTerm.getId(), school.getId());
            List<StaffDailyTimeTableLesson> termStaffDailyTimeTableLessons = staffDailyTimeTableLessonRepository.allIn(termStaffDailyTimeTables);

            staffDailyTimetableDTOS = termStaffDailyTimeTables.parallelStream().flatMap(dailyTimeTable -> termStaffDailyTimeTableLessons.parallelStream()
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


        } else {
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            List<StaffDailyTimeTable> dateStaffDailyTimeTables = staffDailyTimeTableRepository.allByDate_School(localDate, school.getId());
            List<StaffDailyTimeTableLesson> dateStaffDailyTimeTableLessons = staffDailyTimeTableLessonRepository.allIn(dateStaffDailyTimeTables);

            staffDailyTimetableDTOS = dateStaffDailyTimeTables.parallelStream().flatMap(dailyTimeTable -> dateStaffDailyTimeTableLessons.parallelStream()
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

        }


        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<StaffDailyTimetableDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.STAFF_DAILY_TIMETABLES);
            responseDto.setData(staffDailyTimetableDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
            log.info("STAFF_DAILY_TIMETABLES published for {} {} {} ", academicTerm.getTerm(), school.getName(), staffDailyTimetableDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @Override
    @Async
    public void publishStaffDailyTimetableTaskSupervision(School school, AcademicTerm academicTerm, String dateParam) {
        log.info("publishStaffDailyTimetableTaskSupervision");

        List<StaffDailyAttendanceTaskSupervisionDTO> staffDailyAttendanceTaskSupervisionDTOS;
        if ("all".equalsIgnoreCase(dateParam) || dateParam == null) {
            List<StaffDailyAttendanceSupervision> termStaffDailyAttendanceSupervisions = staffDailyAttendanceSupervisionRepository.allByTerm_School(academicTerm.getStartDate(), academicTerm.getEndDate(), school.getId());
            List<StaffDailyAttendanceTaskSupervision> termStaffDailyAttendanceTaskSupervisions = staffDailyAttendanceTaskSupervisionRepository.allIn(termStaffDailyAttendanceSupervisions);


            staffDailyAttendanceTaskSupervisionDTOS = termStaffDailyAttendanceSupervisions.parallelStream().flatMap(attendanceSupervision -> termStaffDailyAttendanceTaskSupervisions.parallelStream()
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


        } else {
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            List<StaffDailyAttendanceSupervision> termStaffDailyAttendanceSupervisions = staffDailyAttendanceSupervisionRepository.allByDate_School(localDate, school.getId());
            List<StaffDailyAttendanceTaskSupervision> termStaffDailyAttendanceTaskSupervisions = staffDailyAttendanceTaskSupervisionRepository.allIn(termStaffDailyAttendanceSupervisions);

            staffDailyAttendanceTaskSupervisionDTOS = termStaffDailyAttendanceSupervisions.parallelStream().flatMap(attendanceSupervision -> termStaffDailyAttendanceTaskSupervisions.parallelStream()
                            .filter(taskSupervision -> attendanceSupervision.getId().equals(taskSupervision.getStaffDailyAttendanceSupervision().getId()))
                            .map(taskSupervision -> {
                                StaffDailyAttendanceTaskSupervisionDTO dto = StaffDailyAttendanceTaskSupervisionDTO.builder()
                                        .id(taskSupervision.getId())
                                        .timeAttendanceId(attendanceSupervision.getId())
                                        .lessonTaskId(attendanceSupervision.getId())
                                        .supervisorId(attendanceSupervision.getSupervisor().getId())
                                        .supervisionDate(attendanceSupervision.getSupervisionDate().format(TelaDatePattern.datePattern))
                                        .supervisionTime(attendanceSupervision.getSupervisionTime().format(TelaDatePattern.timePattern24))
                                        .timeStatus(taskSupervision.getTeachingTimeStatus() != null ? taskSupervision.getTeachingTimeStatus().getSupervision() : SupervisionStatus.NOT_TAUGHT.getSupervision())
                                        .supervisionComment(taskSupervision.getComment())
                                        .build();
                                return dto;
                            })
                    ).sorted(Comparator.comparing(StaffDailyAttendanceTaskSupervisionDTO::getSupervisionDate))
                    .toList();
        }


        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<StaffDailyAttendanceTaskSupervisionDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.STAFF_DAILY_TASK_SUPERVISIONS);
            responseDto.setData(staffDailyAttendanceTaskSupervisionDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
            log.info("STAFF_DAILY_TASK_SUPERVISION published for {} {} {} ", academicTerm.getTerm(), school.getName(), staffDailyAttendanceTaskSupervisionDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @Override
    @Async
    public void publishSchoolClockOuts(School school, AcademicTerm academicTerm, String dateParam) {
        List<ClockOut> schoolClockOuts;
        if ("all".equalsIgnoreCase(dateParam)) {
            schoolClockOuts = clockOutRepository.allByTerm_SchoolWithStaff(academicTerm.getId(), school.getId());
        } else {
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            schoolClockOuts = clockOutRepository.allByDate_SchoolWithStaff(localDate, school.getId());
        }
        List<ClockOutDTO> clockOutDTOS = schoolClockOuts.parallelStream().map(clockOut -> {

                    LocalDateTime clockOutDateTime = LocalDateTime.of(clockOut.getClockOutDate(), clockOut.getClockOutTime());

                    ClockIn clockIn = clockOut.getClockIn();
                    ClockOutDTO clockOutDTO = ClockOutDTO.builder()
                            .clockInId(clockIn.getId())
                            .telaSchoolNumber(school.getTelaSchoolUID())
                            .academicTermId(academicTerm.getId())
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

        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<ClockOutDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.CLOCKOUTS);
            responseDto.setData(clockOutDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
            log.info("CLOCKOUTS PUBLISHED for {} {} {} ", academicTerm.getTerm(), school.getName(), clockOutDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

}
