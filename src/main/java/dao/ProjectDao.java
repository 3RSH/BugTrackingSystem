package dao;

import java.io.File;
import java.util.List;
import model.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class ProjectDao implements Dao<Project, Integer> {

  private final File dbConfig;

  private Session currentSession;
  private Transaction currentTransaction;

  public ProjectDao(File dbConfig) {
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
  }

  public void openCurrentSessionWithTransaction() {
    currentSession = getSessionFactory().openSession();
    currentTransaction = currentSession.beginTransaction();
  }

  public void closeCurrentSessionWithTransaction() {
    currentTransaction.commit();
    currentSession.close();
  }


  @Override
  public List<Project> findAll() {
    return currentSession.createQuery("from Project", Project.class).list();
  }

  @Override
  public void persist(Project entity) {
    currentSession.save(entity);
  }

  @Override
  public Project findById(Integer integer) {
    return currentSession.get(Project.class, integer);
  }

  @Override
  public void delete(Project entity) {
    currentSession.delete(entity);
  }
}