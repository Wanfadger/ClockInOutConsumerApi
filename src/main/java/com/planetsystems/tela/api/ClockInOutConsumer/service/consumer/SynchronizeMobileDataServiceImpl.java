package com.planetsystems.tela.api.ClockInOutConsumer.service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.*;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.IdProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.*;
import com.planetsystems.tela.api.ClockInOutConsumer.model.*;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.SchoolLevel;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.SubjectClassification;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SynchronizeMobileDataServiceImpl implements SynchronizeMobileDataService {
    private final SchoolRepository schoolRepository;
    private final AcademicTermRepository academicTermRepository;
    private final SchoolClassRepository schoolClassRepository;

    private final SchoolStaffRepository schoolStaffRepository;
    final ClockInRepository clockInRepository;

    final SubjectRepository subjectRepository;
    final LearnerEnrollmentRepository learnerEnrollmentRepository;
    final SNLearnerEnrollmentRepository snLearnerEnrollmentRepository;
    final LearnerAttendanceRepository learnerAttendanceRepository;
    final SNLearnerAttendanceRepository snLearnerAttendanceRepository;


    final JmsTemplate jmsTemplate;
    final ObjectMapper objectMapper;




    @JmsListener(destination = "${queue.synchronizeMobileData}")
    @Transactional
    public void synchronizeMobileData(@NonNull Map<String , String> queryParam)  {

      try{
          String telaSchoolNumber = queryParam.get("telaSchoolNumber");
          String dateParam = queryParam.get("date");


          log.info("synchronizeMobileData started for {}", queryParam);


          Optional<IdProjection> schoolIdByTelaNumberOptional = schoolRepository.findByTelaSchoolUIDAndStatusNot(telaSchoolNumber , Status.DELETED);

          if (schoolIdByTelaNumberOptional.isPresent()) {
              IdProjection schoolIdProjection = schoolIdByTelaNumberOptional.get();
              Optional<School> schoolOptional = schoolRepository.findByStatusNotAndId(Status.DELETED, schoolIdProjection.getId());
              Optional<AcademicTerm> optionalAcademicTerm = academicTermRepository.activeAcademicTerm(Status.ACTIVE);
              // school information

              if (schoolOptional.isPresent() && optionalAcademicTerm.isPresent()) {
                  School school = schoolOptional.get();
                  AcademicTerm academicTerm = optionalAcademicTerm.get();
                  // school
                  publishSchoolData(school, academicTerm);

//                 classes
                  publishSchoolClasses(school, academicTerm);
//
                  // staff
                  publishSchoolStaffs(school ,academicTerm);
//
//                // clockins
                  publishSchoolClockIns(school, academicTerm ,dateParam);

//                subjects
                  publishSubjects(school, academicTerm);

                  //publishLearnerEnrollments
                  publishLearnerEnrollments(school, academicTerm);

                  //publishLearnerAttendance
                  publishLearnerAttendance(school, academicTerm , dateParam);

              }
          }
      }catch (Exception e){
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
            log.info("publishSchoolData {} {}" ,school.getTelaSchoolNumber()   ,responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @Override
    @Async
    public void publishSchoolClasses(School school, AcademicTerm academicTerm) {

        List<ClassDTO> classDTOS = schoolClassRepository
                .findAllByStatusNotAndAcademicTerm_IdAndSchool_Id(Status.DELETED, academicTerm.getId(), school.getId())
                .parallelStream().map(schoolClass -> {
                    ClassDTO dto = new ClassDTO();
                    dto.setName(schoolClass.getName());
                    dto.setId(schoolClass.getId());

//                    dto.setHasStreams(schoolClass.isHasStreams());
//                    dto.setClassLevel(schoolClass.isClassLevel());

                    if (schoolClass.getParentSchoolClass() != null) {
//                        dto.setParentSchoolClassName(schoolClass.getParentSchoolClass().getName());
                        dto.setParentSchoolClassId(schoolClass.getParentSchoolClass().getId());
                    } else {
//                        dto.setParentSchoolClassName(null);
                        dto.setParentSchoolClassId(null);
                    }

                    return dto;
                }).sorted(Comparator.comparing(ClassDTO::getName))
                .collect(Collectors.toList());

        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<ClassDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.CLASSES);
            responseDto.setData(classDTOS);
            log.info("publishSchoolClasses classes");
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
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
                            staffDTO.setEmail(generalUserDetail.getEmail());
                        } else {
                            staffDTO.setEmail("");
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

                        staffDTO.setTeachingStaff(schoolStaff.isTeachingstaff());
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
            log.info("publishSchoolStaffs ");
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }


    }

    @Override
    @Async
    public void publishSchoolClockIns(School school, AcademicTerm academicTerm,String dateParam) {
        List<ClockIn> schoolDateClockIns;
        if ("all".equalsIgnoreCase(dateParam)){
            schoolDateClockIns = clockInRepository.allClockByTerm_SchoolWithStaff(academicTerm.getId(), school.getId());
        }else{
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            schoolDateClockIns = clockInRepository.allClockByDate_SchoolWithStaff(localDate, school.getId());
        }
        List<ClockInRequestDTO> clockInRequestDTOS = schoolDateClockIns.parallelStream().map(clockIn -> {

                    LocalDateTime clockInDateTime = LocalDateTime.of(clockIn.getClockInDate(), clockIn.getClockInTime());

                    ClockInRequestDTO clockInRequestDTO = ClockInRequestDTO.builder()
                            .displacement(clockIn.getDisplacement())
                            .clockInDateTime(clockInDateTime.format(TelaDatePattern.dateTimePattern24))
                            .clockInType(clockIn.getClockinType())
                            .staffId(clockIn.getSchoolStaff().getId())
                            .academicTermId(academicTerm.getId())
                            .longitude(clockIn.getLongitude())
                            .latitude(clockIn.getLatitude())
                            .telaSchoolNumber(school.getTelaSchoolNumber())
                            .build();

                    return clockInRequestDTO;
                })
                .sorted(Comparator.comparing(ClockInRequestDTO::getClockInDateTime))
                .toList();

        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<ClockInRequestDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.CLOCKINS);
            responseDto.setData(clockInRequestDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
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
                .parallelStream().map(subject -> new IdNameCodeDTO(subject.getId(), subject.getName() , subject.getCode()))
                .sorted(Comparator.comparing(IdNameCodeDTO::code))
                .toList();

        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<IdNameCodeDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.SUBJECTS);
            responseDto.setData(subjectDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @Override
    @Async
    public void publishLearnerEnrollments(School school, AcademicTerm academicTerm) {
        log.info("publishLearnerEnrollments");
        List<SNLearnerEnrollment> snLearnerEnrollments = snLearnerEnrollmentRepository.allBySchool_term(school.getId(), academicTerm.getId());

        List<LearnerHeadCountDTO> learnerHeadCountDTOS = learnerEnrollmentRepository.allBySchool_term(school.getId(), academicTerm.getId()).parallelStream()
                .map(enrollment -> {
                    LearnerHeadCountDTO learnerHeadCountDTO = LearnerHeadCountDTO.builder()

                    .submissionDate(enrollment.getSubmissionDate().format(TelaDatePattern.datePattern))
                    .general(new GenderCountDTO(enrollment.getTotalBoys(), enrollment.getTotalGirls()))
                    .schoolClass(new IdNameDTO(enrollment.getSchoolClass().getId(), enrollment.getSchoolClass().getName()))
                    .build();

                    Optional<SNLearnerEnrollment> optionalSNLearnerEnrollment = snLearnerEnrollments.parallelStream().filter(snEnrollment -> snEnrollment.getSchoolClass().getId().equals(enrollment.getSchoolClass().getId())).findFirst();

                    if (optionalSNLearnerEnrollment.isPresent()) {
                        SNLearnerEnrollment snLearnerEnrollment = optionalSNLearnerEnrollment.get();
                        learnerHeadCountDTO.setSpecialNeeds(new GenderCountDTO(snLearnerEnrollment.getTotalBoys() , snLearnerEnrollment.getTotalGirls()));
                    }
            return learnerHeadCountDTO;
        }).sorted(Comparator.comparing(learnerHeadCountDTO -> learnerHeadCountDTO.getSchoolClass().name())).toList();


        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<LearnerHeadCountDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.LEARNER_HEADCOUNTS);
            responseDto.setData(learnerHeadCountDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @Override
    @Async
    public void publishLearnerAttendance(School school, AcademicTerm academicTerm , String dateParam) {
        log.info("publishLearnerAttendance");
        List<LearnerAttendance> learnerAttendanceList;
        List<SNLearnerAttendance> snLearnerAttendanceList;
        if ("all".equalsIgnoreCase(dateParam) || dateParam == null){
            learnerAttendanceList = learnerAttendanceRepository.allByTerm_School(academicTerm.getId(), school.getId());
            snLearnerAttendanceList = snLearnerAttendanceRepository.allByTerm_School(academicTerm.getId() ,school.getId());
            learnerAttendanceRepository.allByTerm_School(academicTerm.getId(), school.getId());

        }else{
            LocalDate localDate = LocalDate.parse(dateParam, TelaDatePattern.datePattern);
            learnerAttendanceList = learnerAttendanceRepository.allByDate_School(localDate, school.getId());
            snLearnerAttendanceList = snLearnerAttendanceRepository.allByDate_School(localDate ,school.getId());
        }

        List<LearnerAttendanceDTO> learnerAttendanceDTOS = learnerAttendanceList.parallelStream().map(learnerAttendance -> {
                    LearnerAttendanceDTO learnerAttendanceDTO = LearnerAttendanceDTO.builder()
                            .attendanceDate(learnerAttendance.getAttendanceDate().format(TelaDatePattern.datePattern))
                            .classId(learnerAttendance.getSchoolClass().getId())
                            .general(LearnerAttendanceDTO.AttendanceDTO.builder()
                                    .girlsPresent(learnerAttendance.getGirlsPresent())
                                    .boysPresent(learnerAttendance.getBoysPresent())
                                    .girlsAbsent(learnerAttendance.getGirlsAbsent())
                                    .boysAbsent(learnerAttendance.getBoysAbsent())
                                    .staffId(learnerAttendance.getSchoolStaff().getId())
                                    .comment(learnerAttendance.getComment())
                                    .build())
                            .build();

                    Optional<SNLearnerAttendance> optionalSNLearnerAttendance = snLearnerAttendanceList.parallelStream()
                            .filter(snLearnerAttendance -> snLearnerAttendance.getSchoolClass().getId().equals(learnerAttendance.getSchoolClass().getId()))
                            .findFirst();
                    if (optionalSNLearnerAttendance.isPresent()) {
                        SNLearnerAttendance snLearnerAttendance = optionalSNLearnerAttendance.get();
                        learnerAttendanceDTO.setSpecialNeeds(LearnerAttendanceDTO.AttendanceDTO.builder()
                                .comment(snLearnerAttendance.getComment())
                                .boysAbsent(snLearnerAttendance.getBoysAbsent())
                                .girlsAbsent(snLearnerAttendance.getGirlsAbsent())
                                .boysPresent(snLearnerAttendance.getBoysPresent())
                                .staffId(snLearnerAttendance.getSchoolStaff().getId())
                                .girlsPresent(snLearnerAttendance.getGirlsPresent())
                                .build());
                    }
                    return learnerAttendanceDTO;
                }).sorted(Comparator.comparing(LearnerAttendanceDTO::getAttendanceDate))
                .toList();


        try {
            jmsTemplate.setPubSubDomain(true);
            MQResponseDto<List<LearnerAttendanceDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.LEARNER_ATTENDANCES);
            responseDto.setData(learnerAttendanceDTOS);
            jmsTemplate.convertAndSend(school.getTelaSchoolUID(), objectMapper.writeValueAsString(responseDto));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }


}
