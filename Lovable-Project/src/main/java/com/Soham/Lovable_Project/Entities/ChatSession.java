package com.Soham.Lovable_Project.Entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;

import java.time.Instant;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE )
public class ChatSession {
    Project project;
    User user;
    String title;

    Instant createdAt;
    Instant updatedAt;
    Instant deletedAt;
}
