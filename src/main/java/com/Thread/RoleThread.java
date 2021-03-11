package com.thread;

import com.models.Role;
import com.repository.RoleRepository;

public class RoleThread extends Thread{
    private final RoleRepository roleRepository;
    private long id;
    private Role role = null;

    public RoleThread(RoleRepository roleRepository, long id) {
        this.roleRepository = roleRepository;
        this.id = id;
    }

    @Override
    public void run() {
        role = roleRepository.getRole(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }
}
