package com.repository;

import com.models.Question;
import com.models.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class RoleRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public RoleRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Role> getAllRoles() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Role> cq = cb.createQuery(Role.class);
            Root<Role> rootEntry = cq.from(Role.class);
            CriteriaQuery<Role> all = cq.select(rootEntry);
            TypedQuery<Role> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Role getRole(long id) {
        Role role = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Role> q1 = builder.createQuery(Role.class);
            Root<Role> root = q1.from(Role.class);
            Predicate predicateRole = builder.equal(root.get("id"), id);
            role = session.createQuery(q1.where(predicateRole)).getSingleResult();
            session.getTransaction().commit();
        }
        return role;
    }

    public void updateRole(Role role) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(role);
            session.getTransaction().commit();
        }
    }
}
