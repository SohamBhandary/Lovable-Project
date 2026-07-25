package com.Soham.Intelligence_Service.Repository;


import com.Soham.Intelligence_Service.Entities.ChatSession;
import com.Soham.Intelligence_Service.Entities.ChatSessionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, ChatSessionId> {
}
