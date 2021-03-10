package com.controllers;

import com.Thread.AuthorityThread;
import com.Thread.RoleThread;
import com.models.*;
import com.repository.QuestionRepository;
import com.repository.RoleRepository;
import com.repository.StudentRepository;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public void PasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public AdminController(UserRepository userRepository, QuestionRepository questionRepository, RoleRepository roleRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.roleRepository = roleRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/")
    public String adminPage(Model model) {
        model.addAttribute("questions", questionRepository.getAllQuestions());
        model.addAttribute("roles", userRepository.getAllRoles());
        model.addAttribute("students",studentRepository.getAllStudents());
        return "admin_page";
    }

    @GetMapping("/add")
    public String add() {
        return "add";
    }

    @PostMapping("/addRole")
    public String addRole(Role role) {
        if (roleRepository.getRole(role.getName()) == null) {
            roleRepository.addRole(role);
        }
        return "redirect:/admin/";
    }

    @GetMapping("/role/{id}")
    public String role(@PathVariable("id") Long id, Model model) throws InterruptedException {
        RoleThread roleThread = new RoleThread(roleRepository, id);
        AuthorityThread authorityThread = new AuthorityThread(roleRepository);
        roleThread.start();
        authorityThread.start();
        roleThread.join();
        authorityThread.join();
        Role role = roleThread.getRole();
        List<Authority> authorities = authorityThread.getAuthorities();
        model.addAttribute("role", role);
        model.addAttribute("authorities", authorities);
        return "role";
    }

    @GetMapping("/role/add/{roleId}/{authorityId}")
    public String roleAdd(@PathVariable("roleId") Long roleId, @PathVariable("authorityId") Long authorityId) {
        Role role = roleRepository.getRole(roleId);
        Set<Authority> temp = role.getAuthorities();
        if (temp.add(roleRepository.getAuthority(authorityId))) {
            role.setAuthorities(temp);
        }
        roleRepository.updateRole(role);
        return "redirect:/admin/";
    }

    @GetMapping("/role/delete/{roleId}/{authorityId}")
    public String roleDelete(@PathVariable("roleId") Long roleId, @PathVariable("authorityId") Long authorityId) {
        Role role = roleRepository.getRole(roleId);
        role.removeAuthority(roleRepository.getAuthority(authorityId));
        roleRepository.updateRole(role);
        return "redirect:/admin/";
    }

    @PostMapping("/addQuestion")
    public String addQuestion(Question question) {
        questionRepository.addQuestion(question);
        return "redirect:/admin/";
    }

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id, Model model) {
        Question question = questionRepository.getQuestion(id);
        model.addAttribute("question", question);
        model.addAttribute("statistic",getStatistics(question));
        return "question";
    }

    @PostMapping("/question/add/{id}")
    public String questionAdd(@PathVariable("id") Long id, @RequestParam("optionName") String optionName) {
        Question question = questionRepository.getQuestion(id);
        questionRepository.addOption(new Option(optionName));
        question.addOption(questionRepository.getOption(optionName));
        questionRepository.updateQuestion(question);
        return "redirect:/admin/";
    }

    @GetMapping("/question/delete/{questionId}/{optionId}")
    public String questionDelete(@PathVariable("questionId") Long questionId, @PathVariable("optionId") Long optionId) {
        Question question = questionRepository.getQuestion(questionId);
        question.removeOption(optionId);
        questionRepository.updateQuestion(question);
        return "redirect:/admin/";
    }

    @PostMapping("/addStudent")
    public String addStudent(Student student) {
        if (studentRepository.findByUserName(student.getUsername()) == null) {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
            studentRepository.addStudent(student);
        }
        return "redirect:/admin/";
    }

    @GetMapping("/student/{username}")
    public String student(@PathVariable("username") String username, Model model) {
        Student student = studentRepository.findByUserName(username);
        model.addAttribute("student", student);
        model.addAttribute("roles", roleRepository.getAllRoles());
        return "student";
    }

    @GetMapping("/student/assign/{username}/{roleId}")
    public String studentAssign(@PathVariable("username") String username, @PathVariable("roleId") Long roleId) {
        Student student = studentRepository.findByUserName(username);
        Role role = roleRepository.getRole(roleId);
        student.setRole(role);
        studentRepository.updateStudent(student);
        return "redirect:/admin/";
    }

    public Map<Option, Integer> getStatistics(Question question) {
        List<Student> students = studentRepository.getAllStudents();
        Map<Option, Integer> statistics = new HashMap<>();
        int totalCount = 0;
        for (Student student : students) {
            if (student.containsQuestion(question)) {
                totalCount += 1;
            }
        }
        for (Option option : question.getQuestionOptions()) {
            int counter = 0;
            for (Student student : students) {
                for (AnsweredQuestion answeredQuestion : student.getAnsweredQuestions()) {
                    if (answeredQuestion.getOptionId().equals(option.getId())) {
                        counter += 1;
                    }
                }
            }
            double percentage = (((double) counter) / totalCount) * 100;
            statistics.put(option, (int) percentage);
        }
        return statistics;
    }
}