package com.planetsystems.tela.api.ClockInOutConsumer.controller;

import com.planetsystems.tela.api.ClockInOutConsumer.dto.SchoolDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.SynchronizeRestSchoolDataDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.SystemAppFeedBackDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.service.SchoolService;
import com.planetsystems.tela.api.ClockInOutConsumer.service.cache.CacheKeys;
import com.planetsystems.tela.api.ClockInOutConsumer.service.consumer.SynchronizeMobileDataService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SchoolController {
    final SchoolService schoolService;
    final SynchronizeMobileDataService synchronizeMobileDataService;

    @PostMapping("/validateSchool/{telaSchoolNumber}")
    public ResponseEntity<SystemAppFeedBackDTO<SchoolDTO>> validateSchool(@PathVariable @NotEmpty(message = "telaSchoolNumber is required") @NotBlank(message = "telaSchoolNumber is required") String telaSchoolNumber)  {
        return schoolService.validateSchool(telaSchoolNumber);
    }

    @PostMapping("/synchronizeRestSchoolData")
    public ResponseEntity<Boolean> synchronizeRestSchoolData(@RequestBody SynchronizeRestSchoolDataDTO dto)  {
        return synchronizeMobileDataService.synchronizeRestSchoolData(dto);
    }

    @GetMapping("evictDistricts")
    @CacheEvict(value = CacheKeys.DISTRICTS)
    public String evictDistricts(){
//        evictDistrictService();
        return "Successfully evicted";
    }
}
