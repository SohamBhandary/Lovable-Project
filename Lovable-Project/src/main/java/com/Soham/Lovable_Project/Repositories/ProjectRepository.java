package com.Soham.Lovable_Project.Repositories;

import com.Soham.Lovable_Project.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> bf2ffa8c0de1008ae4eb7065a36828dcd0a749ac

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("""
            select p from Project p where p.deletedAt IS NULL
            AND p.owner.id=:userId ORDER BY p.updatedAt DESC
            
            """)
    List<Project> findAllAccessibleByUser(@Param("userId") Long userId);
<<<<<<< HEAD


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
=======
>>>>>>> bf2ffa8c0de1008ae4eb7065a36828dcd0a749ac
}
