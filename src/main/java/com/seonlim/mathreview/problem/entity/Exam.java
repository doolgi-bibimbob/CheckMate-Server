package com.seonlim.mathreview.problem.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate date;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<Problem> problems = new ArrayList<>();
}
