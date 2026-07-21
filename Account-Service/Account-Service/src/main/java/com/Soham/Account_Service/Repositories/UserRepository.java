package com.Soham.Account_Service.Repositories;


import com.Soham.Account_Service.Entities.User;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    Optional<User> findByUsername(String email);

    Optional<User> findByUsernameIgnoreCase(String email);
}
