package com.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "RoleEntity")
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    @Column(name = "role_name")
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_authorities",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities = new HashSet<>();

    public long getId() {
        return this.id;
    }

    public void setId(long role_id) {
        this.id = role_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String role_name) {
        this.name = role_name;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public boolean checkAuthority(Authority checkingAuthority) {
        for (Authority authority : authorities) if (authority.getId() == checkingAuthority.getId()) return true;
        return false;
    }

    public void removeAuthority(Authority removingAuthority) {
        for (Authority authority : authorities)
            if (authority.getId() == removingAuthority.getId()) {
                authorities.remove(authority);
                return;
            }
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}