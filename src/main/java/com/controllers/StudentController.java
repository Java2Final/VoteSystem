package com.controllers;

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

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("student", new Student());
        return "student_login";
    }

    @GetMapping("/")
    public String studentPage(Model model, Authentication authentication) {
        model.addAttribute("student",studentRepository.findByUserName(authentication.getName()));
        return "student_profile";
    }
}
