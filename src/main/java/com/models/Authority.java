package com.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "AuthorityEntity")
@Table(name = "authorities")
public class Authority implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;
    @Column(name = "authority_name")
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    public long getId() {
        return this.id;
    }

    public void setId(long authority_id) {
        this.id = authority_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String authority_name) {
        this.name = authority_name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}