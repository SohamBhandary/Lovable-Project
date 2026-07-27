package com.Soham.Intelligence_Service.Repository;


import com.Soham.Intelligence_Service.Entities.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UsageLogRepository  extends JpaRepository<UsageLog,Long> {
    Optional<UsageLog> findByUserIdAndDate(Long userId, LocalDate today);

}
