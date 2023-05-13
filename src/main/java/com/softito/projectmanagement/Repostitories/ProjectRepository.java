package com.softito.projectmanagement.Repostitories;

import com.softito.projectmanagement.Models.Project;
import com.softito.projectmanagement.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    //Project findByinvitedid(String invitedid);

    /*@Query("SELECT id FROM Project WHERE inviteid = :inviteid")
    public Project findProjectIdByInviteid(@Param("inviteid") String inviteid);*/

    @Query("SELECT p FROM Project p WHERE p.inviteid = :inviteid")
    public Project findByInviteid(@Param("inviteid") String inviteid);
}
