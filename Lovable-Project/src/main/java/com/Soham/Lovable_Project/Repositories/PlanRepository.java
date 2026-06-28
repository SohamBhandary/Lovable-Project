package com.Soham.Lovable_Project.Repositories;

import com.Soham.Lovable_Project.Entities.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository  extends JpaRepository<Plan,Long> {
}
