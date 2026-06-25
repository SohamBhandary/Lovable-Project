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
        @Mapping(target = "invitedAt", ignore = true)
        MemberResponse toProjectResponseFromOwner(User owner);

        @Mapping(target = "userId", source = "user.id")


//        @Mapping(target = "name", source = "user.name")

        @Mapping(target = "projectRole", source = "projectRole")
        @Mapping(target = "invitedAt", source = "invitedAt")

        MemberResponse toProjectResponseFromMember(ProjectMember member);
    }
