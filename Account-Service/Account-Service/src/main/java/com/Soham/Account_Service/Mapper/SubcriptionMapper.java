package com.Soham.Account_Service.Mapper;


import com.Soham.Account_Service.DTOs.Subcription.PlanResponse;
import com.Soham.Account_Service.DTOs.Subcription.SubcriptionResponse;
import com.Soham.Account_Service.Entities.Plan;
import com.Soham.Account_Service.Entities.Subcription;
import com.Soham.Common_Lib.DTOs.PlanDto;
import com.stripe.model.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubcriptionMapper {

    SubcriptionResponse toSubcriptionResponse(Subcription subcription);

    PlanDto toPlanResponse(Plan plan);
}
