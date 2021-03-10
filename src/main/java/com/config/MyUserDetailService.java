package com.config;

import com.models.Authority;
import com.models.Role;
import com.models.User;
import com.repository.StudentRepository;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("myUserDetailService")
public class MyUserDetailService implements UserDetailsService {

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public StudentRepository getStudentRepository() {
        return studentRepository;
    }

    private UserRepository userRepository;
    private StudentRepository studentRepository;

    public User getUser() {
        return user;
    }

    User user = null;

    @Autowired
    public MyUserDetailService(UserRepository userRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user = userRepository.findByUserName(username);
        if (user == null) {
            user = studentRepository.findByUserName(username);
        }
        Role role = user.getRole();
        Set<Authority> authorities = role.getAuthorities();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (Authority authority : authorities) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
            grantedAuthorities.add(grantedAuthority);
        }

        GrantedAuthority roleAuthority = new SimpleGrantedAuthority(role.getName());
        grantedAuthorities.add(roleAuthority);

        return buildUserForAuthentication(user, grantedAuthorities);
    }

    private org.springframework.security.core.userdetails.User buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
    }
}
