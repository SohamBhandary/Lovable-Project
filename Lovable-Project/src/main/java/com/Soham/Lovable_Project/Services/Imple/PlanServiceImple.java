package com.Soham.Lovable_Project.Services.Imple;

import com.Soham.Lovable_Project.DTOs.Subcription.PlanReponse;
import com.Soham.Lovable_Project.Services.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImple implements PlanService {
    @Override
    public List<PlanReponse> getActivePlans() {
        return List.of();
    }
}
