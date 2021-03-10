package com.repository;

import com.models.Authority;
import com.models.Question;
import com.models.Role;
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
public class RoleRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public RoleRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addAuthority(Authority authority, Role role) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            role.getAuthorities().add(authority);
            session.merge(role);
            session.getTransaction().commit();
        }
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

    public List<Authority> getAllAuthorities() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Authority> cq = cb.createQuery(Authority.class);
            Root<Authority> rootEntry = cq.from(Authority.class);
            CriteriaQuery<Authority> all = cq.select(rootEntry);
            TypedQuery<Authority> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Authority getAuthority(long id) {
        Authority authority = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Authority> q1 = builder.createQuery(Authority.class);
            Root<Authority> root = q1.from(Authority.class);
            Predicate predicateAuthority = builder.equal(root.get("id"), id);
            authority = session.createQuery(q1.where(predicateAuthority)).getSingleResult();
            session.getTransaction().commit();
        } catch (NoResultException exception) {
            exception.printStackTrace();
        }
        return authority;
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
        } catch (NoResultException exception) {
            exception.printStackTrace();
        }
        return role;
    }

    public Role getRole(String name) {
        Role role = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Role> q1 = builder.createQuery(Role.class);
            Root<Role> root = q1.from(Role.class);
            Predicate predicateRole = builder.equal(root.get("name"), name);
            role = session.createQuery(q1.where(predicateRole)).getSingleResult();
            session.getTransaction().commit();
        } catch (NoResultException exception) {
            exception.printStackTrace();
        }
        return role;
    }

    public void addRole(Role role) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(role);
            session.getTransaction().commit();
        }
    }

    public void updateRole(Role role) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(role);
            session.getTransaction().commit();
        }
    }
}
