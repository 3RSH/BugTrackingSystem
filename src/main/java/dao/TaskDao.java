package dao;

import java.io.File;
import java.util.List;
import model.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class TaskDao implements Dao<Task, Integer> {

  private final File dbConfig;

  private Session currentSession;
  private Transaction currentTransaction;

  public TaskDao(File dbConfig) {
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
  public List<Task> findAll() {
    return currentSession.createQuery("from Task", Task.class).list();
  }

  @Override
  public void persist(Task entity) {
    currentSession.save(entity);
  }

  @Override
  public Task findById(Integer integer) {
    return currentSession.get(Task.class, integer);
  }

  @Override
  public void delete(Task entity) {
    currentSession.delete(entity);
  }
}