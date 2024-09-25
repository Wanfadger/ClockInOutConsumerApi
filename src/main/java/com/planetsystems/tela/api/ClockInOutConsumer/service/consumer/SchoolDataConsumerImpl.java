package com.planetsystems.tela.api.ClockInOutConsumer.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.*;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.IdProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.*;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.schoolData.SchoolDataPublishPayloadDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.exception.TelaNotFoundException;
import com.planetsystems.tela.api.ClockInOutConsumer.model.*;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.TelaDatePattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolDataConsumerImpl {

    final ObjectMapper objectMapper;
    final LearnerEnrollmentRepository learnerEnrollmentRepository;
    final SNLearnerEnrollmentRepository snLearnerEnrollmentRepository;
    final SchoolRepository schoolRepository;
    final LearnerAttendanceRepository learnerAttendanceRepository;
    final SNLearnerAttendanceRepository snLearnerAttendanceRepository;
    final SchoolClassRepository schoolClassRepository;
    final AcademicTermRepository academicTermRepository;
    final SchoolStaffRepository schoolStaffRepository;
    final StaffDailyAttendanceSupervisionRepository staffDailyAttendanceSupervisionRepository;
    final JmsTemplate jmsTemplate;





    @JmsListener(destination = "${queue.learnerHeadCounts}")
    @Transactional
    public void subscribeLearnerHeadCounts(String learnerHeadCountStr) throws JsonProcessingException {
        log.info("learnerHeadCountStr {}  " , learnerHeadCountStr);
        SchoolDataPublishPayloadDTO<List<LearnerHeadCountDTO>> publishPayloadDTO = objectMapper.readValue(learnerHeadCountStr, new TypeReference<SchoolDataPublishPayloadDTO<List<LearnerHeadCountDTO>>>() {
        });

        List<LearnerHeadCountDTO> allLearnerHeadCountDTOS =  publishPayloadDTO.getData();
        log.info("ddd {} " , allLearnerHeadCountDTOS);

        AcademicTerm academicTerm = academicTermRepository.findById(publishPayloadDTO.getAcademicTerm()).orElseThrow(() -> new TelaNotFoundException("Term " + publishPayloadDTO.getAcademicTerm() + " not found"));

        Optional<IdProjection> optionalIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(publishPayloadDTO.getSchoolTelaNumber(), Status.DELETED);
        if (optionalIdProjection.isPresent()) {
            log.info("FOUND TELA NUMBER {} " , publishPayloadDTO.getSchoolTelaNumber());
            IdProjection idProjection = optionalIdProjection.get();
            List<LearnerEnrollment> learnerEnrollments = learnerEnrollmentRepository.allBySchool_term(idProjection.getId(), publishPayloadDTO.getAcademicTerm());
            List<SNLearnerEnrollment> snLearnerEnrollments = snLearnerEnrollmentRepository.allBySchool_term(idProjection.getId(), publishPayloadDTO.getAcademicTerm());

            // process new general learners
            List<LearnerHeadCountDTO> allGeneralLearnerHeadCountDTOS = allLearnerHeadCountDTOS.parallelStream().filter(dto -> dto.getLearnerType().equalsIgnoreCase("General")).toList();
            List<LearnerEnrollment> newGeneralLearnerEnrollments = allGeneralLearnerHeadCountDTOS.parallelStream()
                    .filter(dto -> learnerEnrollments.parallelStream().noneMatch(enrollment -> dto.getClassId().equals(enrollment.getSchoolClass().getId())))
                    .map(dto -> {
                        LearnerEnrollment enrollment = LearnerEnrollment.builder()
                                .schoolClass(new SchoolClass(dto.getClassId()))
                                .enrollmentStatus(Status.ACTIVE)
                                .schoolStaff(new SchoolStaff(dto.getStaffId()))
                                .totalBoys(dto.getTotalMale())
                                .totalGirls(dto.getTotalFemale())
                                .submissionDate(LocalDate.parse(dto.getSubmissionDate(), TelaDatePattern.datePattern))
                                .build();
                        return enrollment;
                    })
                    .toList();
            if (newGeneralLearnerEnrollments.size()>0){
                learnerEnrollmentRepository.saveAll(newGeneralLearnerEnrollments);
            }
            log.info("newGeneralLearnerEnrollment {} " , newGeneralLearnerEnrollments.size());

            // process new sn learners
            List<LearnerHeadCountDTO> allSnLearnerHeadCountDTOS = allLearnerHeadCountDTOS.parallelStream().filter(dto -> dto.getLearnerType().equalsIgnoreCase("Special Needs")).toList();
            List<SNLearnerEnrollment> newSnLearnerEnrollments = allSnLearnerHeadCountDTOS.parallelStream()
                    .filter(dto -> snLearnerEnrollments.parallelStream().noneMatch(enrollment -> dto.getClassId().equals(enrollment.getSchoolClass().getId())))
                    .map(dto -> {
                        SNLearnerEnrollment enrollment = SNLearnerEnrollment.builder()
                                .schoolClass(new SchoolClass(dto.getClassId()))
                                .enrollmentStatus(Status.ACTIVE)
                                .schoolStaff(new SchoolStaff(dto.getStaffId()))
                                .totalBoys(dto.getTotalMale())
                                .totalGirls(dto.getTotalFemale())
                                .submissionDate(LocalDate.parse(dto.getSubmissionDate(), TelaDatePattern.datePattern))
                                .build();
                        return enrollment;
                    })
                    .toList();
            if (newSnLearnerEnrollments.size()>0){
                snLearnerEnrollmentRepository.saveAll(newSnLearnerEnrollments);
            }
            log.info("newSnLearnerEnrollment {} " , newSnLearnerEnrollments.size());


            // process existing
            List<LearnerEnrollment> existingGeneralLearnerEnrollments = allGeneralLearnerHeadCountDTOS.parallelStream()
                    .flatMap(dto -> learnerEnrollments.parallelStream()
                            .filter(enrollment -> dto.getClassId().equals(enrollment.getSchoolClass().getId()))
                            .map(enrollment -> {
                                enrollment.setTotalBoys(enrollment.getTotalBoys());
                                enrollment.setTotalGirls(enrollment.getTotalGirls());
                                return enrollment;
                            })
                    ).toList();
            if (existingGeneralLearnerEnrollments.size()>0){
                learnerEnrollmentRepository.saveAll(existingGeneralLearnerEnrollments);
            }
            log.info("existingGeneralLearnerEnrollment {} " , existingGeneralLearnerEnrollments.size());


            List<SNLearnerEnrollment> existingSnLearnerEnrollments = allSnLearnerHeadCountDTOS.parallelStream()
                    .flatMap(dto -> snLearnerEnrollments.parallelStream()
                            .filter(enrollment -> dto.getClassId().equals(enrollment.getSchoolClass().getId()))
                            .map(enrollment -> {
                                enrollment.setTotalBoys(enrollment.getTotalBoys());
                                enrollment.setTotalGirls(enrollment.getTotalGirls());
                                return enrollment;
                            })
                    ).toList();
            if (existingSnLearnerEnrollments.size()>0){
                snLearnerEnrollmentRepository.saveAll(existingSnLearnerEnrollments);
            }
            log.info("existingSnLearnerEnrollment {} " , existingSnLearnerEnrollments.size());
            try {
                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<List<LearnerHeadCountDTO>> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.LEARNER_HEADCOUNTS);
                responseDto.setData(allLearnerHeadCountDTOS);
                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED LEARNER_HEADCOUNTS  for {} {} {} ",academicTerm.getTerm(), idProjection.getId() ,  allLearnerHeadCountDTOS.size());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }

        }


    }


    @JmsListener(destination = "${queue.classAttendances}")
    @Transactional
    public void subscribeClassAttendances(String classAttendanceStr) throws JsonProcessingException {
        log.info("subscribeClassAttendances {}  " , classAttendanceStr);
        SchoolDataPublishPayloadDTO<List<LearnerAttendanceDTO>> publishPayloadDTO = objectMapper.readValue(classAttendanceStr, new TypeReference<SchoolDataPublishPayloadDTO<List<LearnerAttendanceDTO>>>() {
        });

        AcademicTerm academicTerm = academicTermRepository.findById(publishPayloadDTO.getAcademicTerm()).orElseThrow(() -> new TelaNotFoundException("Term " + publishPayloadDTO.getAcademicTerm() + " not found"));

        List<LearnerAttendanceDTO> allClassAttendanceDTOS =  publishPayloadDTO.getData();
        log.info("allClassAttendanceDTOS {} " , allClassAttendanceDTOS);

        Optional<IdProjection> optionalIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(publishPayloadDTO.getSchoolTelaNumber(), Status.DELETED);
        if (optionalIdProjection.isPresent()) {
            log.info("FOUND TELA NUMBER {} " , publishPayloadDTO.getSchoolTelaNumber());
            IdProjection idProjection = optionalIdProjection.get();
            LearnerAttendanceDTO firstAttendanceDTO = allClassAttendanceDTOS.parallelStream().findFirst().get();
            LocalDate attendanceDate = LocalDate.parse(firstAttendanceDTO.getSubmissionDate() , TelaDatePattern.datePattern);

            List<LearnerAttendance> allExitingLearnerAttendanceList = learnerAttendanceRepository.allByDate_School(attendanceDate , idProjection.getId());
            List<SNLearnerAttendance> allExistingSnLearnerAttendanceList = snLearnerAttendanceRepository.allByDate_School(attendanceDate, idProjection.getId());

            // process new general learners
            List<LearnerAttendanceDTO> allGeneralLearnerAttendanceDTOS = allClassAttendanceDTOS.parallelStream().filter(dto -> dto.getLearnerType().equalsIgnoreCase(LearnerType.GENERAL.getType())).toList();
            List<LearnerAttendance> newGeneralLearnerAttendances = allGeneralLearnerAttendanceDTOS.parallelStream()
                    .filter(dto -> allExitingLearnerAttendanceList.parallelStream().noneMatch(learnerAttendance -> dto.getClassId().equals(learnerAttendance.getSchoolClass().getId())))
                    .map(dto -> {
                        LearnerAttendance attendance = LearnerAttendance.builder()
                                .schoolClass(new SchoolClass(dto.getClassId()))
                                .academicTerm(new AcademicTerm(publishPayloadDTO.getAcademicTerm()))
                                .schoolStaff(new SchoolStaff(dto.getStaffId()))
                                .attendanceDate(attendanceDate)
                                .status(Status.ACTIVE)
                                .girlsAbsent(dto.getFemaleAbsent())
                                .boysAbsent(dto.getMaleAbsent())
                                .girlsPresent(dto.getFemalePresent())
                                .boysPresent(dto.getMalePresent())
                                .comment(dto.getComment())
                                .build();
                        return attendance;
                    })
                    .toList();


            if (newGeneralLearnerAttendances.size()>0){
                learnerAttendanceRepository.saveAll(newGeneralLearnerAttendances);
            }
            log.info("newGeneralLearnerAttendances {} " , newGeneralLearnerAttendances.size());

            // process new sn learners
            List<LearnerAttendanceDTO> allSnLearnerAttendanceDTOS = allClassAttendanceDTOS.parallelStream().filter(dto -> dto.getLearnerType().equalsIgnoreCase(LearnerType.SPECIAL_NEEDS.getType())).toList();
            List<SNLearnerAttendance> newSnLearnerAttendances = allSnLearnerAttendanceDTOS.parallelStream()
                    .filter(dto -> allExistingSnLearnerAttendanceList.parallelStream().noneMatch(attendance -> dto.getClassId().equals(attendance.getSchoolClass().getId())))
                    .map(dto -> {
                        SNLearnerAttendance attendance = SNLearnerAttendance.builder()
                                .schoolClass(new SchoolClass(dto.getClassId()))
                                .schoolStaff(new SchoolStaff(dto.getStaffId()))
                                .academicTerm(new AcademicTerm(publishPayloadDTO.getAcademicTerm()))
                                .attendanceDate(attendanceDate)
                                .status(Status.ACTIVE)
                                .girlsAbsent(dto.getFemaleAbsent())
                                .boysAbsent(dto.getMaleAbsent())
                                .girlsPresent(dto.getFemalePresent())
                                .boysPresent(dto.getMalePresent())
                                .comment(dto.getComment())
                                .build();
                        return attendance;
                    })
                    .toList();
            if (newSnLearnerAttendances.size()>0){
                snLearnerAttendanceRepository.saveAll(newSnLearnerAttendances);
            }
            log.info("newSnLearnerAttendances {} " , newSnLearnerAttendances.size());


            // process existing
            List<LearnerAttendance> existingGeneralLearnerAttendances = allGeneralLearnerAttendanceDTOS.parallelStream()
                    .flatMap(dto -> allExitingLearnerAttendanceList.parallelStream()
                            .filter(attendance -> dto.getClassId().equals(attendance.getSchoolClass().getId()))
                            .map(attendance -> {
                                attendance.setBoysAbsent(dto.getFemaleAbsent());
                                attendance.setGirlsAbsent(dto.getFemaleAbsent());
                                attendance.setBoysPresent(dto.getMalePresent());
                                attendance.setGirlsPresent(dto.getFemalePresent());
                                attendance.setComment(dto.getComment());
                                return attendance;
                            })
                    ).toList();
            if (existingGeneralLearnerAttendances.size()>0){
                learnerAttendanceRepository.saveAll(existingGeneralLearnerAttendances);
            }
            log.info("existingGeneralLearnerAttendances {} " , existingGeneralLearnerAttendances.size());


            List<SNLearnerAttendance> existingSnLearnerAttendances = allSnLearnerAttendanceDTOS.parallelStream()
                    .flatMap(dto -> allExistingSnLearnerAttendanceList.parallelStream()
                            .filter(attendance -> dto.getClassId().equals(attendance.getSchoolClass().getId()))
                            .map(attendance -> {
                                attendance.setBoysAbsent(dto.getFemaleAbsent());
                                attendance.setGirlsAbsent(dto.getFemaleAbsent());
                                attendance.setBoysPresent(dto.getMalePresent());
                                attendance.setGirlsPresent(dto.getFemalePresent());
                                attendance.setComment(dto.getComment());
                                return attendance;
                            })
                    ).toList();
            if (existingSnLearnerAttendances.size()>0){
                snLearnerAttendanceRepository.saveAll(existingSnLearnerAttendances);
            }
            log.info("existingSnLearnerAttendances {} " , existingSnLearnerAttendances.size());


            try {
                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<List<LearnerAttendanceDTO>> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.LEARNER_ATTENDANCES);
                responseDto.setData(allClassAttendanceDTOS);
                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED LEARNER_ATTENDANCES  for {} {} {} ",academicTerm.getTerm(), idProjection.getId() ,  allClassAttendanceDTOS.size());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }

        }


    }



    @JmsListener(destination = "${queue.classes}")
    @Transactional
    public void subscribeClasses(String classesStr) throws JsonProcessingException {
        log.info("subscribeClasses {}  " , classesStr);
        SchoolDataPublishPayloadDTO<List<ClassDTO>> publishPayloadDTO = objectMapper.readValue(classesStr, new TypeReference<SchoolDataPublishPayloadDTO<List<ClassDTO>>>() {
        });

        List<ClassDTO> allClassDTOS =  publishPayloadDTO.getData();
        log.info("allClassDTOS {} " , allClassDTOS);

        AcademicTerm academicTerm = academicTermRepository.findById(publishPayloadDTO.getAcademicTerm()).orElseThrow(() -> new TelaNotFoundException("Term " + publishPayloadDTO.getAcademicTerm() + " not found"));

        Optional<IdProjection> optionalIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(publishPayloadDTO.getSchoolTelaNumber(), Status.DELETED);
        if (optionalIdProjection.isPresent()) {
            log.info("FOUND TELA NUMBER {} " , publishPayloadDTO.getSchoolTelaNumber());
            IdProjection idProjection = optionalIdProjection.get();

            List<SchoolClass> allExistingSchoolClasses = schoolClassRepository.findAllByStatusNotAndAcademicTerm_IdAndSchool_Id(Status.DELETED, publishPayloadDTO.getAcademicTerm() , idProjection.getId());

            // process new general learners
            List<ClassDTO> allNewClassDTOS = allClassDTOS.parallelStream().filter(dto -> dto.getId().equalsIgnoreCase("")).toList();
            List<ClassDTO> allExistingClassDTOS = allClassDTOS.parallelStream().filter(dto -> !dto.getId().equalsIgnoreCase("")).toList();

            List<ClassDTO> allSavedNewClassDTOS = allNewClassDTOS.parallelStream().map(classDTO -> {
                SchoolClass schoolClass = SchoolClass.builder()
                        .status(Status.ACTIVE)
                        .name(classDTO.getName())
                        .code(classDTO.getName())
                        .classLevel(false)
                        .hasStreams(false)
                        .school(new School(idProjection.getId()))
                        .academicTerm(academicTerm)
                        .build();

                if (classDTO.getParentSchoolClassId() != null && !classDTO.getParentSchoolClassId().isEmpty()) {

                    SchoolClass parentSchoolClass = schoolClassRepository.findById(classDTO.getParentSchoolClassId()).orElseThrow(() -> new TelaNotFoundException("parent class not found"));
                    schoolClass.setParentSchoolClass(parentSchoolClass);
                    parentSchoolClass.setHasStreams(true);
                    parentSchoolClass.setClassLevel(true);

                }

                SchoolClass save = schoolClassRepository.save(schoolClass);
                classDTO.setId(save.getId());

                return classDTO;
            }).collect(Collectors.toList());


            // process existing
            List<SchoolClass> allExistingClasses = allExistingClassDTOS.parallelStream()
                    .flatMap(dto -> allExistingSchoolClasses.parallelStream()
                            .filter(schoolClass -> dto.getId().equals(schoolClass.getId()))
                            .map(schoolClass -> {
                                schoolClass.setName(schoolClass.getName());
                                return schoolClass;
                            })
                    ).toList();
            if (allExistingClasses.size()>0){
                schoolClassRepository.saveAll(allExistingClasses);
            }

            allSavedNewClassDTOS.addAll(allExistingClassDTOS);
            log.info("allExistingClasses {} " , allExistingClasses.size());


            try {
                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<List<ClassDTO>> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.CLASSES);
                responseDto.setData(allSavedNewClassDTOS);
                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED CLASSES  for {} {} {} ",academicTerm.getTerm(), idProjection.getId() ,  allSavedNewClassDTOS.size());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }

        }


    }


    @JmsListener(destination = "${queue.staffs}")
    @Transactional
    public void subscribeStaffs(String staffStr) throws JsonProcessingException {

        try {
            log.info("subscribeStaffs {}  " , staffStr);
            SchoolDataPublishPayloadDTO<List<StaffDTO>> publishPayloadDTO = objectMapper.readValue(staffStr, new TypeReference<SchoolDataPublishPayloadDTO<List<StaffDTO>>>() {
            });


            List<StaffDTO> allStaffDTOS =  publishPayloadDTO.getData();
            log.info("allStaffDTOS {} " , allStaffDTOS.size());

            AcademicTerm academicTerm = academicTermRepository.findById(publishPayloadDTO.getAcademicTerm()).orElseThrow(() -> new TelaNotFoundException("Term " + publishPayloadDTO.getAcademicTerm() + " not found"));

            Optional<IdProjection> optionalIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(publishPayloadDTO.getSchoolTelaNumber(), Status.DELETED);
            if (optionalIdProjection.isPresent()) {
                log.info("FOUND TELA NUMBER {} ", publishPayloadDTO.getSchoolTelaNumber());
                IdProjection idProjection = optionalIdProjection.get();

                List<SchoolStaff> allExistingStaffs = schoolStaffRepository.findAllBySchoolWithSchool_StaffDetail(Status.DELETED, idProjection.getId());

                // process new general learners
                List<StaffDTO> allNewStaffDTOS = allStaffDTOS.parallelStream().filter(dto -> dto.getId().equalsIgnoreCase("")).toList();
                List<StaffDTO> allExistingStaffDTOS = allStaffDTOS.parallelStream().filter(dto -> !dto.getId().equalsIgnoreCase("")).toList();

                List<StaffDTO> allSavedNewStaffDTOS = allNewStaffDTOS.parallelStream().map(dto -> {

                    Optional<StaffType> staffTypeOptional = StaffType.fromString(dto.getStaffType());
                    SchoolStaff schoolStaff = SchoolStaff.builder()
                            .status(Status.ACTIVE)
                            .school(new School(idProjection.getId()))
                            .staffCode(dto.getEmployeeNumber())
                            .staffType(staffTypeOptional.isPresent() ? staffTypeOptional.get() : StaffType.TEACHER)
                            .registered(dto.getOnPayRoll().equalsIgnoreCase("Yes") ? true : false)
                            .teachingstaff(dto.getStaffType().equalsIgnoreCase(StaffType.TEACHER.getType()) ? true : false)
                            .nationality("Ugandan")
                            .staffInServiceStatus(StaffInServiceStatus.ACTIVE)
                            .specialNeeds(dto.getHasSpecialNeeds().equalsIgnoreCase("true") ? true : false)
                            .build();

                    if (dto.getExpectedHours() == 0) {
                        schoolStaff.setExpectedHours(8);
                    } else {
                        schoolStaff.setExpectedHours(dto.getExpectedHours());
                    }


                    Optional<Gender> optionalGender = Gender.fromString(dto.getGender());
                    GeneralUserDetail generalUserDetail = GeneralUserDetail.builder()
                            .email(dto.getEmailAddress())
                            .firstName(dto.getFirstName())
                            .lastName(dto.getLastName())
                            .gender(optionalGender.isPresent() ? optionalGender.get() : Gender.MALE)
                            .nameAbbrev(dto.getInitials())
                            .nationalId(dto.getNationalId())
                            .phoneNumber(dto.getPhoneNumber())
                            .build();
                    schoolStaff.setGeneralUserDetail(generalUserDetail);

                    SchoolStaff save = schoolStaffRepository.save(schoolStaff);
                    dto.setId(save.getId());

                    return dto;
                }).collect(Collectors.toList());


                // process existing
                List<SchoolStaff> allUpdatedExistingStaffs = allExistingStaffDTOS.parallelStream()
                        .flatMap(dto -> allExistingStaffs.parallelStream()
                                .filter(staff -> dto.getId().equals(staff.getId()))
                                .map(staff -> {
                                    Optional<Gender> optionalGender = Gender.fromString(dto.getGender());
                                    GeneralUserDetail generalUserDetail = GeneralUserDetail.builder()
                                            .id(staff.getGeneralUserDetail().getId())
                                            .email(dto.getEmailAddress())
                                            .firstName(dto.getFirstName())
                                            .lastName(dto.getLastName())
                                            .gender(optionalGender.isPresent() ? optionalGender.get() : Gender.MALE)
                                            .nameAbbrev(dto.getInitials())
                                            .nationalId(dto.getNationalId())
                                            .phoneNumber(dto.getPhoneNumber())
                                            .build();
                                    staff.setGeneralUserDetail(generalUserDetail);
                                    return staff;
                                })
                        ).toList();
                if (allUpdatedExistingStaffs.size() > 0) {
                    schoolStaffRepository.saveAll(allUpdatedExistingStaffs);
                }

                allSavedNewStaffDTOS.addAll(allExistingStaffDTOS);

                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<List<StaffDTO>> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.STAFFS);
                responseDto.setData(allSavedNewStaffDTOS);
                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED STAFFS  for {} {} {} ",academicTerm.getTerm(), idProjection.getId() ,  allSavedNewStaffDTOS.size());
            }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }

        }


    @JmsListener(destination = "${queue.staffDailyTimeAttendances}")
    @Transactional
    public void subscribeStaffDailyTimeAttendances(String staffStr) throws JsonProcessingException {

        try {
            log.info("subscribeStaffs {}  " , staffStr);
            SchoolDataPublishPayloadDTO<List<StaffDailyTimeAttendanceDTO>> publishPayloadDTO = objectMapper.readValue(staffStr, new TypeReference<>() {
            });


            List<StaffDailyTimeAttendanceDTO> allAttendanceDTOS =  publishPayloadDTO.getData();
            log.info("attendanceDTOS {} " , allAttendanceDTOS.size());

            AcademicTerm academicTerm = academicTermRepository.findById(publishPayloadDTO.getAcademicTerm()).orElseThrow(() -> new TelaNotFoundException("Term " + publishPayloadDTO.getAcademicTerm() + " not found"));

            Optional<IdProjection> optionalIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(publishPayloadDTO.getSchoolTelaNumber(), Status.DELETED);
            if (optionalIdProjection.isPresent()) {
                log.info("FOUND TELA NUMBER {} ", publishPayloadDTO.getSchoolTelaNumber());
                IdProjection idProjection = optionalIdProjection.get();

                List<StaffDailyAttendanceSupervision> existingTermStaffDailyAttendanceSupervisions = staffDailyAttendanceSupervisionRepository.allByTerm_School(academicTerm.getStartDate(), academicTerm.getEndDate(), idProjection.getId());


                // process new general learners
                List<StaffDailyTimeAttendanceDTO> allNewStaffDailyTimeAttendanceDTOS = allAttendanceDTOS.parallelStream().filter(dto -> dto.getId().equalsIgnoreCase("")).toList();
                List<StaffDailyTimeAttendanceDTO> allExistingStaffDailyTimeAttendanceDTOS = allAttendanceDTOS.parallelStream().filter(dto -> !dto.getId().equalsIgnoreCase("")).toList();

                /// new
                List<StaffDailyTimeAttendanceDTO> allSavedNewDailyTimeAttendanceDTOS = allNewStaffDailyTimeAttendanceDTOS.parallelStream().map(dto -> {
                    LocalDateTime localDateTime = LocalDateTime.parse(dto.getSupervisionDateTime(), TelaDatePattern.dateTimePattern24);

                    StaffDailyAttendanceSupervision staffDailyAttendanceSupervision = StaffDailyAttendanceSupervision.builder()
                            .supervisor(new SchoolStaff(dto.getSupervisorId()))
                            .schoolStaff(new SchoolStaff(dto.getStaffId()))
                            .comment(dto.getSupervisorComment())
                            .supervisionTime(localDateTime.toLocalTime())
                            .supervisionDate(localDateTime.toLocalDate())
                            .attendanceStatus(AttendanceStatus.fromString(dto.getSupervisionStatus()).get())
                            .status(Status.ACTIVE)
                            .build();

                    StaffDailyAttendanceSupervision save = staffDailyAttendanceSupervisionRepository.save(staffDailyAttendanceSupervision);
                    dto.setId(save.getId());

                    return dto;
                }).collect(Collectors.toList());


                // process existing
                List<StaffDailyAttendanceSupervision> allUpdatedExistingStaffDailyTimeAttendances = allExistingStaffDailyTimeAttendanceDTOS.parallelStream()
                        .flatMap(dto -> existingTermStaffDailyAttendanceSupervisions.parallelStream()
                                .filter(attendance -> dto.getId().equals(attendance.getId()))
                                .map(attendance -> {
                                    LocalDateTime localDateTime = LocalDateTime.parse(dto.getSupervisionDateTime(), TelaDatePattern.dateTimePattern24);
                                    attendance.setSupervisor(new SchoolStaff(dto.getSupervisorId()));
                                    attendance.setSchoolStaff(new SchoolStaff(dto.getStaffId()));
                                    attendance.setSupervisionTime(localDateTime.toLocalTime());
                                    attendance.setSupervisionDate(localDateTime.toLocalDate());
                                    attendance.setComment(dto.getSupervisorComment());
                                    attendance.setAttendanceStatus(AttendanceStatus.fromString(dto.getSupervisionStatus()).get());
                                    return attendance;
                                })
                        ).toList();
                if (allUpdatedExistingStaffDailyTimeAttendances.size() > 0) {
                    staffDailyAttendanceSupervisionRepository.saveAll(allUpdatedExistingStaffDailyTimeAttendances);
                }

                allSavedNewDailyTimeAttendanceDTOS.addAll(allExistingStaffDailyTimeAttendanceDTOS);

                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<List<StaffDailyTimeAttendanceDTO>> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.STAFF_DAILY_TIME_ATTENDANCES);
                responseDto.setData(allSavedNewDailyTimeAttendanceDTOS);
                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED STAFF_DAILY_TIME_ATTENDANCES  for {} {} {} ",academicTerm.getTerm(), idProjection.getId() ,  allSavedNewDailyTimeAttendanceDTOS.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }





}
