package com.Soham.Lovable_Project.Entities;
import com.Soham.Lovable_Project.Enums.SubcriptionStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE )
public class Subcription {
    Long id;
    User user;
    Plan plan;
    SubcriptionStatus status;
    String stripeCustomerId;
    String stripeSubscriptionId;
    Instant currentPeriodStart;
    Instant currentPeriodEnd;
    Boolean cancelAtPeriodEnd=false;
    Instant createdAt;
    Instant updatedAt;

}
