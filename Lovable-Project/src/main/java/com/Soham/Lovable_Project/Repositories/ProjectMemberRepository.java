package com.Soham.Lovable_Project.Repositories;

import com.Soham.Lovable_Project.Entities.ProjectMember;
import com.Soham.Lovable_Project.Entities.ProjectMemberId;
import com.Soham.Lovable_Project.Enums.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findByIdProjectId(Long projectId);

    @Query("""
    SELECT pm.projectRole
    FROM ProjectMember pm
    WHERE pm.id.projectId = :projectId
      AND pm.id.userId = :userId
""")
    Optional<ProjectRole> findRoleByProjectAndUserId(
            @Param("projectId") Long projectId,
            @Param("userId") Long userId
    );
}
