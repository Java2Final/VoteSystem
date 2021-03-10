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
    @Column(name = "age")
    private int age;
    @Column(name = "interests")
    private String interests;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "answered_questions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = {@JoinColumn(name = "question_id")}
    )
    private Set<Question> answeredQuestions = new HashSet<>();

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public Set<Question> getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(Set<Question> answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public void answerQuestion(Question question) {
        answeredQuestions.add(question);
    }
}
