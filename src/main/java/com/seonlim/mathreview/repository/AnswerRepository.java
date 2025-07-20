package com.seonlim.mathreview.repository;

import com.seonlim.mathreview.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByProblemId(Long problemId);
    List<Answer> findAllByUserId(Long userId);
    @Query("""
        select a
        from Answer a
        join fetch a.problem p
        left join fetch p.tags t
        join fetch a.user u
        where a.id = :id
    """)
    Optional<Answer> findByIdFetch(Long id);

}
