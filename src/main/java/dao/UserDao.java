package dao;

import java.io.File;
import java.util.List;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class UserDao implements Dao<User, Integer> {

  private File dbConfig;

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

  public Session getCurrentSession() {
    return currentSession;
  }

  public void closeCurrentSession() {
    currentSession.close();
  }

  public Session openCurrentSessionWithTransaction() {
    currentSession = getSessionFactory().openSession();
    currentTransaction = currentSession.beginTransaction();
    return currentSession;
  }

  public void closeCurrentSessionWithTransaction() {
    currentTransaction.commit();
    currentSession.close();
  }


  @Override
  public List<User> findAll() {
    return (List<User>) currentSession.createQuery("from User", User.class).list();
  }

  @Override
  public void persist(User entity) {
    getCurrentSession().save(entity);
  }
}
