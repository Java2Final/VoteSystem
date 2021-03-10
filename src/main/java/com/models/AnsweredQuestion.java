package com.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "AnsweredQuestionEntity")
@Table(name = "answeredQuestions")
public class AnsweredQuestion implements Serializable {

    @Id
    @Column(name = "answered_question_id")
    private Long id;
    @Column(name = "answered_option_id")
    private Long option_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOptionId() {
        return option_id;
    }

    public void setOptionId(Long option_id) {
        this.option_id = option_id;
    }
}
