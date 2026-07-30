package com.Soham.Lovable_Project.Entities;
import com.Soham.Lovable_Project.Enums.SubcriptionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE )
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subcription {

    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(nullable = false,name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name = "plan_id")
    Plan plan;

    @Enumerated(value = EnumType.STRING)
    SubcriptionStatus status;
    String stripeCustomerId;
    String stripeSubscriptionId;
    Instant currentPeriodStart;
    Instant currentPeriodEnd;
    Boolean cancelAtPeriodEnd=false;

    @CreationTimestamp
    Instant createdAt;

    @UpdateTimestamp
    Instant updatedAt;

}
