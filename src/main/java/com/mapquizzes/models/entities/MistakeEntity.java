package com.mapquizzes.models.entities;

import com.mapquizzes.models.entities.compositekeys.MistakeId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "quizzes", name = "mistakes")
@IdClass(MistakeId.class)
public class MistakeEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
    @Id
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private QuestionEntity question;
    @Column(name = "number_of_mistakes")
    private Integer numberOfMistakes;
}
