package com.Soham.Workspace_Service.Mapper;


import com.Soham.Workspace_Service.DTOs.Member.MemberResponse;
import com.Soham.Workspace_Service.Entities.ProjectMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberRepsonseMapper {



        @Mapping(target = "userId", source = "id")
        @Mapping(target = "role", constant = "OWNER")
//        MemberResponse toProjectMemberResponseFromOwner(User owner);

        @Mapping(target = "userId", source = "user.id")
        @Mapping(target = "username", source = "user.username")
        @Mapping(target = "name", source = "user.name")
        @Mapping(target = "role", source = "projectRole")
        MemberResponse toProjectMemberResponseFromMember(ProjectMember projectMember);
    }
