package com.repository;

import com.models.AnsweredQuestion;
import com.models.Question;
import com.models.Role;
import com.models.Student;
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

    public List<Student> getAllStudents() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Student> cq = cb.createQuery(Student.class);
            Root<Student> rootEntry = cq.from(Student.class);
            CriteriaQuery<Student> all = cq.select(rootEntry);
            TypedQuery<Student> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addStudent(Student student) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder1 = session.getCriteriaBuilder();
            CriteriaQuery<Role> q1 = builder1.createQuery(Role.class);
            Root<Role> root1 = q1.from(Role.class);

            Predicate predicateRole = builder1.equal(root1.get("name"), "ROLE_STUDENT");
            Role role = session.createQuery(q1.where(predicateRole)).getSingleResult();
            student.setRole(role);
            session.persist(student);
            session.getTransaction().commit();
        }
    }

    public AnsweredQuestion getAnsweredQuestion(long id) {
        AnsweredQuestion answeredQuestion = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AnsweredQuestion> q1 = builder.createQuery(AnsweredQuestion.class);
            Root<AnsweredQuestion> root = q1.from(AnsweredQuestion.class);
            Predicate predicateRole = builder.equal(root.get("id"), id);
            answeredQuestion = session.createQuery(q1.where(predicateRole)).getSingleResult();
            session.getTransaction().commit();
        } catch (NoResultException exception) {
            exception.printStackTrace();
        }
        return answeredQuestion;
    }

    public void voteStudent(Student student, AnsweredQuestion answeredQuestion) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            if (getAnsweredQuestion(answeredQuestion.getId()) == null) {
                session.persist(answeredQuestion);
                session.merge(student);
                session.getTransaction().commit();
            }
        }
    }

    public void updateStudent(Student student) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(student);
            session.getTransaction().commit();
        }
    }
}
