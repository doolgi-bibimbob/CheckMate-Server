package com.seonlim.mathreview.repository;

import com.seonlim.mathreview.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByProblemId(Long problemId);
    List<Answer> findAllByUserId(Long userId);
}
