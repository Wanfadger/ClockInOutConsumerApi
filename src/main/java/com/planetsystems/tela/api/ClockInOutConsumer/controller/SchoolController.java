package com.planetsystems.tela.api.ClockInOutConsumer.controller;

import com.planetsystems.tela.api.ClockInOutConsumer.dto.SchoolDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.SystemAppFeedBackDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.service.SchoolService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SchoolController {
    final SchoolService schoolService;

    @PostMapping("/validateSchool/{telaSchoolNumber}")
    public ResponseEntity<SystemAppFeedBackDTO<SchoolDTO>> validateSchool(@PathVariable @NotEmpty(message = "telaSchoolNumber is required") @NotBlank(message = "telaSchoolNumber is required") String telaSchoolNumber)  {
       log.info("jajjssd {} " , telaSchoolNumber);
        return schoolService.validateSchool(telaSchoolNumber);
    }
}
