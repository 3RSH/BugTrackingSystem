package service;

import dao.TaskDao;
import java.io.File;
import java.util.List;
import model.Task;

public class TaskService {

  private final TaskDao taskDao;

  public TaskService(File dbConfig) {
    taskDao = new TaskDao(dbConfig);
  }


  public List<Task> findAll() {
    taskDao.openCurrentSession();
    List<Task> tasks = taskDao.findAll();
    taskDao.closeCurrentSession();
    return tasks;
  }

  public void persist(Task task) {
    taskDao.openCurrentSessionWithTransaction();
    taskDao.persist(task);
    taskDao.closeCurrentSessionWithTransaction();
  }

  public Task findById(Integer id) {
    taskDao.openCurrentSession();
    Task task = taskDao.findById(id);
    taskDao.closeCurrentSession();
    return task;
  }

  public void delete(Task task) {
    taskDao.openCurrentSessionWithTransaction();
    taskDao.delete(task);
    taskDao.closeCurrentSessionWithTransaction();
  }
}