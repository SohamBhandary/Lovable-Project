package com.Soham.Intelligence_Service.Repository;


import com.Soham.Intelligence_Service.Entities.ChatEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatEventRepository extends JpaRepository<ChatEvent,Long> {
}
