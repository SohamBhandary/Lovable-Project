package com.Soham.Lovable_Project.Entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE )
public class User {
     Long id;

     String email;
     String passwordHash;
     String avatarUrl;

    Instant createdAt;
    Instant updatedAt;
    Instant deletedAt;



}
