package com.controllers;

import com.models.Question;
import com.models.User;
import com.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    private final QuestionRepository questionRepository;

    @Autowired
    public MainController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("questions", questionRepository.getAllQuestions());
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/vote/{id}")
    public String borrow(@PathVariable("id") Long id, Model model) {
        Question question = questionRepository.getQuestion(id);
        model.addAttribute("question", question);
        return "vote";
    }
}