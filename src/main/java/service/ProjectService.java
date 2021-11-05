package service;

import dao.ProjectDao;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import model.Project;

public class ProjectService {

  private final ProjectDao projectDao;


  public ProjectService(File dbConfig) {
    projectDao = new ProjectDao(dbConfig);
  }


  public List<String> getAndPrintProjectNamesList() {
    System.out.println("\nProjects:");
    List<Project> projects = findAll();
    List<String> projectNames =
        projects.stream().map(Project::getName).collect(Collectors.toList());

    for (Project project : projects) {
      System.out.println(
          project.getName() + " <- " + project.getTasks().size() + " tasks");
    }
    System.out.println();

    return projectNames;
  }

  public void addOrDelProject(String action, String projectName, List<String> projectNames) {

    if (action.equals("add")) {

      if (!projectNames.contains(projectName)) {
        Project project = new Project();
        project.setName(projectName);
        persist(project);
      }
    } else {

      if (projectNames.contains(projectName)) {
        Project project = findAll().stream()
            .filter(p -> p.getName().equals(projectName))
            .findFirst().get();

        delete(project);
      }
    }
  }


  protected List<Project> findAll() {
    projectDao.openCurrentSession();
    List<Project> projects = projectDao.findAll();
    projectDao.closeCurrentSession();
    return projects;
  }

  private void persist(Project project) {
    projectDao.openCurrentSessionWithTransaction();
    projectDao.persist(project);
    projectDao.closeCurrentSessionWithTransaction();
  }

  private Project findById(Integer id) {
    projectDao.openCurrentSession();
    Project project = projectDao.findById(id);
    projectDao.closeCurrentSession();
    return project;
  }

  private void delete(Project project) {
    projectDao.openCurrentSessionWithTransaction();
    projectDao.delete(project);
    projectDao.closeCurrentSessionWithTransaction();
  }
}