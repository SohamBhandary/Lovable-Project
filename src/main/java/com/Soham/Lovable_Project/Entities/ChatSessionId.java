package com.Soham.Lovable_Project.Entities;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@ToString
public class ChatSessionId implements Serializable {
    Long projectId;
    Long userId;
}