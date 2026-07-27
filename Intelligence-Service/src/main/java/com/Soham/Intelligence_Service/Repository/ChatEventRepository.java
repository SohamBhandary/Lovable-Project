package com.Soham.Intelligence_Service.Repository;


import com.Soham.Intelligence_Service.Entities.ChatEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatEventRepository extends JpaRepository<ChatEvent,Long> {
    Optional<ChatEvent> findBySagaId(String s);
}
