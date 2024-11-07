package com.planetsystems.tela.api.ClockInOutConsumer.service;

import com.planetsystems.tela.api.ClockInOutConsumer.Repository.SchoolRepository;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.IdProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.IdNameDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.SchoolDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.SystemAppFeedBackDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.exception.TelaNotFoundException;
import com.planetsystems.tela.api.ClockInOutConsumer.model.District;
import com.planetsystems.tela.api.ClockInOutConsumer.model.School;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolService {
    final SchoolRepository schoolRepository;

    public  ResponseEntity<SystemAppFeedBackDTO<SchoolDTO>> validateSchool(String telaSchoolNumber) {
        IdProjection idProjection = schoolRepository.idByStatusAndTelaSchoolUID(Status.DELETED , telaSchoolNumber)
                .orElseThrow(() -> new TelaNotFoundException("Theres no school associated with " + telaSchoolNumber));
        School school = schoolRepository.findByStatusNotAndId(Status.DELETED, idProjection.getId()).orElseThrow(() -> new TelaNotFoundException("Theres no school associated with " + telaSchoolNumber));
        District district = school.getDistrict();
        SchoolDTO dto  = SchoolDTO.builder()
                .schoolOwnership(school.getSchoolOwnership().getSchoolOwnership())
                .schoolLevel(school.getSchoolLevel().getLevel())
                .name(school.getName())
                .telaSchoolNumber(school.getTelaSchoolUID())
                .phoneNumber(school.getDeviceNumber())
                .district(new IdNameDTO(district.getId(), district.getName()))
                .build();

        SystemAppFeedBackDTO feedBackDTO = new SystemAppFeedBackDTO<SchoolDTO>();
        feedBackDTO.setMessage("success");
        feedBackDTO.setData(dto);
        feedBackDTO.setStatus(true);
        return ResponseEntity.ok(feedBackDTO);
    }
}
