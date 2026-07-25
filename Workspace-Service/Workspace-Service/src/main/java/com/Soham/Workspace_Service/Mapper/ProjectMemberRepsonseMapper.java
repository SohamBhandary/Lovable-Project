package com.Soham.Workspace_Service.Mapper;


import com.Soham.Workspace_Service.DTOs.Member.MemberResponse;
import com.Soham.Workspace_Service.Entities.ProjectMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberRepsonseMapper {



        @Mapping(target = "userId", source = "id.userId")
        MemberResponse toProjectMemberResponseFromMember(ProjectMember projectMember);
    }
