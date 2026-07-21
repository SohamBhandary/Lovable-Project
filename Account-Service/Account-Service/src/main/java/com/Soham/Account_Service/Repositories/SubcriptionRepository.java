package com.Soham.Account_Service.Repositories;



import com.Soham.Account_Service.Entities.Subcription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SubcriptionRepository extends JpaRepository<Subcription,Long> {
    Optional<Subcription> findByUserIdAndStatusIn(Long userId, Set<Object> objects);

    boolean existsByStripeSubscriptionId(String subscriptionId);

    Optional<Subcription> findByStripeSubscriptionId(String gatewaySubscriptionId);


}
