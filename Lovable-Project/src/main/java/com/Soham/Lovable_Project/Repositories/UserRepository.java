package com.Soham.Lovable_Project.Repositories;

import com.Soham.Lovable_Project.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    Optional<User> findByEmail(String email);
}
