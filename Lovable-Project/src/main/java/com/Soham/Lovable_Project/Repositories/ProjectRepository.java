package com.Soham.Lovable_Project.Repositories;

import com.Soham.Lovable_Project.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("""
            select p from Project p where p.deletedAt IS NULL
            AND p.owner.id=:userId ORDER BY p.updatedAt DESC
            
            """)
    List<Project> findAllAccessibleByUser(@Param("userId") Long userId);



    @Query("""
            Select p from Project p
            Left join fetch p.owner
            where p.id=:projectId
            and p.deletedAt is null
            and p.owner.id=:userId
            
            
            
            """)
    Optional<Project> findAccessibleProjectById(@Param("projectId") Long projectId,
                                               @Param("userId") Long userId
                                               );

}
