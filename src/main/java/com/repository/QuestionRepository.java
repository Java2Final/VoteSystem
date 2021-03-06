package com.repository;

import com.models.Option;
import com.models.Question;
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
import java.util.*;

@Repository
public class QuestionRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public QuestionRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Question> getAllQuestions() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Question> cq = cb.createQuery(Question.class);
            Root<Question> rootEntry = cq.from(Question.class);
            CriteriaQuery<Question> all = cq.select(rootEntry);
            TypedQuery<Question> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Option> getAllOptions() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Option> cq = cb.createQuery(Option.class);
            Root<Option> rootEntry = cq.from(Option.class);
            CriteriaQuery<Option> all = cq.select(rootEntry);
            TypedQuery<Option> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Question getQuestion(long id) {
        Question question = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Question> q1 = builder.createQuery(Question.class);
            Root<Question> root = q1.from(Question.class);
            Predicate predicateQuestion = builder.equal(root.get("id"), id);
            question = session.createQuery(q1.where(predicateQuestion)).getSingleResult();
            session.getTransaction().commit();
        } catch (NoResultException exception) {
            exception.printStackTrace();
        }
        return question;
    }

    public void addQuestion(Question question) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(question);
            session.getTransaction().commit();
        }
    }

    public void addOption(Option option) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            if (getOption(option.getName()) == null) {
                session.persist(option);
                session.getTransaction().commit();
            }
        }
    }
    public Option getOption(String optionName) {
        Option option = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Option> q1 = builder.createQuery(Option.class);
            Root<Option> root = q1.from(Option.class);
            Predicate predicateRole = builder.equal(root.get("name"), optionName);
            option = session.createQuery(q1.where(predicateRole)).getSingleResult();
            session.getTransaction().commit();
        } catch (NoResultException exception) {
            exception.printStackTrace();
        }
        return option;
    }

    public void updateQuestion(Question question) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(question);
            session.getTransaction().commit();
        }
    }

    public void deleteQuestion(Question question) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(question);
            session.getTransaction().commit();
        }
    }
}
