package org.example.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoImpl() {
        this(HibernateUtil.getSessionFactory());
    }

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(User user) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            System.err.println("Ошибка при создании пользователя: "
                    + e.getMessage());
        }
    }

    @Override
    public User findById(Long id) {

        try (Session session = sessionFactory.openSession()) {

            return session.get(User.class, id);

        } catch (Exception e) {

            System.err.println("Ошибка при поиске пользователя: "
                    + e.getMessage());

            return null;
        }
    }

    @Override
    public List<User> findAll() {

        try (Session session = sessionFactory.openSession()) {

            return session
                    .createQuery("FROM User", User.class)
                    .getResultList();

        } catch (Exception e) {

            System.err.println("Ошибка при получении пользователей: "
                    + e.getMessage());

            return List.of();
        }
    }

    @Override
    public void update(User user) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            session.merge(user);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            System.err.println("Ошибка при обновлении пользователя: "
                    + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            User user = session.get(User.class, id);

            if (user != null) {
                session.remove(user);
            } else {
                System.out.println("Пользователь с ID " + id + " не найден.");
            }

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            System.err.println("Ошибка при удалении пользователя: "
                    + e.getMessage());
        }
    }
}