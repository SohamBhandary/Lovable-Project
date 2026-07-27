package com.Soham.Intelligence_Service.Entities;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class ChatSessionId implements Serializable {
    Long projectId;
    Long userId;
}