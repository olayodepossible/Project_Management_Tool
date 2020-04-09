package com.possible.ppmtool.repositories;

import com.possible.ppmtool.model.Backlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository  extends JpaRepository<Backlog, Long> {

    Backlog findByProjectIdentifier(String identifier);
}

