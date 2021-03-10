package com.controllers;

import com.models.Question;
import com.models.Role;
import com.models.User;
import com.repository.QuestionRepository;
import com.repository.RoleRepository;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public void PasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public AdminController(UserRepository userRepository, QuestionRepository questionRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("admin", new User());
        return "admin_login";
    }

    @GetMapping("/")
    public String adminPage(Model model) {
        model.addAttribute("questions", questionRepository.getAllQuestions());
        model.addAttribute("roles", userRepository.getAllRoles());
        return "admin_page";
    }

    @GetMapping("/add")
    public String add() {
        return "add_question";
    }

    @PostMapping("/add")
    public String addQuestion(Question question) {
        questionRepository.addQuestion(question);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        Question question = questionRepository.getQuestion(id);
        model.addAttribute("question", question);
        return "question";
    }

    @PostMapping("/update/{id}")
    public String updateQuestion(@PathVariable("id") Long id, Question question) {
        Question question1 = questionRepository.getQuestion(id);
        question1.setName(question.getName());
        questionRepository.updateQuestion(question1);
        return "redirect:/admin";
    }

    @GetMapping("/update2/{id}")
    public String update2(@PathVariable("id") Long id, Model model) {
        Role role = roleRepository.getRole(id);
        model.addAttribute("role", role);
        return "role";
    }

    @PostMapping("/update2/{id}")
    public String updateRole(@PathVariable("id") Long id, Role role) {
        Role role1 = roleRepository.getRole(role.getId());
        role1.setName(role.getName());
        role1.setAuthorities(role.getAuthorities());
        roleRepository.updateRole(role1);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable("id") Long id) {
        Question question = questionRepository.getQuestion(id);
        questionRepository.deleteQuestion(question);
        return "redirect:/admin";
    }
}