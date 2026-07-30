package com.Soham.Lovable_Project.Mapper;

import com.Soham.Lovable_Project.DTOs.Project.FileNode;
import com.Soham.Lovable_Project.Entities.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {

    List<FileNode> toListOfFileNode(List<ProjectFile> projectFileList);
}
