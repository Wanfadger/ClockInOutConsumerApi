package com.planetsystems.tela.api.ClockInOutConsumer.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.ClockInRepository;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.ClockOutRepository;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.SchoolRepository;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.IdProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.ClockInDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.ClockOutDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.MQResponseDto;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.ResponseType;
import com.planetsystems.tela.api.ClockInOutConsumer.exception.TelaNotFoundException;
import com.planetsystems.tela.api.ClockInOutConsumer.model.*;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.ClockedStatus;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.TelaDatePattern;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.publisher.QueueTopicPublisher;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClockInConsumerImpl {

    private final JmsTemplate jmsTemplate;
    private final ClockInRepository clockInRepository;
    private final ClockOutRepository clockOutRepository;
    private final SchoolRepository schoolRepository;

    private final ObjectMapper objectMapper;

    private final QueueTopicPublisher queueTopicPublisher;



    @JmsListener(destination = "${queue.clockins}" , containerFactory = "queueConnectionFactory")
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

            /// existing
            List<ClockInDTO> existingSavedClockInDTOS = dtoList.parallelStream()
                    .filter(dto -> (dto.getId() != null  && !dto.getId().isEmpty()))
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
                        }
                        return dto;
                    }).toList();


            newSavedClockInDTOS.addAll(existingSavedClockInDTOS);

            publishSchoolClockIns(firstDTO.getTelaSchoolNumber() , newSavedClockInDTOS);

        }

    }


    @JmsListener(destination = "${queue.clockouts}" , containerFactory = "queueConnectionFactory")
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


//    private void  publishTopicData(String telaSchoolNumber , String data){
//        try {
//            jmsTemplate.setPubSubDomain(true);
//            jmsTemplate.setConnectionFactory(topicConnectionFactory);
//            jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
//            jmsTemplate.convertAndSend(telaSchoolNumber, data);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e);
//        }
//    }



//    @JmsListener(destination = "8008226193412" , containerFactory = "topicConnectionFactory")
//    @Transactional
//    public void subscribeSchoolClockIn(String clockInString) throws JsonProcessingException {
//
//        log.info("8008226193412 subscribeSchoolClockIn 1  {} ", clockInString);
//
//
//    }

//    @JmsListener(destination = "8008226193412" , containerFactory = "topicConnectionFactory")
//    @Transactional
//    public void subscribeSchoolClockIn22(String clockInString) throws JsonProcessingException {
//
//        log.info("80082259624390 subscribeSchoolClockIn 2  {} ", clockInString);
//
//
//    }
//
//    @JmsListener(destination = "800822596243901" , containerFactory = "topicConnectionFactory")
//    @Transactional
//    public void subscribeSchoolClockIn2(String clockInString) throws JsonProcessingException {
//
//        log.info("800822596243901 subscribeSchoolClockIn 1 {} ", clockInString);
//
//
//    }
//
//
//    @JmsListener(destination = "800822596243901" , containerFactory = "topicConnectionFactory")
//    @Transactional
//    public void subscribeSchoolClockIn3(String clockInString) throws JsonProcessingException {
//
//        log.info(" 800822596243901 subscribeSchoolClockIn 2 {} ", clockInString);
//
//
//    }





}
