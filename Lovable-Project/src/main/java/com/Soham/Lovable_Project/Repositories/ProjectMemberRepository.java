package com.Soham.Lovable_Project.Repositories;

import com.Soham.Lovable_Project.Entities.ProjectMember;
import com.Soham.Lovable_Project.Entities.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findByIdProjectId(Long projectId);
}
