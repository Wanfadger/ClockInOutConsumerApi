package com.planetsystems.tela.api.ClockInOutConsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.*;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.IdProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.supervision.StaffDailyAttendanceTaskSupervisionDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.ClassTimetableDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.StaffDailyTimetableDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.TimeTableLessonDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.timetable.TimetableDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.exception.TelaNotFoundException;
import com.planetsystems.tela.api.ClockInOutConsumer.model.*;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.SchoolLevel;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import com.planetsystems.tela.api.ClockInOutConsumer.service.cache.CacheEvictService;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.Convertor;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.TelaDatePattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableAsync
@Slf4j
@RequiredArgsConstructor
@EnableCaching
public class ClockInOutConsumerApplication implements CommandLineRunner {
//    private final CacheEvictService cacheEvictService;
//	private final SchoolRepository schoolRepository;
//	private final AcademicTermRepository academicTermRepository;
//	private final ClockOutRepository clockOutRepository;
//	final SubjectRepository subjectRepository;
//	final LearnerEnrollmentRepository learnerEnrollmentRepository;
//	final SNLearnerEnrollmentRepository snLearnerEnrollmentRepository;
//	final LearnerAttendanceRepository learnerAttendanceRepository;
//	final SNLearnerAttendanceRepository snLearnerAttendanceRepository;
//	final TimeTableLessonRepository timeTableLessonRepository;
//	final TimeTableRepository timeTableRepository;
//	final StaffDailyTimeTableRepository staffDailyTimeTableRepository;
//	final StaffDailyTimeTableLessonRepository staffDailyTimeTableLessonRepository;
//	final StaffDailyAttendanceSupervisionRepository staffDailyAttendanceSupervisionRepository;
//	final StaffDailyAttendanceTaskSupervisionRepository staffDailyAttendanceTaskSupervisionRepository;
	public static void main(String[] args) {
		SpringApplication.run(ClockInOutConsumerApplication.class, args);

	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		//MQResponseDto(ResponseType=SCHOOL, data=SchoolDTO(id=2c9a808b8b46eead018b51cb73d4002f, telaSchoolNumber=8008227371101, phoneNumber=8008227371101, name=DES PRIMARY SCHOOL,
		// district=IdNameDTO[id=2c9a80838711dfe5018712cf296100a4, name=Nakawa Division], academicTerm=AcademicTermDTO(id=8a8089b191dfdd700191dfe76bc80018, name=Term 3, startDate=16/09/2024,
		// endDate=06/12/2024, year=2024), regNo=null, schoolLevel=Primary, schoolOwnership=Government, schoolType=null, licensed=true, licenseKey=null,
		// geoCoordinate=GeoCoordinateDTO(id=8a80891c8f1905cd018f19784817317d, latitude=0.3277913, longitude=32.6128246, geoFenceActivated=false, pinClockActivated=false, maxDisplacement=1500.0)))
//        String term = "8a8089b191dfdd700191dfe76bc80018";
//		String telaNumber = "8008227371101";
//
//		cacheEvictService.evictSchoolData(telaNumber);
//		cacheEvictService.evictDistricts();

//		Optional<IdProjection> optionalSchoolIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot("8008222085089" , Status.DELETED);
//		if (optionalSchoolIdProjection.isPresent()) {
//			IdProjection idProjection = optionalSchoolIdProjection.get();
//			Optional<AcademicTerm> optionalAcademicTerm = academicTermRepository.activeAcademicTerm(Status.ACTIVE);
//			AcademicTerm academicTerm = optionalAcademicTerm.get();
//
//			List<ClockOut> clockOuts = clockOutRepository.allByTerm_SchoolWithStaff(academicTerm.getId(), idProjection.getId());
//			System.out.println("clockOuts "+" "+clockOuts.size());
//
//			// TODO fetch all term clockouts for school
//			// todo group then by clockin and date
//			Map<String, List<ClockOut>> dateClockInMap = clockOuts.parallelStream()
//					.collect(Collectors.groupingBy(clockOut -> clockOut.getClockOutDate().format(TelaDatePattern.datePattern) + "-" + clockOut.getClockIn().getId()));
//			// todo sortthem by create time and delete the last created ones
//			dateClockInMap.keySet().stream().forEach(dateClockIn -> {
//				String[] split = dateClockIn.split("-");
//				String date = split[0];
//				String clockIn = split[1];
//				List<ClockOut> clockOutList = dateClockInMap.get(dateClockIn);
//				if (clockOutList.size() > 1){
//					ClockOut first = clockOutList.stream().findFirst().get();
//					List<ClockOut> excludedFirst = clockOutList.stream().filter(clockOut -> !(clockOut.getId().equals(first.getId()))).toList();
//					System.out.println(dateClockIn);
//					System.out.println("DATE: "+date+" cl: "+ clockIn+" SIZE:"+ clockOutList.size());
//					System.out.println(clockOutList.stream().map(clockOut -> clockOut.getCreatedDateTime()).sorted().toList());
//					System.out.println(excludedFirst.stream().map(clockOut -> clockOut.getCreatedDateTime()).sorted().toList());
//
////					clockOutRepository.deleteAllInBatch(excludedFirst);
////					System.out.println("successfully deleted");
//				}else {
//					System.out.println(clockOutList);
//					System.out.println("All are below one");
//				}
//
//			});
//
////			System.out.println(clockOuts1);
//		}
//

		/// dleete

//		Optional<AcademicTerm> optionalAcademicTerm = academicTermRepository.activeAcademicTerm(Status.ACTIVE);
//		System.out.println(optionalAcademicTerm.get().getTerm());
////8008226193412 , 8008229464166
//		Optional<IdProjection> schoolIdByTelaNumberOptional = schoolRepository.findByTelaSchoolUIDAndStatusNot("8008229464166" , Status.DELETED);
//		if (schoolIdByTelaNumberOptional.isPresent() && optionalAcademicTerm.isPresent()) {
//			AcademicTerm academicTerm = optionalAcademicTerm.get();
//
//
//			IdProjection schoolIdProjection = schoolIdByTelaNumberOptional.get();
//			School school = schoolRepository.findByStatusNotAndId(Status.DELETED, schoolIdProjection.getId()).orElseThrow(() -> new RuntimeException("school not found"));
//
//			log.info("school {} " , school.getName());
//			log.info("school {} " , school.getId());
//			log.info("academicTerm {} " , academicTerm.getId());
//			SchoolLevel schoolLevel = school.getSchoolLevel();
//
//			ClassTimetableDTO classTimetableDTO = ClassTimetableDTO.builder()
//					.lessons(List.of(TimeTableLessonDTO.builder().build()))
//					.build();
//
//			TimetableDTO timetableDTO = TimetableDTO.builder()
//					.id("")
//					.schoolId("")
//					.academicTermId("")
//					.classTimetables(List.of(classTimetableDTO))
//					.build();


//			List<StaffDailyTimeTable> termStaffDailyTimeTables = staffDailyTimeTableRepository.allByTerm_School("8a8089aa8fa5e464018fae49dac500ce", school.getId());
//			log.info("termStaffDailyTimeTables {}  " , termStaffDailyTimeTables.size() );
//			List<StaffDailyTimeTableLesson> termStaffDailyTimeTableLessons = staffDailyTimeTableLessonRepository.allIn(termStaffDailyTimeTables);
//			log.info("termStaffDailyTimeTableLessons {}  " , termStaffDailyTimeTableLessons.size());
//
//			List<StaffDailyTimetableDTO> staffDailyTimetableDTOS = termStaffDailyTimeTables.parallelStream().flatMap(dailyTimeTable -> termStaffDailyTimeTableLessons.parallelStream()
//					.filter(dailyLeLesson -> dailyTimeTable.getId().equals(dailyLeLesson.getStaffDailyTimeTable().getId()))
//					.map(dailyLeLesson -> {
//						StaffDailyTimetableDTO dto = StaffDailyTimetableDTO.builder()
//								.lessonId(dailyLeLesson.getId())
//								.actionStatus("")
//								.submissionDate(dailyLeLesson.getLessonDate().format(TelaDatePattern.datePattern))
//								.staffId(dailyTimeTable.getSchoolStaff().getId())
//								.comment(dailyTimeTable.getComment())
//								.build();
//						return dto;
//					})
//			).sorted(Comparator.comparing(StaffDailyTimetableDTO::getStaffId)).toList();
//			log.info("staffDailyTimetableDTOS {}  {} " , staffDailyTimetableDTOS , staffDailyTimetableDTOS.size() );


//			List<StaffDailyAttendanceSupervision> termStaffDailyAttendanceSupervisions = staffDailyAttendanceSupervisionRepository.allByTerm_School(academicTerm.getStartDate(), academicTerm.getEndDate(), school.getId());
//			List<StaffDailyAttendanceTaskSupervision> termStaffDailyAttendanceTaskSupervisions = staffDailyAttendanceTaskSupervisionRepository.allIn(termStaffDailyAttendanceSupervisions);
//
//
//			List<StaffDailyAttendanceTaskSupervisionDTO> staffDailyAttendanceTaskSupervisionDTOS = termStaffDailyAttendanceSupervisions.parallelStream().flatMap(attendanceSupervision -> termStaffDailyAttendanceTaskSupervisions.parallelStream()
//							.filter(taskSupervision -> attendanceSupervision.getId().equals(taskSupervision.getStaffDailyAttendanceSupervision().getId()))
//							.map(taskSupervision -> {
//								StaffDailyAttendanceTaskSupervisionDTO dto = StaffDailyAttendanceTaskSupervisionDTO.builder()
//										.id(taskSupervision.getId())
//										.lessonTaskId(attendanceSupervision.getId())
//										.supervisorId(attendanceSupervision.getSupervisor().getId())
//										.supervisionDate(attendanceSupervision.getSupervisionDate().format(TelaDatePattern.datePattern))
//										.supervisionTime(attendanceSupervision.getSupervisionTime().format(TelaDatePattern.timePattern24))
//										.supervisionStatus(attendanceSupervision.getAttendanceStatus().getAttendance())
//										.timeStatus( taskSupervision.getTeachingStatus() != null ?  taskSupervision.getTeachingStatus().getSupervision():"")
//										.comment(taskSupervision.getComment())
//										.build();
//								return dto;
//							})
//					).sorted(Comparator.comparing(StaffDailyAttendanceTaskSupervisionDTO::getSupervisionDate))
//					.toList();
//			log.info("staffDailyAttendanceTaskSupervisionDTOS {}  {} " , staffDailyAttendanceTaskSupervisionDTOS , staffDailyAttendanceTaskSupervisionDTOS.size() );

//			IdProjection timetable = timeTableRepository.findBySchool_IdAndAcademicTerm_Id(school.getId() , "8a8089aa8fa5e464018fae49dac500ce").orElseThrow(() -> new TelaNotFoundException(school.getName() + "Timetable not found from " + academicTerm.getTerm()));
//
//			List<TimeTableLesson> timeTableLessons = timeTableLessonRepository.allByTimetable(timetable.getId()).parallelStream()
//					.filter(Convertor.distinctByKey(timeTableLesson -> timeTableLesson.getStartTime() + "" + timeTableLesson.getEndTime() + "" + timeTableLesson.getLessonDay() + "" + timeTableLesson.getSubject().getId()))
//
//					.toList();
//
//			log.info("TIME LESSONS {} \n {} " , timeTableLessons.size() , timeTableLessons);
//
//			System.out.println();
//			System.out.println();
//
//			Map<String, Long> stringLongMap = timeTableLessonRepository.allByTimetable(timetable.getId()).parallelStream()
//					.filter(Convertor.distinctByKey(timeTableLesson -> timeTableLesson.getStartTime() + "" + timeTableLesson.getLessonDay() + "" + timeTableLesson.getSubject().getId()))
//					.collect(Collectors.groupingBy(timeTableLesson -> timeTableLesson.getSchoolClass().getId(), Collectors.counting()));
//			log.info("tim \n \n {}" , stringLongMap);


//			log.info("TIME TABLE \n {} " , timetableDTO);

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

//			List<LearnerAttendance> learnerAttendances = learnerAttendanceRepository.allByTerm_School("8a8089aa8fa5e464018fae49dac500ce", "2c92808282d44b4a0182d492cc730818");
//				log.info("learnerAttendances {} "  ,learnerAttendances.size());

//			List<LearnerAttendance> DATElearnerAttendances = learnerAttendanceRepository.allByDate_School(LocalDate.of(2024 , 8 , 06), "2c92808282d44b4a0182d492cc730818");
//			log.info("learnerAttendances DTE {} "  ,DATElearnerAttendances.size());


//			List<LearnerAttendance> slearnerAttendances = snLearnerAttendanceRepository.allByTerm_School("8a8089b191dfdd700191dfe76bc80018", "2c92808282d44b4a0182d492cc730818");
//			log.info("slearnerAttendances {} ", slearnerAttendances.size());
//
//			List<LearnerAttendance> sDATElearnerAttendances = snLearnerAttendanceRepository.allByDate_School(LocalDate.of(2024, 8, 06), "2c92808282d44b4a0182d492cc730818");
//			log.info("slearnerAttendances DTE {} ", sDATElearnerAttendances.size());



//		List<ClockIn> schoolDateClockIns = clockInRepository.allByTerm_SchoolWithStaff(academicTerm.getId(), school.getId());


//		}



	}

//	@JmsListener(destination = "8008225962439")
//	@Transactional
//	public void subscribeSchoolClockIn(String clockInString) throws JsonProcessingException {
//		log.info("SUBSCRIBING CLOCK INS");
//		log.info("subscribeSchoolClockIn {}", clockInString);
//
//	}
}
