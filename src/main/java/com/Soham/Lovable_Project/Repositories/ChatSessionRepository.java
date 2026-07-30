package com.Soham.Lovable_Project.Repositories;

import com.Soham.Lovable_Project.Entities.ChatSession;
import com.Soham.Lovable_Project.Entities.ChatSessionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, ChatSessionId> {
}
