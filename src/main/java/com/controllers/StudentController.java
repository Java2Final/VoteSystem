package com.controllers;

import com.models.AnsweredQuestion;
import com.models.Option;
import com.models.Student;
import org.springframework.security.core.Authentication;
import com.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.repository.StudentRepository;

@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public void PasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public StudentController(StudentRepository studentRepository, QuestionRepository questionRepository) {
        this.studentRepository = studentRepository;
        this.questionRepository = questionRepository;
    }

    @GetMapping("/")
    public String studentPage(Model model, Authentication authentication) {
        Student student = studentRepository.findByUserName(authentication.getName());
        model.addAttribute("student",student);
        model.addAttribute("answered_questions",student.getAnsweredQuestions());
        model.addAttribute("questions",questionRepository.getAllQuestions());
        model.addAttribute("options",questionRepository.getAllOptions());
        return "student_profile";
    }

    @PostMapping("/update")
    public String update(Student student, Authentication authentication) {
        Student student1 = studentRepository.findByUserName(authentication.getName());
        student1.setUsername(student.getUsername());
        student1.setPassword(passwordEncoder.encode(student.getPassword()));
        student1.setFirstname(student.getFirstname());
        student1.setLastname(student.getLastname());
        student1.setGroupName(student.getGroupName());
        studentRepository.updateStudent(student1);
        return "redirect:/student/";
    }

    @PostMapping("/vote/{id}")
    public String vote(@PathVariable("id") long id, @RequestParam("option") String optionName, Authentication authentication) {
        Student student = studentRepository.findByUserName(authentication.getName());
        System.out.println(id);
        if (questionRepository.getQuestion(id) != null) {
            Option option = questionRepository.getOption(optionName);
            AnsweredQuestion answeredQuestion = new AnsweredQuestion();
            System.out.println(id);
            System.out.println(optionName);
            System.out.println(option.getId());
            answeredQuestion.setId(id);
            answeredQuestion.setOptionId(option.getId());
            student.answerQuestion(answeredQuestion);
            studentRepository.voteStudent(student, answeredQuestion);
        }
        return "redirect:/student/";
    }
}
