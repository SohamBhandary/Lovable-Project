package com.Soham.Workspace_Service.DTOs.Project;

public record FileNode(
        String path

) {

    @Override
    public String toString() {
        return path;
    }
}
