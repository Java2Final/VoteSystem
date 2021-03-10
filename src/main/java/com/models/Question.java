package com.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "QuestionEntity")
@Table(name = "questions")
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;
    @Column(name = "question_name")
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "question_options",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = {@JoinColumn(name = "option_id")}
    )
    private Set<Option> questionOptions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Option> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(Set<Option> questionOptions) {
        this.questionOptions = questionOptions;
    }

    public void addOption(Option option) {
        questionOptions.add(option);
    }

    public void removeOption(Long optionId) {
        for (Option option : questionOptions) {
            if (optionId.equals(option.getId())) {
                questionOptions.remove(option);
                break;
            }
        }
    }
}
