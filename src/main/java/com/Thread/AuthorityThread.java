package com.thread;

import com.models.Authority;
import com.models.Role;
import com.repository.RoleRepository;

import java.util.List;

public class AuthorityThread extends Thread{
    private final RoleRepository roleRepository;
    private List<Authority> authorities = null;

    public AuthorityThread(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run() {
        authorities = roleRepository.getAllAuthorities();
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }
}
