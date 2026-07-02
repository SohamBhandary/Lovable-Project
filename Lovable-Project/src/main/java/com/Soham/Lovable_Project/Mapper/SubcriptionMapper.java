package com.Soham.Lovable_Project.Mapper;

import com.Soham.Lovable_Project.DTOs.Subcription.PlanReponse;
import com.Soham.Lovable_Project.DTOs.Subcription.SubcriptionResponse;
import com.Soham.Lovable_Project.Entities.Plan;
import com.stripe.model.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubcriptionMapper {

    SubcriptionResponse toSubcriptionResponse(Subscription subcription);

    PlanReponse toPlanResponse(Plan plan);
}
