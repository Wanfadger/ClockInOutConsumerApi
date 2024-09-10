package com.planetsystems.tela.api.ClockInOutConsumer.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.ClockInTableColumns;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.MQResponseDto;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.ResponseType;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.SyncClockIn;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.enums.ClockedStatus;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.queries.ClockInQueryString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cms.PasswordRecipientId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClockInConsumerImpl {

    private final JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JmsTemplate jmsTemplate;


    private final ObjectMapper objectMapper;

    @JmsListener(destination = "${queue.clockins}")
          @Transactional
          public void subscribeClockIns(String clockIns) throws JsonProcessingException {
        List<SyncClockIn> dtos = objectMapper.readValue(clockIns, new TypeReference<>() {
        });

        log.info("subscribeClockIn {}" , dtos);
    }


    @JmsListener(destination = "${queue.clockin}")
    @Transactional
    public void subscribeClockIn(String clockInString) throws JsonProcessingException {
        SyncClockIn dto = objectMapper.readValue(clockInString, new TypeReference<>() {
        });



//        String NationalClockInBetweenDatesSQL = """
//            SELECT   C."clockInDate" , C."clockInTime" , C."clockinType" AS clockInType  , D.name AS districtName , D.id AS districtId
//            FROM "ClockIns" AS C
//                     INNER JOIN "Schools" S on S.id = C.school_id
//                     INNER JOIN "Districts" D on D.id = S.district_id
//                     WHERE C.status <> 8 AND S.status <> 8 AND D.status <> 8
//                       AND C."clockInDate" BETWEEN ?::DATE AND ?::DATE AND S."schoolOwnership" = ?
//            """;

        // todo check if clock exits then save

        // todo save to db
        ClockInTableColumns clockIn = ClockInTableColumns.builder()
                .displacement(dto.getDisplacement())
                .academicTerm_id(dto.getAcademicTermId())
                .build();
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(clockIn);



        int update = namedParameterJdbcTemplate.update(ClockInQueryString.INSERT_CLOCKIN_QUERY, parameters);
        log.info("subscribeClockIn {}" , dto);
        publishSchoolClockIns(dto);
    }


    @Async
    public void publishSchoolClockIns(SyncClockIn dto){
      try {
          // todo get school by tela number



          // get clockIns by tela number
          List<SyncClockIn> clockIns = List.of(dto);
          jmsTemplate.setPubSubDomain(true);


          MQResponseDto<List<SyncClockIn>> responseDto = new MQResponseDto<>();
          responseDto.setResponseType(ResponseType.CLOCKINS);
          responseDto.setData(clockIns);
          jmsTemplate.convertAndSend(dto.getTelaSchoolNumber()  ,objectMapper.writeValueAsString(responseDto));
      }catch (Exception e){
          e.printStackTrace();
          System.out.println(e);
      }


    }

    /*
     List<DistrictClockInOutProjection> districtClockInOutProjectionList = jdbcTemplate.queryForStream(DistrictQueryString.DistrictClockInClockOutBetweenDatesSQL, (rs, rowNum) ->
                        new DistrictClockInOutProjection(rs.getDate("clockInDate").toLocalDate(), rs.getTime("clockInTime").toLocalTime(), rs.getTime("clockOutTime").toLocalTime(),
                                rs.getString("clockInType"), rs.getString("schoolId"), rs.getString("staffId")), localGovernment, yearTermDate.startDate(), yearTermDate.endDate())
                .filter(nCOP -> termPublicHolidayDtoList.stream().noneMatch(publicHoliday -> (nCOP.clockInDate().equals(publicHoliday.date())))) // exclude public holidays
                .filter(nCOP -> !(nCOP.clockInDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) || nCOP.clockInDate().getDayOfWeek().equals(DayOfWeek.SUNDAY))) // exclude weekends
                .toList();

     */


}
