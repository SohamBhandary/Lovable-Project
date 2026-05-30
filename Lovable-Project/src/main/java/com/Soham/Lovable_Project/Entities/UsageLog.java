package com.Soham.Lovable_Project.Entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

public class UsageLog {
    Long id;
    User user;
    Project project;
    String action;
    Integer tokenUsed;
    Integer durationMs;
    String metaData;//JSON of{model_used,prompt_used}
    Instant createdAt;


}
