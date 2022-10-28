package com.tougher.app.v1.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tougher.app.v1.model.ref.Occupation;

public interface OccupationRepository extends JpaRepository<Occupation, Long> {

}
