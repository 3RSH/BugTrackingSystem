package service;

import dao.TaskDao;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import model.Project;
import model.Task;
import model.Task.Priority;
import model.Task.TaskType;
import model.User;

public class TaskService {

  private static final Scanner scanner = new Scanner(System.in);

  private final TaskDao taskDao;
  private final UserService userService;
  private final ProjectService projectService;


  public TaskService(File dbConfig, ProjectService projectService, UserService userService) {
    taskDao = new TaskDao(dbConfig);
    this.projectService = projectService;
    this.userService = userService;
  }


  public void printTaskList() {

    System.out.println("Tasks:");
    List<Task> tasks = findAll();

    for (Task task : tasks) {

      User user = userService.findById(task.getUser().getId());

      StringBuilder builder = new StringBuilder()
          .append("Id: ").append(task.getId())
          .append(" Заголовок: ").append(task.getTopic())
          .append(" Испонитель: ").append(user.getName())
          .append(" -> ").append(task.getProject().getName());

      System.out.println(builder.toString());
    }
    System.out.println();
  }


  public Task createTask(User user, List<String> projectNames) {
    Task task = new Task();

    task.setUser(user);
    System.out.print("Укажите имя проекта задачи: ");
    String projectName = scanner.nextLine();

    if (!projectNames.contains(projectName)) {
      return null;
    }

    List<Project> projects = projectService.findAll();
    task.setProject(
        projects.stream().filter(p -> p.getName().equals(projectName)).findFirst().get());

    System.out.print("Введите заголовок задачи: ");
    String topic = scanner.nextLine();
    task.setTopic(topic);

    System.out.print("Введите описание задачи: ");
    String description = scanner.nextLine();
    task.setDescription(description);

    while (true) {
      System.out.print("Укажите тип задачи (MANDATORY/OPTIONAL): ");
      String type = scanner.nextLine();

      if (type.equals(TaskType.MANDATORY.toString())) {
        task.setType(TaskType.MANDATORY);
        break;

      } else if (type.equals(TaskType.OPTIONAL.toString())) {
        task.setType(TaskType.OPTIONAL);
        break;
      }

      System.out.println("Введите корректную команду.");
    }
    while (true) {
      System.out.print("Укажите приоритет задачи (HIGH/LOW): ");
      String priority = scanner.nextLine();

      if (priority.equals(Priority.HIGH.toString())) {
        task.setPriority(Priority.HIGH);
        break;

      } else if (priority.equals(Priority.LOW.toString())) {
        task.setPriority(Priority.LOW);
        break;
      }

      System.out.println("Введите корректную команду.");
    }

    return task;
  }

  private List<Task> findAll() {
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