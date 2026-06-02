package com.Soham.Lovable_Project.Mapper;

import com.Soham.Lovable_Project.DTOs.Member.MemberResponse;
import com.Soham.Lovable_Project.Entities.ProjectMember;
import com.Soham.Lovable_Project.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberRepsonseMapper {

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "avatarURL", source = "user.avatarUrl")
    @Mapping(target = "projectRole", source = "projectRole")
    @Mapping(target = "name", ignore = true)
    MemberResponse toProjectResponseFromMember(ProjectMember member);

    @Mapping(target = "projectRole", constant = "OWNER")
    @Mapping(target = "invitedAt", ignore = true)
    @Mapping(target = "avatarURL", source = "avatarUrl")
    @Mapping(target = "name", ignore = true)
    MemberResponse toProjectResponseFromOwner(User owner);
}