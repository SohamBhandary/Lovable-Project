package com.Soham.Lovable_Project.DTOs.Project;

import java.time.Instant;

public record FileNode(
        String path,
        Instant modifiedAt,
        Long size,
        String type

) {
}
