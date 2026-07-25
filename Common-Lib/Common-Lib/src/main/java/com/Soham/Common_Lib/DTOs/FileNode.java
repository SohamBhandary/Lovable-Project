package com.Soham.Common_Lib.DTOs;

public record FileNode(
        String path
) {

    @Override
    public String toString() {
        return path;
    }
}