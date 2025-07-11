package com.seonlim.mathreview.problem.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private ExamPublisher publisher;

    private int year;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<Problem> problems = new ArrayList<>();
}
