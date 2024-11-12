package com.planetsystems.tela.api.ClockInOutConsumer.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.*;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.IdProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.*;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.schoolData.SchoolDataPublishPayloadDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.supervision.StaffDailyAttendanceTaskSupervisionDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.StaffDailyTimetableDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.UpdateTimeTableLessonDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.exception.TelaNotFoundException;
import com.planetsystems.tela.api.ClockInOutConsumer.model.*;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.TelaDatePattern;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.publisher.QueueTopicPublisher;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.publisher.TelaQueueNames;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolDataConsumerServiceImpl implements SchoolDataConsumerService{

    final ObjectMapper objectMapper;
    final LearnerEnrollmentRepository learnerEnrollmentRepository;
    final SNLearnerEnrollmentRepository snLearnerEnrollmentRepository;
    final SchoolRepository schoolRepository;
    final LearnerAttendanceRepository learnerAttendanceRepository;
    final SNLearnerAttendanceRepository snLearnerAttendanceRepository;
    final SchoolClassRepository schoolClassRepository;
    final AcademicTermRepository academicTermRepository;
    final SchoolStaffRepository schoolStaffRepository;
    final GeneralUserDetailsRepository generalUserDetailsRepository;
    final StaffDailyAttendanceSupervisionRepository staffDailyAttendanceSupervisionRepository;
    final StaffDailyTimeTableRepository staffDailyTimeTableRepository;
    final StaffDailyTimeTableLessonRepository staffDailyTimeTableLessonRepository;
    final StaffDailyAttendanceTaskSupervisionRepository staffDailyAttendanceTaskSupervisionRepository;

    final TimeTableLessonRepository timeTableLessonRepository;

    final SchoolGeoCoordinateRepository schoolGeoCoordinateRepository;
    private final ClockInRepository clockInRepository;
    private final ClockOutRepository clockOutRepository;

//    final JmsTemplate jmsTemplate;
    private final QueueTopicPublisher queueTopicPublisher;



    @JmsListener(destination = "${queue.clockins}")
    @Transactional
    public void subscribeClockIns(String clockInsStr) throws JsonProcessingException {
        log.info("subscribeClockIns1");
        List<ClockInDTO> dtoList = objectMapper.readValue(clockInsStr, new TypeReference<>() {
        });

        Optional<ClockInDTO> firstOptional = dtoList.parallelStream().findFirst();
        if (firstOptional.isPresent()) {
            ClockInDTO firstDTO = firstOptional.get();

            IdProjection idProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(firstDTO.getTelaSchoolNumber(), Status.DELETED).orElseThrow(() -> new TelaNotFoundException("School not found"));

            // new
            List<ClockInDTO> newSavedClockInDTOS = dtoList.parallelStream()
                    .filter(dto -> dto.getId().equals(null) || dto.getId().isEmpty())
                    .map(dto -> {
                        LocalDateTime clockInDateTime = LocalDateTime.parse(dto.getClockInDateTime(), TelaDatePattern.dateTimePattern24);
                        Optional<ClockIn> optionalClockIn = clockInRepository.clockInByDate_Staff(clockInDateTime.toLocalDate(), dto.getStaffId());
                        if (optionalClockIn.isPresent()) {
                            // todo compare clockin times
                            ClockIn clock = optionalClockIn.get();
                            boolean after = clock.getClockInTime().isAfter(clockInDateTime.toLocalTime());
                            if (after){
                                clock.setClockInTime(clockInDateTime.toLocalTime());
                                clockInRepository.save(clock);
                            }
                            dto.setId(clock.getId());
                        }else{
                            ClockIn clockIn = toNewClockIn(dto, clockInDateTime, idProjection);
                            ClockIn save = clockInRepository.save(clockIn);
                            dto.setId(save.getId());
                        }
                        return dto;
                    }).collect(Collectors.toList());

            Optional<ClockInDTO> clockInDTOOptional = dtoList.parallelStream().findAny();


            /// existing
            List<ClockInDTO> existingSavedClockInDTOS = Collections.emptyList();
            if (clockInDTOOptional.isPresent()) {
                ClockInDTO clockInDTO = clockInDTOOptional.get();
                LocalDateTime clockIDateTime = LocalDateTime.parse(clockInDTO.getClockInDateTime(), TelaDatePattern.dateTimePattern24);
                List<ClockIn> dateClockIns = clockInRepository.clockInByDate(clockIDateTime.toLocalDate());


                existingSavedClockInDTOS = dateClockIns.parallelStream().flatMap(clockIn -> dtoList.parallelStream()
                        .filter(dto -> clockIn.getSchoolStaff().getId().equals(dto.getStaffId()))
                        .map(dto -> {
                            LocalDateTime clockInDateTime = LocalDateTime.parse(dto.getClockInDateTime(), TelaDatePattern.dateTimePattern24);
                            Optional<ClockIn> optionalClockIn = clockInRepository.clockInByDate_Staff(clockInDateTime.toLocalDate(), dto.getStaffId());
                            if (optionalClockIn.isPresent()) {
                                // todo compare clockin times
                                ClockIn clock = optionalClockIn.get();
                                boolean after = clock.getClockInTime().isAfter(clockInDateTime.toLocalTime());
                                if (after) {
                                    clock.setClockInTime(clockInDateTime.toLocalTime());
                                    clockInRepository.save(clock);
                                }
                            }
                            return dto;
                        })).toList();


//                List<ClockInDTO> existingSavedClockInDTOS = dtoList.parallelStream()
//                        .filter(dto -> (dto.getId() != null  && !dto.getId().isEmpty()))
//                        .map(dto -> {
//                            LocalDateTime clockInDateTime = LocalDateTime.parse(dto.getClockInDateTime(), TelaDatePattern.dateTimePattern24);
//                            Optional<ClockIn> optionalClockIn = clockInRepository.clockInByDate_Staff(clockInDateTime.toLocalDate(), dto.getStaffId());
//                            if (optionalClockIn.isPresent()) {
//                                // todo compare clockin times
//                                ClockIn clock = optionalClockIn.get();
//                                boolean after = clock.getClockInTime().isAfter(clockInDateTime.toLocalTime());
//                                if (after){
//                                    clock.setClockInTime(clockInDateTime.toLocalTime());
//                                    clockInRepository.save(clock);
//                                }
//                            }
//                            return dto;
//                        }).toList();

            }







            newSavedClockInDTOS.addAll(existingSavedClockInDTOS);

            publishSchoolClockIns(firstDTO.getTelaSchoolNumber() , newSavedClockInDTOS);

        }

    }


    @Async
    public void publishSchoolClockIns(String telaSchoolNumber ,  List<ClockInDTO> dtoList) {
        try {
//            jmsTemplate.setPubSubDomain(true);
//            jmsTemplate.setConnectionFactory(topicConnectionFactory);
//            jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
            MQResponseDto<List<ClockInDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.CLOCKINS);
            responseDto.setData(dtoList);
//            jmsTemplate.convertAndSend(telaSchoolNumber, objectMapper.writeValueAsString(responseDto));


            queueTopicPublisher.publishTopicData(telaSchoolNumber , objectMapper.writeValueAsString(responseDto));

            log.info("PUBLISHED SAVE UPDATED CLOCKINS FOR {} {} " , telaSchoolNumber , dtoList.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }


    @Async
    public void publishSchoolClockOuts(String telaSchoolNumber ,  List<ClockOutDTO> dtoList) {
        try {
//            jmsTemplate.setPubSubDomain(true);
//            jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
            MQResponseDto<List<ClockOutDTO>> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.CLOCKOUTS);
            responseDto.setData(dtoList);
//            jmsTemplate.convertAndSend(telaSchoolNumber, objectMapper.writeValueAsString(responseDto));
            queueTopicPublisher.publishTopicData(telaSchoolNumber , objectMapper.writeValueAsString(responseDto));
            log.info("PUBLISHED SAVE UPDATED CLOCKOUTS FOR {} {} " , telaSchoolNumber , dtoList.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }


    @JmsListener(destination = "${queue.clockouts}" )
    @Transactional
    public void subscribeClockOuts(String clockOutsStr) throws JsonProcessingException {
        log.info("subscribeClockOuts1");
        List<ClockOutDTO> dtoList = objectMapper.readValue(clockOutsStr, new TypeReference<>() {
        });

        Optional<ClockOutDTO> firstOptional = dtoList.parallelStream().findFirst();
        if (firstOptional.isPresent()) {
            ClockOutDTO firstDTO = firstOptional.get();
            log.info("subscribeClockOuts {} {} " , dtoList.size() , dtoList);

            /// new

            List<ClockOutDTO> newSavedClockOutDTOS = dtoList.parallelStream()
                    .filter(dto -> dto.getId().equals(null) || dto.getId().isEmpty())
                    .map(dto -> {
                        LocalDateTime clockOutDateTime = LocalDateTime.parse(dto.getClockOutDateTime(), TelaDatePattern.dateTimePattern24);
                        Optional<ClockOut> optionalClockOut = clockOutRepository.clockOutByDate_Staff(clockOutDateTime.toLocalDate(), dto.getStaffId());
                        if (optionalClockOut.isPresent()) {
                            // todo compare clockout times
                            ClockOut clockOut = optionalClockOut.get();
                            boolean after = clockOut.getClockOutTime().isAfter(clockOutDateTime.toLocalTime());
                            if (after){
                                clockOut.setClockOutTime(clockOutDateTime.toLocalTime());
                                clockOutRepository.save(clockOut);
                            }
                            dto.setId(clockOut.getId());
                        }else{
                            ClockOut clockOut = toNewClockOut(dto, clockOutDateTime);
                            ClockOut save = clockOutRepository.save(clockOut);
                            dto.setId(save.getId());
                        }
                        return dto;
                    }).collect(Collectors.toList());


            /// existing
            List<ClockOutDTO> existingSavedClockOutDTOS = dtoList.parallelStream()
                    .filter(dto -> (dto.getId() != null  && !dto.getId().isEmpty()))
                    .map(dto -> {
                        LocalDateTime clockOutDateTime = LocalDateTime.parse(dto.getClockOutDateTime(), TelaDatePattern.dateTimePattern24);
                        Optional<ClockOut> optionalClockOut = clockOutRepository.clockOutByDate_Staff(clockOutDateTime.toLocalDate(), dto.getStaffId());
                        if (optionalClockOut.isPresent()) {
                            // todo compare clockin times
                            ClockOut clockOut = optionalClockOut.get();
                            boolean after = clockOut.getClockOutTime().isAfter(clockOutDateTime.toLocalTime());
                            if (after){
                                clockOut.setClockOutTime(clockOutDateTime.toLocalTime());
                                clockOutRepository.save(clockOut);
                            }
                        }
                        return dto;
                    }).toList();


            newSavedClockOutDTOS.addAll(existingSavedClockOutDTOS);

            publishSchoolClockOuts(firstDTO.getTelaSchoolNumber() , newSavedClockOutDTOS);
        }










//        dtoList.parallelStream().filter(dto -> {
//            LocalDateTime clockInDateTime = LocalDateTime.parse(dto.getClockInDateTime(), TelaDatePattern.dateTimePattern24);
//            boolean alreadyExists = !clockInRepository.existsByStatusNotAndClockInDateAndSchoolStaff_Id(Status.DELETED, clockInDateTime.toLocalDate(), dto.getStaffId());
//            if (alreadyExists){
//                log.info("alreadyExists {} ", dto);
//                publishSchoolClockIn(dto);
//            }
//            return alreadyExists;
//        }).forEach(dto -> {
//            LocalDateTime clockInDateTime = LocalDateTime.parse(dto.getClockInDateTime(), TelaDatePattern.dateTimePattern24);
//            Optional<IdProjection> optionalSchoolIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(dto.getTelaSchoolNumber() , Status.DELETED);
//
//            if (optionalSchoolIdProjection.isPresent()) {
//                IdProjection schoolIdProjection = optionalSchoolIdProjection.get();
//                ClockIn clockIn = toNewClockIn(dto, clockInDateTime, schoolIdProjection);
//
//                log.info("CLOCKIN TO BE SAVED {}  " , dto);
//                log.info("TELA NO {}  " , dto.getTelaSchoolNumber());
//
//                ClockIn save = clockInRepository.save(clockIn);
//                dto.setId(save.getId());
//                publishSchoolClockIns( dto);
//            }
//        });
    }


    private  ClockIn toNewClockIn(ClockInDTO dto, LocalDateTime clockInDateTime, IdProjection schoolIdProjection) {
        return ClockIn.builder()
                .clockedStatus(ClockedStatus.CLOCKED_IN)
                .clockInDate(clockInDateTime.toLocalDate())
                .clockInTime(clockInDateTime.toLocalTime())
                .academicTerm(new AcademicTerm(dto.getAcademicTermId()))
                .school(new School(schoolIdProjection.getId()))
                .schoolStaff(new SchoolStaff(dto.getStaffId()))
                .comment("")
                .clockinType(dto.getClockInType())
                .displacement(dto.getDisplacement())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .status(Status.ACTIVE)
                .build();
    }
    private ClockOut toNewClockOut(ClockOutDTO dto, LocalDateTime clockOutDateTime) {
        return ClockOut.builder()
                .status(Status.ACTIVE)
                .clockIn(new ClockIn(dto.getClockInId()))
                .clockOutDate(clockOutDateTime.toLocalDate())
                .clockOutTime(clockOutDateTime.toLocalTime())
                .comment(dto.getReason())
                .clockedStatus(ClockedStatus.CLOCKED_OUT)
                .clockOutType(dto.getClockOutType())
                .displacement((int) dto.getDisplacement())
                .build();

    }


    @JmsListener(destination = "${queue.learnerHeadCounts}")
    @Transactional
    @Override
    public void subscribeLearnerHeadCounts(String learnerHeadCountStr , Message message) throws JsonProcessingException {
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
                                .status(Status.ACTIVE)
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
                                .status(Status.ACTIVE)
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
                                enrollment.setStatus(Status.ACTIVE);
                                enrollment.setTotalBoys(dto.getTotalMale());
                                enrollment.setTotalGirls(dto.getTotalFemale());
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
                                enrollment.setStatus(Status.ACTIVE);
                                enrollment.setTotalBoys(dto.getTotalMale());
                                enrollment.setTotalGirls(dto.getTotalFemale());
                                return enrollment;
                            })
                    ).toList();
            if (existingSnLearnerEnrollments.size()>0){
                snLearnerEnrollmentRepository.saveAll(existingSnLearnerEnrollments);
            }
            log.info("existingSnLearnerEnrollment {} " , existingSnLearnerEnrollments.size());
            try {
//                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<List<LearnerHeadCountDTO>> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.LEARNER_HEADCOUNTS);
                responseDto.setData(allLearnerHeadCountDTOS);
//                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                queueTopicPublisher.publishTopicData(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED LEARNER_HEADCOUNTS  for {} {} {} ",academicTerm.getTerm(), idProjection.getId() ,  allLearnerHeadCountDTOS.size());
                queueTopicPublisher.deleteMessageFromQueue(TelaQueueNames.LearnerHeadCounts , message);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }

        }


    }


    @JmsListener(destination = "${queue.classAttendances}" )
    @Transactional
    @Override
    public void subscribeClassAttendances(String classAttendanceStr, Message message) throws JsonProcessingException {
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
//                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<List<LearnerAttendanceDTO>> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.LEARNER_ATTENDANCES);
                responseDto.setData(allClassAttendanceDTOS);
//                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                queueTopicPublisher.publishTopicData(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED LEARNER_ATTENDANCES  for {} {} {} ",academicTerm.getTerm(), idProjection.getId() ,  allClassAttendanceDTOS.size());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }

        }


    }



    @JmsListener(destination = "${queue.classes}" )
    @Transactional
    @Override
    public void subscribeClasses(String classesStr, Message message) throws JsonProcessingException {
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
//                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<List<ClassDTO>> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.CLASSES);
                responseDto.setData(allSavedNewClassDTOS);
//                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                queueTopicPublisher.publishTopicData(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED CLASSES  for {} {} {} ",academicTerm.getTerm(), idProjection.getId() ,  allSavedNewClassDTOS.size());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }

        }


    }


    @JmsListener(destination = "${queue.staffs}" )
    @Transactional
    @Override
    public void subscribeStaffs(String staffStr, Message message) throws JsonProcessingException {

        try {
            SchoolDataPublishPayloadDTO<List<StaffDTO>> publishPayloadDTO = objectMapper.readValue(staffStr, new TypeReference<SchoolDataPublishPayloadDTO<List<StaffDTO>>>() {
            });


            List<StaffDTO> allStaffDTOS =  publishPayloadDTO.getData();

            AcademicTerm academicTerm = academicTermRepository.findById(publishPayloadDTO.getAcademicTerm()).orElseThrow(() -> new TelaNotFoundException("Term " + publishPayloadDTO.getAcademicTerm() + " not found"));

            Optional<IdProjection> optionalIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(publishPayloadDTO.getSchoolTelaNumber(), Status.DELETED);
            if (optionalIdProjection.isPresent()) {
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
                            .schoolStaff(schoolStaff)
                            .status(Status.ACTIVE)
                            .build();
                    schoolStaff.setGeneralUserDetail(generalUserDetail);

                    SchoolStaff save = schoolStaffRepository.save(schoolStaff);
                    dto.setId(save.getId());

                    return dto;
                }).collect(Collectors.toList());

                List<SchoolStaff> allUpdatedExistingStaffDetails = allExistingStaffDTOS.parallelStream()
                        .flatMap(dto -> allExistingStaffs.parallelStream()
                                .filter(staff -> dto.getId().equals(staff.getId()))
                                .map(staff -> {
                                    staff.setStaffType(StaffType.fromString(dto.getRole()).get());
                                    staff.setTeachingstaff(dto.getStaffType().equalsIgnoreCase("Teaching") ? true : false);

                                    Optional<Gender> optionalGender = Gender.fromString(dto.getGender());
                                    GeneralUserDetail generalUserDetail = staff.getGeneralUserDetail();
                                    generalUserDetail.setStatus(Status.ACTIVE);
                                    generalUserDetail.setEmail(dto.getEmailAddress());
                                    generalUserDetail.setFirstName(dto.getFirstName());
                                    generalUserDetail.setLastName(dto.getLastName());
                                    generalUserDetail.setNameAbbrev(dto.getInitials());
                                    generalUserDetail.setNationalId(dto.getNationalId());
                                    generalUserDetail.setGender(optionalGender.isPresent() ? optionalGender.get() : Gender.MALE);
                                    generalUserDetail.setPhoneNumber(dto.getPhoneNumber());
                                    staff.setGeneralUserDetail(generalUserDetail);
                                    return staff;
                                })
                        ).toList();
                if (allUpdatedExistingStaffDetails.size() > 0) {
                    schoolStaffRepository.saveAll(allUpdatedExistingStaffDetails);
                }

                allSavedNewStaffDTOS.addAll(allExistingStaffDTOS);

//                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<List<StaffDTO>> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.STAFFS);
                responseDto.setData(allSavedNewStaffDTOS);
//                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                queueTopicPublisher.publishTopicData(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED STAFFS  for {} {} {} ",academicTerm.getTerm(), idProjection.getId() ,  allSavedNewStaffDTOS.size());
            }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }

        }


    @JmsListener(destination = "${queue.staffDailyTimeAttendances}" )
    @Transactional
    @Override
    public void subscribeStaffDailyTimeAttendances(String staffStr, Message message) {

        try {
            SchoolDataPublishPayloadDTO<List<StaffDailyTimeAttendanceDTO>> publishPayloadDTO = objectMapper.readValue(staffStr, new TypeReference<>() {
            });


            List<StaffDailyTimeAttendanceDTO> allAttendanceDTOS =  publishPayloadDTO.getData();

            AcademicTerm academicTerm = academicTermRepository.findById(publishPayloadDTO.getAcademicTerm()).orElseThrow(() -> new TelaNotFoundException("Term " + publishPayloadDTO.getAcademicTerm() + " not found"));

            Optional<IdProjection> optionalIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(publishPayloadDTO.getSchoolTelaNumber(), Status.DELETED);
            if (optionalIdProjection.isPresent()) {
                IdProjection idProjection = optionalIdProjection.get();

                List<StaffDailyAttendanceSupervision> existingTermStaffDailyAttendanceSupervisions = staffDailyAttendanceSupervisionRepository
                        .allByTerm_School(academicTerm.getStartDate(), academicTerm.getEndDate(), idProjection.getId());


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

//                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<List<StaffDailyTimeAttendanceDTO>> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.STAFF_DAILY_TIME_ATTENDANCES);
                responseDto.setData(allSavedNewDailyTimeAttendanceDTOS);
//                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                queueTopicPublisher.publishTopicData(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED STAFF_DAILY_TIME_ATTENDANCES  for {} {} {} ",academicTerm.getTerm(), idProjection.getId() ,  allSavedNewDailyTimeAttendanceDTOS.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    @JmsListener(destination = "${queue.updateTimetableLessons}" )
    @Transactional
    @Override
    public void subscribeUpdateTimetableLessons(String updateTimetableLessonStr , Message message) {
        try {
            SchoolDataPublishPayloadDTO<List<UpdateTimeTableLessonDTO>> publishPayloadDTO = objectMapper.readValue(updateTimetableLessonStr, new TypeReference<>() {
            });


            List<UpdateTimeTableLessonDTO> allUpdateTimeTableLessonDTOS =  publishPayloadDTO.getData();
            Optional<UpdateTimeTableLessonDTO> firstOptional = allUpdateTimeTableLessonDTOS.parallelStream().findFirst();

            AcademicTerm academicTerm = academicTermRepository.findById(publishPayloadDTO.getAcademicTerm()).orElseThrow(() -> new TelaNotFoundException("Term " + publishPayloadDTO.getAcademicTerm() + " not found"));

            Optional<IdProjection> optionalIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(publishPayloadDTO.getSchoolTelaNumber(), Status.DELETED);

            if (optionalIdProjection.isPresent() && firstOptional.isPresent()) {
                IdProjection idProjection = optionalIdProjection.get();
                UpdateTimeTableLessonDTO firstDTO = firstOptional.get();

                List<TimeTableLesson> allClassTimeTableLessons = timeTableLessonRepository.allByTerm_School_Class(academicTerm.getId(), idProjection.getId(),firstDTO.getClassId() );
                // todo get all class subjects for that term

                List<TimeTableLesson> updatedTimeTableLessons = allUpdateTimeTableLessonDTOS.parallelStream().flatMap(dto -> allClassTimeTableLessons.parallelStream().filter(lesson -> dto.getId().equalsIgnoreCase(lesson.getId())).map(lesson -> {
                    lesson.setSchoolStaff(new SchoolStaff(dto.getStaffId()));
                    lesson.setSubject(new Subject(dto.getSubjectId()));
                    return lesson;
                })).toList();

                timeTableLessonRepository.saveAll(updatedTimeTableLessons);

//                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<List<UpdateTimeTableLessonDTO>> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.UPDATE_TIMETABLE_LESSONS);
                responseDto.setData(allUpdateTimeTableLessonDTOS);
//                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                queueTopicPublisher.publishTopicData(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED UPDATE_TIMETABLE_LESSONS  for {} {} {} ",academicTerm.getTerm(), idProjection.getId() ,  allUpdateTimeTableLessonDTOS.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @JmsListener(destination = "${queue.staffDailyTimetables}" )
    @Transactional
    @Override
    public void subscribeStaffDailyTimetables(String staffDailyTimetableStr, Message message) throws JsonProcessingException {
        try {
            SchoolDataPublishPayloadDTO<List<StaffDailyTimetableDTO>> publishPayloadDTO = objectMapper.readValue(staffDailyTimetableStr, new TypeReference<>() {
            });


            List<StaffDailyTimetableDTO> allStaffDailyTimetableDTOS =  publishPayloadDTO.getData();

            AcademicTerm academicTerm = academicTermRepository.findById(publishPayloadDTO.getAcademicTerm()).orElseThrow(() -> new TelaNotFoundException("Term " + publishPayloadDTO.getAcademicTerm() + " not found"));

            Optional<IdProjection> optionalIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(publishPayloadDTO.getSchoolTelaNumber(), Status.DELETED);

            if (optionalIdProjection.isPresent()) {
                IdProjection idProjection = optionalIdProjection.get();

                List<StaffDailyTimeTable> existingTermStaffDailyAttendanceSupervisions = staffDailyTimeTableRepository
                        .allByTerm_School(academicTerm.getId(), idProjection.getId());
                List<StaffDailyTimeTableLesson> existingStaffDailyTimeTableLessons = staffDailyTimeTableLessonRepository.allIn(existingTermStaffDailyAttendanceSupervisions);


                // process new general learners
                List<StaffDailyTimetableDTO> allNewStaffDailyTimetableDTOS = allStaffDailyTimetableDTOS.parallelStream().filter(dto -> dto.getId().equalsIgnoreCase("")).toList();
                List<StaffDailyTimetableDTO> allExistingStaffDailyTimetableDTOS = allStaffDailyTimetableDTOS.parallelStream().filter(dto -> !dto.getId().equalsIgnoreCase("")).toList();

                /// new
                List<StaffDailyTimetableDTO> allSavedNewStaffDailyTimetableDTOS = allNewStaffDailyTimetableDTOS.parallelStream().map(dto -> {

                    StaffDailyTimeTable staffDailyTimeTable = StaffDailyTimeTable.builder()
                            .lessonDate(LocalDate.parse(dto.getSubmissionDate() , TelaDatePattern.datePattern))
                            .academicTerm(new AcademicTerm(publishPayloadDTO.getAcademicTerm()))
                            .schoolStaff(new SchoolStaff(dto.getStaffId()))
                            .comment(dto.getComment())
                            .status(Status.ACTIVE)
                            .build();

                    String[] StartTimes = dto.getStartTime().split(":");
                    String[] EndTimes = dto.getEndTime().split(":");

                    StaffDailyTimeTableLesson staffDailyTimeTableLesson = StaffDailyTimeTableLesson.builder()
                            .dailyTimeTableLessonStatus(AttendanceStatus.fromString(dto.getActionStatus()).get())
                            .lessonDate(LocalDate.parse(dto.getSubmissionDate() , TelaDatePattern.datePattern))
                            .schoolClass(new SchoolClass(dto.getClassId()))
                            .subject(new Subject(dto.getSubjectId()))
//                            .startTime(LocalTime.parse(dto.getStartTime() , TelaDatePattern.timePattern24))
//                            .endTime(LocalTime.parse(dto.getEndTime() , TelaDatePattern.timePattern24))
                            .staffDailyTimeTable(staffDailyTimeTable)
                            .status(Status.ACTIVE)
                            .build();

                    if (StartTimes.length == 3){
                        staffDailyTimeTableLesson.setStartTime(LocalTime.parse(dto.getStartTime() , TelaDatePattern.timePattern24));
                    }else{
                        staffDailyTimeTableLesson.setStartTime(LocalTime.parse(dto.getStartTime() , TelaDatePattern.HourMinutePattern24));
                    }

                    if (EndTimes.length == 3){
                        staffDailyTimeTableLesson.setEndTime(LocalTime.parse(dto.getStartTime() , TelaDatePattern.timePattern24));
                    }else{
                        staffDailyTimeTableLesson.setEndTime(LocalTime.parse(dto.getStartTime() , TelaDatePattern.HourMinutePattern24));
                    }

                    StaffDailyTimeTableLesson save = staffDailyTimeTableLessonRepository.save(staffDailyTimeTableLesson);
                    dto.setId(save.getId());

                    return dto;
                }).collect(Collectors.toList());


                // process existing
                List<StaffDailyTimeTableLesson> allUpdatedExistingStaffDailyTimetables = allExistingStaffDailyTimetableDTOS.parallelStream()
                        .flatMap(dto -> existingStaffDailyTimeTableLessons.parallelStream()
                                .filter(dailyTimeTable -> dto.getId().equals(dailyTimeTable.getId()))
                                .map(dailyTimeTable -> {
                                  dailyTimeTable.setSubject(new Subject(dto.getSubjectId()));
                                  dailyTimeTable.setEndTime(LocalTime.parse(dto.getEndTime() , TelaDatePattern.timePattern24));
                                  dailyTimeTable.setStartTime(LocalTime.parse(dto.getStartTime() , TelaDatePattern.timePattern24));
                                  dailyTimeTable.setSchoolClass(new SchoolClass(dto.getClassId()));
                                  dailyTimeTable.setLessonDate(LocalDate.parse(dto.getSubmissionDate() , TelaDatePattern.datePattern));
                                  dailyTimeTable.setDailyTimeTableLessonStatus(AttendanceStatus.fromString(dto.getActionStatus()).get());
                                    return dailyTimeTable;
                                })
                        ).toList();

                if (allUpdatedExistingStaffDailyTimetables.size() > 0) {
                    staffDailyTimeTableLessonRepository.saveAll(allUpdatedExistingStaffDailyTimetables);
                }

                allSavedNewStaffDailyTimetableDTOS.addAll(allExistingStaffDailyTimetableDTOS);

//                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<List<StaffDailyTimetableDTO>> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.STAFF_DAILY_TIMETABLES);
                responseDto.setData(allSavedNewStaffDailyTimetableDTOS);
//                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                queueTopicPublisher.publishTopicData(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED STAFF_DAILY_TIMETABLES  for {} {} {} ",academicTerm.getTerm(), idProjection.getId() ,  allSavedNewStaffDailyTimetableDTOS.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }


    @JmsListener(destination = "${queue.staffDailyTaskSupervisions}" )
    @Transactional
    @Override
    public void subscribeStaffDailyTaskSupervisions(String staffDailyTaskSupervisionStr, Message message) throws JsonProcessingException {
        try {
            SchoolDataPublishPayloadDTO<List<StaffDailyAttendanceTaskSupervisionDTO>> publishPayloadDTO = objectMapper.readValue(staffDailyTaskSupervisionStr, new TypeReference<>() {
            });
            List<StaffDailyAttendanceTaskSupervisionDTO> allStaffDailyTaskSupervisionDTOS = publishPayloadDTO.getData();
            AcademicTerm academicTerm = academicTermRepository.findById(publishPayloadDTO.getAcademicTerm()).orElseThrow(() -> new TelaNotFoundException("Term " + publishPayloadDTO.getAcademicTerm() + " not found"));

            Optional<IdProjection> optionalIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(publishPayloadDTO.getSchoolTelaNumber(), Status.DELETED);

            if (optionalIdProjection.isPresent()) {
                IdProjection idProjection = optionalIdProjection.get();

                List<StaffDailyAttendanceSupervision> existingTermStaffDailyTimeSupervisions = staffDailyAttendanceSupervisionRepository
                        .allByTerm_School(academicTerm.getStartDate() , academicTerm.getEndDate(), idProjection.getId());

                List<StaffDailyAttendanceTaskSupervision> existingTermStaffDailyTaskSupervisions = staffDailyAttendanceTaskSupervisionRepository.allIn(existingTermStaffDailyTimeSupervisions);


                // process new task supervision
                List<StaffDailyAttendanceTaskSupervisionDTO> allNewStaffDailyAttendanceTaskSupervisionDTO = allStaffDailyTaskSupervisionDTOS
                        .parallelStream().filter(dto -> dto.getId().equalsIgnoreCase("")).toList();
                List<StaffDailyAttendanceTaskSupervisionDTO> allExistingStaffDailyAttendanceTaskSupervisionDTOS = allStaffDailyTaskSupervisionDTOS.parallelStream()
                        .filter(dto -> !dto.getId().equalsIgnoreCase("")).toList();

                /// new
                List<StaffDailyAttendanceTaskSupervisionDTO> allSavedNewStaffDailyAttendanceTaskSupervisionDTOS = allNewStaffDailyAttendanceTaskSupervisionDTO.parallelStream().map(dto -> {
                    StaffDailyAttendanceTaskSupervision taskSupervision = StaffDailyAttendanceTaskSupervision.builder()
                            .staffDailyAttendanceSupervision(new StaffDailyAttendanceSupervision(dto.getTimeAttendanceId()))
                            .staffDailyTimeTableLesson(new StaffDailyTimeTableLesson(dto.getLessonTaskId()))
                            .teachingTimeStatus(SupervisionStatus.fromString(dto.getTimeStatus()).get())
                            .comment(dto.getSupervisionComment())
                            .status(Status.ACTIVE)
                            .build();

                    StaffDailyAttendanceTaskSupervision save = staffDailyAttendanceTaskSupervisionRepository.save(taskSupervision);
                    dto.setId(save.getId());

                    return dto;
                }).collect(Collectors.toList());


                // process existing
                List<StaffDailyAttendanceTaskSupervision> allUpdatedExistingStaffDailyAttendanceTaskSupervision = allExistingStaffDailyAttendanceTaskSupervisionDTOS.parallelStream()
                        .flatMap(dto -> existingTermStaffDailyTaskSupervisions.parallelStream()
                                .filter(taskSupervision -> dto.getId().equals(taskSupervision.getId()))
                                .map(taskSupervision -> {
                                    taskSupervision.setComment(dto.getSupervisionComment());
                                    taskSupervision.setTeachingTimeStatus(SupervisionStatus.fromString(dto.getTimeStatus()).get());
                                    return taskSupervision;
                                })
                        ).toList();

                if (allUpdatedExistingStaffDailyAttendanceTaskSupervision.size() > 0) {
                    staffDailyAttendanceTaskSupervisionRepository.saveAll(allUpdatedExistingStaffDailyAttendanceTaskSupervision);
                }

                allSavedNewStaffDailyAttendanceTaskSupervisionDTOS.addAll(allExistingStaffDailyAttendanceTaskSupervisionDTOS);

//                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<List<StaffDailyAttendanceTaskSupervisionDTO>> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.STAFF_DAILY_TASK_SUPERVISIONS);
                responseDto.setData(allSavedNewStaffDailyAttendanceTaskSupervisionDTOS);
//                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                queueTopicPublisher.publishTopicData(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED STAFF_DAILY_TASK_SUPERVISIONS  for {} {} {} ",academicTerm.getTerm(), idProjection.getId() ,  allSavedNewStaffDailyAttendanceTaskSupervisionDTOS.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @JmsListener(destination = "${queue.schoolCoordinate}" )
    @Transactional
    @Override
    public void subscribeSchoolCoordinates(String schoolCoordinateStr, Message message) throws JsonProcessingException {
        try {
            SchoolDataPublishPayloadDTO<List<GeoCoordinateDTO>> publishPayloadDTO = objectMapper.readValue(schoolCoordinateStr, new TypeReference<>() {
            });
            Optional<GeoCoordinateDTO> optionalGeoCoordinateDTO = publishPayloadDTO.getData().parallelStream().findFirst();

            Optional<IdProjection> optionalIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(publishPayloadDTO.getSchoolTelaNumber(), Status.DELETED);

            if (optionalIdProjection.isPresent() && optionalGeoCoordinateDTO.isPresent()) {
                GeoCoordinateDTO schoolCoordinateDTO = optionalGeoCoordinateDTO.get();
                IdProjection idProjection = optionalIdProjection.get();
                Optional<SchoolGeoCoordinate> optionalSchoolGeoCoordinate = schoolGeoCoordinateRepository.findByStatusNotAndSchool_Id(Status.DELETED, idProjection.getId());

                if (optionalSchoolGeoCoordinate.isPresent()) {
                    SchoolGeoCoordinate schoolGeoCoordinate = optionalSchoolGeoCoordinate.get();
                    schoolGeoCoordinate.setDisplacement((int) schoolCoordinateDTO.getMaxDisplacement());
                    schoolGeoCoordinate.setLatitude(schoolCoordinateDTO.getLatitude());
                    schoolGeoCoordinate.setLongtitude(schoolCoordinateDTO.getLongitude());
                    schoolGeoCoordinate.setGeoFenceActivated(schoolCoordinateDTO.isGeoFenceActivated());
                    schoolGeoCoordinate.setPinClockActivated(schoolCoordinateDTO.isPinClockActivated());
                    SchoolGeoCoordinate saved = schoolGeoCoordinateRepository.save(schoolGeoCoordinate);
                    schoolCoordinateDTO.setId(saved.getId());
                }else{
                    SchoolGeoCoordinate schoolGeoCoordinate = new SchoolGeoCoordinate();
                    schoolGeoCoordinate.setStatus(Status.ACTIVE);
                    schoolGeoCoordinate.setSchool(new School(idProjection.getId()));
                    schoolGeoCoordinate.setDisplacement((int) schoolCoordinateDTO.getMaxDisplacement());
                    schoolGeoCoordinate.setLatitude(schoolCoordinateDTO.getLatitude());
                    schoolGeoCoordinate.setLongtitude(schoolCoordinateDTO.getLongitude());
                    schoolGeoCoordinate.setGeoFenceActivated(schoolCoordinateDTO.isGeoFenceActivated());
                    schoolGeoCoordinate.setPinClockActivated(schoolCoordinateDTO.isPinClockActivated());
                    SchoolGeoCoordinate saved = schoolGeoCoordinateRepository.save(schoolGeoCoordinate);
                    schoolCoordinateDTO.setId(saved.getId());
                }
//                jmsTemplate.setPubSubDomain(true);
                MQResponseDto<GeoCoordinateDTO> responseDto = new MQResponseDto<>();
                responseDto.setResponseType(ResponseType.SCHOOL_COORDINATES);
                responseDto.setData(schoolCoordinateDTO);
//                jmsTemplate.convertAndSend(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));

                queueTopicPublisher.publishTopicData(publishPayloadDTO.getSchoolTelaNumber(), objectMapper.writeValueAsString(responseDto));
                log.info("PUBLISHED SAVE UPDATED SCHOOL_COORDINATES  for {} {} {} ",publishPayloadDTO.getAcademicTerm(), idProjection.getId() ,  schoolCoordinateDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }


}
