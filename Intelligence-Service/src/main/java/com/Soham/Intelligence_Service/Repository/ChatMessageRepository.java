package com.Soham.Intelligence_Service.Repository;


import com.Soham.Intelligence_Service.Entities.ChatMessage;
import com.Soham.Intelligence_Service.Entities.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {


    @Query("""
            SELECT DISTINCT m FROM ChatMessage m
            LEFT JOIN FETCH m.events e
            WHERE m.chatSession = :chatSession
            ORDER BY m.createdAt ASC, e.sequenceOrder ASC
            """)
    List<ChatMessage> findByChatSession(ChatSession chatSession);
}
