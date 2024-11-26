package com.planetsystems.tela.api.ClockInOutConsumer.service.cache;

import com.planetsystems.tela.api.ClockInOutConsumer.dto.AcademicTermDTO;
import com.planetsystems.tela.api.ClockInOutConsumer.dto.SchoolDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateCacheServiceImpl implements UpdateCacheService{

    final RedisTemplate redisTemplate;

    @Override
    public void updatedCacheActiveAcademicTerm(AcademicTermDTO academicTermDTO) {
        academicTermDTO.setStartDate("26/11/2024");
        academicTermDTO.setEndDate("26/11/2024");
        log.info("Successfully updated {}  {}" , CacheKeys.ACTIVE_ACADEMIC_TERM , redisTemplate.opsForValue().get(CacheKeys.ACTIVE_ACADEMIC_TERM+"::"+SimpleKey.EMPTY));

        // update
        redisTemplate.opsForValue()
                .set(CacheKeys.ACTIVE_ACADEMIC_TERM+"::"+SimpleKey.EMPTY , academicTermDTO ,Duration.ofMinutes(30));

        log.info("Successfully updated after {}  {}" , CacheKeys.ACTIVE_ACADEMIC_TERM , redisTemplate.opsForValue().get(CacheKeys.ACTIVE_ACADEMIC_TERM+"::"+SimpleKey.EMPTY));
        log.info("Successfully updated after {}  {}" , CacheKeys.SCHOOL , redisTemplate.opsForValue().get(CacheKeys.SCHOOL+"::"+"8008227371101"));
        log.info("Successfully updated after {}  {}" , CacheKeys.STAFFS , redisTemplate.opsForValue().get(CacheKeys.STAFFS+"::"+"8008227371101"));
    }

    @Override
    public void updatedCacheSchoolData(SchoolDTO schoolDTO) {

    }
}
