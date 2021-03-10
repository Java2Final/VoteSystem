package com.repository;

import com.models.Role;
import com.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepository(SessionFactory sessionFactory) {
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

    public User findByUserName(String username) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder1 = session.getCriteriaBuilder();
            CriteriaQuery<User> q1 = builder1.createQuery(User.class);
            Root<User> root1 = q1.from(User.class);
            Predicate predicateUsername = builder1.equal(root1.get("username"), username);
            user = session.createQuery(q1.where(predicateUsername)).getSingleResult();
        } catch (NoResultException noResultException) {
            noResultException.printStackTrace();
        }
        return user;
    }

    public void updateRole(Role role) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(role);
            session.getTransaction().commit();
        }
    }
}