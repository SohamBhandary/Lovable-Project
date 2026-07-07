package com.Soham.Lovable_Project.DTOs.Project;

import java.time.Instant;

public record FileNode(
        String path

) {

    @Override
    public String toString() {
        return path;
    }
}
