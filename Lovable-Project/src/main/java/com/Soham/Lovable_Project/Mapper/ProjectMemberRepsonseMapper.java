package com.Soham.Lovable_Project.Mapper;

import com.Soham.Lovable_Project.DTOs.Member.MemberResponse;
import com.Soham.Lovable_Project.Entities.ProjectMember;
import com.Soham.Lovable_Project.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberRepsonseMapper {



        @Mapping(target = "userId", source = "id")
        @Mapping(target = "projectRole", constant = "OWNER")
        MemberResponse toProjectMemberResponseFromOwner(User owner);

        @Mapping(target = "userId", source = "user.id")
        @Mapping(target = "name", source = "user.username")
        @Mapping(target = "username", source = "user.name")

        MemberResponse toProjectResponseFromMember(ProjectMember member);
    }
