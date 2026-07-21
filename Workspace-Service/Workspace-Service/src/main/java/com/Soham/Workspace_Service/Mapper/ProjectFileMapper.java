package com.Soham.Workspace_Service.Mapper;


import com.Soham.Workspace_Service.DTOs.Project.FileNode;
import com.Soham.Workspace_Service.Entities.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {

    List<FileNode> toListOfFileNode(List<ProjectFile> projectFileList);
}
