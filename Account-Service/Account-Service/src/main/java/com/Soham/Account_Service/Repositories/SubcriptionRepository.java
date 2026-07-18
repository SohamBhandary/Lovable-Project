package com.Soham.Account_Service.Repositories;

import com.Soham.Lovable_Project.Entities.Subcription;
import com.stripe.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface SubcriptionRepository extends JpaRepository<Subcription,Long> {
    Optional<Subscription> findByUserIdAndStatusIn(Long userId, Set<Object> objects);

    boolean existsByStripeSubscriptionId(String subscriptionId);

    Optional<Subcription> findByStripeSubscriptionId(String gatewaySubscriptionId);
}
