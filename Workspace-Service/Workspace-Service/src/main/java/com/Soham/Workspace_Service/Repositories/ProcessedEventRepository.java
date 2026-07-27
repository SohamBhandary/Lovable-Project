package com.Soham.Workspace_Service.Repositories;


import com.Soham.Workspace_Service.Entities.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {
}
