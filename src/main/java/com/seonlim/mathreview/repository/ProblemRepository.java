package com.seonlim.mathreview.repository;

import com.seonlim.mathreview.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long>, ProblemQuerydslRepository {
    Optional<Problem> findById(Long id);
}

