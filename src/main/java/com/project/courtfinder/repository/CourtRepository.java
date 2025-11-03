package com.project.courtfinder.repository;

import com.project.courtfinder.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
    boolean existsByName(String name);
    List<Court> findByOwnerId(Long ownerId);
}
