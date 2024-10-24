package com.planetsystems.tela.api.ClockInOutConsumer.Repository;

import com.planetsystems.tela.api.ClockInOutConsumer.model.Otp;
import com.planetsystems.tela.api.ClockInOutConsumer.model.enums.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.time.*;
import java.util.*;


@Repository
public interface OtpRepository extends JpaRepository<Otp , String> {
    Optional<Otp> findByUsername(String username);
    Optional<Otp> findByUsernameAndCode(String username , int otp);
    boolean existsByCode(int otp);
    boolean existsByUsernameAndCode(String username , int otp);
}
