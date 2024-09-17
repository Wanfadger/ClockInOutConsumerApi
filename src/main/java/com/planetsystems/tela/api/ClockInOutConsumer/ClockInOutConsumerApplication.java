package com.planetsystems.tela.api.ClockInOutConsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.*;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.IdProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.ClockInRequestDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.MQResponseDto;
import com.planetsystems.tela.api.ClockInOutConsumer.model.*;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.SchoolLevel;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.SubjectClassification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableAsync
@Slf4j
@RequiredArgsConstructor
public class ClockInOutConsumerApplication implements CommandLineRunner {

	private final SchoolRepository schoolRepository;
	private final AcademicTermRepository academicTermRepository;
	private final ClockInRepository clockInRepository;
	final SubjectRepository subjectRepository;
	final LearnerEnrollmentRepository learnerEnrollmentRepository;
	final SNLearnerEnrollmentRepository snLearnerEnrollmentRepository;
	final LearnerAttendanceRepository learnerAttendanceRepository;
	final SNLearnerAttendanceRepository snLearnerAttendanceRepository;

	public static void main(String[] args) {
		SpringApplication.run(ClockInOutConsumerApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

//		Optional<IdProjection> optionalSchoolIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot("8008225962439" , Status.DELETED);
//		if (optionalSchoolIdProjection.isPresent()) {
//			System.out.println(optionalSchoolIdProjection.get().getId());
//		}

		Optional<AcademicTerm> optionalAcademicTerm = academicTermRepository.activeAcademicTerm(Status.ACTIVE);
//		System.out.println(optionalAcademicTerm.get().getTerm());
////8008226193412 , 8008229464166
		Optional<IdProjection> schoolIdByTelaNumberOptional = schoolRepository.findByTelaSchoolUIDAndStatusNot("8008224447387" , Status.DELETED);
		if (schoolIdByTelaNumberOptional.isPresent() && optionalAcademicTerm.isPresent()) {
			AcademicTerm academicTerm = optionalAcademicTerm.get();


			IdProjection schoolIdProjection = schoolIdByTelaNumberOptional.get();
			School school = schoolRepository.findByStatusNotAndId(Status.DELETED, schoolIdProjection.getId()).orElseThrow(() -> new RuntimeException("school not found"));

			log.info("school {} " , school.getName());
			log.info("school {} " , school.getId());
			log.info("academicTerm {} " , academicTerm.getId());
			SchoolLevel schoolLevel = school.getSchoolLevel();
//			SubjectClassification subjectClassification = SubjectClassification.getSubjectClassification(schoolLevel.getLevel());
//			clockInRepository.allClockByDate_SchoolWithStaff(LocalDate.now(), schoolIdProjection.getId());
//			List<Subject> subjects = subjectRepository.findAllBySubjectClassificationNotNullAndStatusNotAndSubjectClassification(Status.DELETED, subjectClassification);
//			log.info("SUBJECTS {} " , subjects.size());
//			log.info("slclc {} " , subjectClassification.getSubjectClassification());

//			List<LearnerEnrollment> learnerEnrollments = learnerEnrollmentRepository.allBySchool_term("2c92808282d44b4a0182d495066821d8", "8a8089aa8fa5e464018fae49dac500ce");
//			log.info("learnerEnrollments {} "  ,learnerEnrollments.size());
//
//			List<SNLearnerEnrollment> slearnerEnrollments = snLearnerEnrollmentRepository.allBySchool_term("2c92808282d44b4a0182d495066821d8", "8a8089aa8fa5e464018fae49dac500ce");
//			log.info("SlearnerEnrollments {} "  ,slearnerEnrollments.size());

			List<LearnerAttendance> learnerAttendances = learnerAttendanceRepository.allByTerm_School("8a8089aa8fa5e464018fae49dac500ce", "2c92808282d44b4a0182d492cc730818");
				log.info("learnerAttendances {} "  ,learnerAttendances.size());

//			List<LearnerAttendance> DATElearnerAttendances = learnerAttendanceRepository.allByDate_School(LocalDate.of(2024 , 8 , 06), "2c92808282d44b4a0182d492cc730818");
//			log.info("learnerAttendances DTE {} "  ,DATElearnerAttendances.size());


//			List<LearnerAttendance> slearnerAttendances = snLearnerAttendanceRepository.allByTerm_School("8a8089b191dfdd700191dfe76bc80018", "2c92808282d44b4a0182d492cc730818");
//			log.info("slearnerAttendances {} ", slearnerAttendances.size());
//
//			List<LearnerAttendance> sDATElearnerAttendances = snLearnerAttendanceRepository.allByDate_School(LocalDate.of(2024, 8, 06), "2c92808282d44b4a0182d492cc730818");
//			log.info("slearnerAttendances DTE {} ", sDATElearnerAttendances.size());


		}



	}

	@JmsListener(destination = "8008225962439")
	@Transactional
	public void subscribeSchoolClockIn(String clockInString) throws JsonProcessingException {
		log.info("SUBSCRIBING CLOCK INS");
		log.info("subscribeSchoolClockIn {}", clockInString);

	}
}
