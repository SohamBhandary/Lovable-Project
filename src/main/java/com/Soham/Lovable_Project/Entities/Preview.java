package com.Soham.Lovable_Project.Entities;

import com.Soham.Lovable_Project.Enums.PreviewStatus;
import com.Soham.Lovable_Project.Enums.ProjectRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE )
public class Preview {
    Long id;
    Project project;
    String namespace;
    String podName;
    String previewUrl;
    PreviewStatus status;
    Instant startedAt;
    Instant terminatedAt;
    Instant createdAt;
}
