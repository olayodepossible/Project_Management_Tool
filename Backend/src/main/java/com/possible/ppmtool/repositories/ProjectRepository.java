package com.possible.ppmtool.repositories;

import com.possible.ppmtool.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByProjectIdentifier(String projectIdentifier);
}
