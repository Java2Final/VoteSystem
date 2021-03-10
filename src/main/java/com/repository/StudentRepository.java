package com.repository;

import com.models.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class StudentRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public StudentRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Student findByUserName(String username) {
        Student student = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder1 = session.getCriteriaBuilder();
            CriteriaQuery<Student> q1 = builder1.createQuery(Student.class);
            Root<Student> root1 = q1.from(Student.class);
            Predicate predicateUsername = builder1.equal(root1.get("username"), username);
            student = session.createQuery(q1.where(predicateUsername)).getSingleResult();
        } catch (NoResultException noResultException) {
            noResultException.printStackTrace();
        }
        return student;
    }

    public void updateStudent(Student student) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(student);
            session.getTransaction().commit();
        }
    }
}
