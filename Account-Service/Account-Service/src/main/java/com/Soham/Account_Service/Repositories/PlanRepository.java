package com.Soham.Account_Service.Repositories;

import com.Soham.Lovable_Project.Entities.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository  extends JpaRepository<Plan,Long> {
    Optional<Plan> findByStripePriceId(String id);
}
