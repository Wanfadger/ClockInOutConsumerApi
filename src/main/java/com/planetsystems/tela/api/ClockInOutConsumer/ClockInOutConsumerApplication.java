package com.planetsystems.tela.api.ClockInOutConsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.AcademicTermRepository;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.ClockInRepository;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.SchoolRepository;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.IdProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.ClockInRequestDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.MQResponseDto;
import com.planetsystems.tela.api.ClockInOutConsumer.model.AcademicTerm;
import com.planetsystems.tela.api.ClockInOutConsumer.model.School;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootApplication
@EnableAsync
@Slf4j
@RequiredArgsConstructor
public class ClockInOutConsumerApplication implements CommandLineRunner {

	private final SchoolRepository schoolRepository;
	private final AcademicTermRepository academicTermRepository;
	private final ClockInRepository clockInRepository;
	public static void main(String[] args) {
		SpringApplication.run(ClockInOutConsumerApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

//		Optional<IdProjection> optionalSchoolIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot("8008225962439" , Status.DELETED);
//		if (optionalSchoolIdProjection.isPresent()) {
//			System.out.println(optionalSchoolIdProjection.get().getId());
//		}

//		Optional<AcademicTerm> optionalAcademicTerm = academicTermRepository.activeAcademicTerm(Status.ACTIVE);
//		System.out.println(optionalAcademicTerm.get().getTerm());
//
//		Optional<IdProjection> schoolIdByTelaNumberOptional = schoolRepository.findByTelaSchoolUIDAndStatusNot("8008226193412" , Status.DELETED);
//		if (schoolIdByTelaNumberOptional.isPresent()) {
//			IdProjection schoolIdProjection = schoolIdByTelaNumberOptional.get();
//			Optional<School> schoolOptional = schoolRepository.findByStatusNotAndId(Status.DELETED, schoolIdProjection.getId());
//
//			log.info("school {} " , schoolOptional.get());
//
//
//
//			clockInRepository.allClockByDate_SchoolWithStaff(LocalDate.now(), schoolIdProjection.getId());
//		}



	}

	@JmsListener(destination = "8008225962439")
	@Transactional
	public void subscribeSchoolClockIn(String clockInString) throws JsonProcessingException {
		log.info("SUBSCRIBING CLOCK INS");
		log.info("subscribeSchoolClockIn {}", clockInString);

	}
}
