package service;

import dao.ProjectDao;
import java.io.File;
import java.util.List;
import model.Project;

public class ProjectService {

  private static ProjectDao projectDao;

  public ProjectService(File dbConfig) {
    projectDao = new ProjectDao(dbConfig);
  }


  public List<Project> findAll() {
    projectDao.openCurrentSession();
    List<Project> projects = projectDao.findAll();
    projectDao.closeCurrentSession();
    return projects;
  }

  public void persist(Project project) {
    projectDao.openCurrentSessionWithTransaction();
    projectDao.persist(project);
    projectDao.closeCurrentSessionWithTransaction();
  }

  public Project findById(Integer id) {
    projectDao.openCurrentSession();
    Project project = projectDao.findById(id);
    projectDao.closeCurrentSession();
    return project;
  }

  public void delete(Project project) {
    projectDao.openCurrentSessionWithTransaction();
    projectDao.delete(project);
    projectDao.closeCurrentSessionWithTransaction();
  }
}
