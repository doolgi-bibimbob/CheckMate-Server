package com.seonlim.mathreview.problem.repository;

import com.seonlim.mathreview.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long>, ProblemQuerydslRepository {
    Optional<Problem> findById(Long id);
}

