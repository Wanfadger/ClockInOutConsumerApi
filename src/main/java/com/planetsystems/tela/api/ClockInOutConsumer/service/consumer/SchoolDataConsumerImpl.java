package com.planetsystems.tela.api.ClockInOutConsumer.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.LearnerEnrollmentRepository;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.SNLearnerEnrollmentRepository;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.SchoolRepository;
import com.planetsystems.tela.api.ClockInOutConsumer.Repository.projections.IdProjection;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.ClockInRequestDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.schoolData.P_LearnerHeadCountDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.schoolData.SchoolDataPublishPayloadDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.model.*;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.Status;
import com.planetsystems.tela.api.ClockInOutConsumer.utils.TelaDatePattern;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolDataConsumerImpl {

    final ObjectMapper objectMapper;
    final LearnerEnrollmentRepository learnerEnrollmentRepository;
    final SNLearnerEnrollmentRepository snLearnerEnrollmentRepository;
    final SchoolRepository schoolRepository;

    final SynchronizeMobileDataService synchronizeMobileDataService;



    @JmsListener(destination = "${queue.learnerHeadCounts}")
    @Transactional
    public void subscribeLearnerHeadCounts(String learnerHeadCountStr) throws JsonProcessingException {
        log.info("learnerHeadCountStr {}  " , learnerHeadCountStr);
        SchoolDataPublishPayloadDTO<List<P_LearnerHeadCountDTO>> publishPayloadDTO = objectMapper.readValue(learnerHeadCountStr, new TypeReference<SchoolDataPublishPayloadDTO<List<P_LearnerHeadCountDTO>>>() {
        });

        List<P_LearnerHeadCountDTO> p_learnerHeadCountDTOS =  publishPayloadDTO.getData();
        log.info("ddd {} " , p_learnerHeadCountDTOS);

        Optional<IdProjection> optionalIdProjection = schoolRepository.findByTelaSchoolUIDAndStatusNot(publishPayloadDTO.getSchoolTelaNumber(), Status.DELETED);
        if (optionalIdProjection.isPresent()) {
            log.info("FOUND TELA NUMBER {} " , publishPayloadDTO.getSchoolTelaNumber());
            IdProjection idProjection = optionalIdProjection.get();
            List<LearnerEnrollment> learnerEnrollments = learnerEnrollmentRepository.allBySchool_term(idProjection.getId(), publishPayloadDTO.getAcademicTerm());
            List<SNLearnerEnrollment> snLearnerEnrollments = snLearnerEnrollmentRepository.allBySchool_term(idProjection.getId(), publishPayloadDTO.getAcademicTerm());

            // process new general learners
            List<LearnerEnrollment> newGeneralLearnerEnrollments = p_learnerHeadCountDTOS.parallelStream().filter(dto -> dto.getLearnerType().equalsIgnoreCase("General"))
                    .filter(dto -> learnerEnrollments.parallelStream().noneMatch(enrollment -> dto.getClassId().equals(enrollment.getSchoolClass().getId())))
                    .map(dto -> {
                        LearnerEnrollment enrollment = LearnerEnrollment.builder()
                                .schoolClass(new SchoolClass(dto.getClassId()))
                                .enrollmentStatus(Status.ACTIVE)
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
            List<SNLearnerEnrollment> newSnLearnerEnrollments = p_learnerHeadCountDTOS.parallelStream().filter(dto -> dto.getLearnerType().equalsIgnoreCase("Special Needs"))
                    .filter(dto -> snLearnerEnrollments.parallelStream().noneMatch(enrollment -> dto.getClassId().equals(enrollment.getSchoolClass().getId())))
                    .map(dto -> {
                        SNLearnerEnrollment enrollment = SNLearnerEnrollment.builder()
                                .schoolClass(new SchoolClass(dto.getClassId()))
                                .enrollmentStatus(Status.ACTIVE)
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
            List<LearnerEnrollment> existingGeneralLearnerEnrollments = p_learnerHeadCountDTOS.parallelStream().
                    filter(dto -> dto.getLearnerType().equalsIgnoreCase("General"))
                    .flatMap(dto -> learnerEnrollments.parallelStream()
                            .filter(enrollment -> dto.getClassId().equals(enrollment.getSchoolClass().getId()))
                            .map(enrollment -> {
                                enrollment.setTotalBoys(enrollment.getTotalBoys());
                                enrollment.setTotalGirls(enrollment.getTotalGirls());
                                return enrollment;
                            })
                    ).toList();
            if (existingGeneralLearnerEnrollments.size()>0){
                learnerEnrollmentRepository.saveAll(existingGeneralLearnerEnrollments);
            }
            log.info("existingGeneralLearnerEnrollment {} " , existingGeneralLearnerEnrollments.size());


            List<SNLearnerEnrollment> existingSnLearnerEnrollments = p_learnerHeadCountDTOS.parallelStream().
                    filter(dto -> dto.getLearnerType().equalsIgnoreCase("Special Needs"))
                    .flatMap(dto -> snLearnerEnrollments.parallelStream()
                            .filter(enrollment -> dto.getClassId().equals(enrollment.getSchoolClass().getId()))
                            .map(enrollment -> {
                                enrollment.setTotalBoys(enrollment.getTotalBoys());
                                enrollment.setTotalGirls(enrollment.getTotalGirls());
                                return enrollment;
                            })
                    ).toList();
            if (existingSnLearnerEnrollments.size()>0){
                snLearnerEnrollmentRepository.saveAll(existingSnLearnerEnrollments);
            }
            log.info("existingSnLearnerEnrollment {} " , existingSnLearnerEnrollments.size());


            School school = schoolRepository.findByStatusNotAndId(Status.DELETED, idProjection.getId()).get();
            synchronizeMobileDataService.publishLearnerEnrollments(school , new AcademicTerm(publishPayloadDTO.getAcademicTerm()));
        }

//        schoolRepository.findByTelaSchoolUIDAndStatusNot()
//
//        learnerEnrollmentRepository.allBySchool_term()


        log.info("subscribeLearnerHeadCounts {} {} " , p_learnerHeadCountDTOS.size() , p_learnerHeadCountDTOS);

    }

}
