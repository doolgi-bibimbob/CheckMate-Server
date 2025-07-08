package com.seonlim.mathreview.user.repository;

import com.seonlim.mathreview.user.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
