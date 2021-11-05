package dao;

import java.io.File;
import java.util.List;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class UserDao implements Dao<User, Integer> {

  private final File dbConfig;

  private Session currentSession;
  private Transaction currentTransaction;

  public UserDao(File dbConfig) {
    this.dbConfig = dbConfig;
  }


  private SessionFactory getSessionFactory() {
    return new Configuration().configure(dbConfig).buildSessionFactory();
  }

  public void openCurrentSession() {
    currentSession = getSessionFactory().openSession();
  }

  public void closeCurrentSession() {
    currentSession.close();
    currentSession.getSessionFactory().close();
  }

  public void openCurrentSessionWithTransaction() {
    currentSession = getSessionFactory().openSession();
    currentTransaction = currentSession.beginTransaction();
  }

  public void closeCurrentSessionWithTransaction() {
    currentTransaction.commit();
    currentSession.close();
    currentSession.getSessionFactory().close();
  }


  @Override
  public List<User> findAll() {
    return currentSession.createQuery("from User", User.class).list();
  }

  @Override
  public void persist(User entity) {
    currentSession.save(entity);
  }

  @Override
  public User findById(Integer integer) {
    return currentSession.get(User.class, integer);
  }

  @Override
  public void delete(User entity) {
    currentSession.delete(entity);
  }
}