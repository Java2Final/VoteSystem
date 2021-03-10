package com.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "StudentEntity")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "students")
public class Student extends User{
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "groupName")
    private String groupName;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "answered_questions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = {@JoinColumn(name = "answered_question_id")}
    )
    private Set<AnsweredQuestion> answeredQuestions = new HashSet<>();

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<AnsweredQuestion> getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(Set<AnsweredQuestion> answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public void answerQuestion(AnsweredQuestion question) {
        answeredQuestions.add(question);
    }
}
