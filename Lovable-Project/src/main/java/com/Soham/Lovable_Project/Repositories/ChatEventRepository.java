package com.Soham.Lovable_Project.Repositories;

import com.Soham.Lovable_Project.Entities.ChatEvent;
import com.Soham.Lovable_Project.Entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatEventRepository extends JpaRepository<ChatEvent,Long> {
}
