package com.seonlim.mathreview.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAnnotation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Answer answer;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id")
    private Review review;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "pos_x")),
            @AttributeOverride(name = "y", column = @Column(name = "pos_y"))
    })
    private Position position;

    private int width;
    private int height;

    @Builder
    public ReviewAnnotation(Answer answer,
                            String imageUrl,
                            String content,
                            Position position,
                            int width, int height) {

        Objects.requireNonNull(answer, "answer는 필수입니다.");
        Objects.requireNonNull(position, "position은 필수입니다.");
        if ((imageUrl == null || imageUrl.isBlank()) &&
                (content  == null || content.isBlank())) {
            throw new IllegalArgumentException("imageUrl과 content 중 하나는 반드시 존재해야 합니다.");
        }

        this.answer   = answer;
        this.imageUrl = imageUrl;
        this.content  = content;
        this.position = position;
        this.width    = width;
        this.height   = height;
    }
}
