package com.planetsystems.tela.api.ClockInOutConsumer.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.ClockInRepository;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.SchoolRepository;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.IdProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.ClockInRequestDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.MQResponseDto;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.ResponseType;
import com.planetsystems.tela.api.ClockInOutConsumer.model.AcademicTerm;
import com.planetsystems.tela.api.ClockInOutConsumer.model.ClockIn;
import com.planetsystems.tela.api.ClockInOutConsumer.model.School;
import com.planetsystems.tela.api.ClockInOutConsumer.model.SchoolStaff;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.ClockedStatus;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.TelaDatePattern;
import jakarta.jms.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClockInConsumerImpl {

    private final JmsTemplate jmsTemplate;
    private final ClockInRepository clockInRepository;
    private final SchoolRepository schoolRepository;

    private final ObjectMapper objectMapper;


    @JmsListener(destination = "${queue.clockins}")
    @Transactional
    public void subscribeClockIns(String clockIns) throws JsonProcessingException {
        List<ClockInRequestDTO> dtos = objectMapper.readValue(clockIns, new TypeReference<>() {
        });

//        log.info("subscribeClockIn {}", dtos);
    }


    @JmsListener(destination = "${queue.clockin}" )
    @Transactional
    public void subscribeClockIn(String clockInString) throws JsonProcessingException {
        ClockInRequestDTO dto = objectMapper.readValue(clockInString, new TypeReference<>() {
        });

        Optional<IdProjection> optionalSchoolIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(dto.getTelaSchoolNumber() , Status.DELETED);



        if (optionalSchoolIdProjection.isPresent()) {
            LocalDateTime clockInDateTime = LocalDateTime.parse(dto.getClockInDateTime(), TelaDatePattern.dateTimePattern24);
            IdProjection schoolIdProjection = optionalSchoolIdProjection.get();

            if (!clockInRepository.existsByStatusNotAndClockInDateAndSchoolStaff_Id(Status.DELETED , clockInDateTime.toLocalDate() , dto.getStaffId())) {
                ClockIn clockIn = ClockIn.builder()
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

                log.info("CLOCKIN TO BE SAVED {}  " , dto);
                log.info("TELA NO {}  " , dto.getTelaSchoolNumber());

                clockInRepository.save(clockIn);

                publishSchoolClockIn(dto);
            }else{
                log.info("ALREADY CLOCKED IN");
            }

        }
    }


    @Async
    public void publishSchoolClockIn(ClockInRequestDTO dto) {
        try {
            log.info("PUBLISHING SCHOOL CLOCk IN TO {} " , dto.getTelaSchoolNumber());
            jmsTemplate.setPubSubDomain(true);
            jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
            MQResponseDto<ClockInRequestDTO> responseDto = new MQResponseDto<>();
            responseDto.setResponseType(ResponseType.CLOCKIN);
            responseDto.setData(dto);
            jmsTemplate.convertAndSend(dto.getTelaSchoolNumber(), objectMapper.writeValueAsString(responseDto));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }



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
