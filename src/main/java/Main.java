import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import model.Project;
import model.Task;
import model.Task.Priority;
import model.Task.TaskType;
import model.User;
import service.ProjectService;
import service.TaskService;
import service.UserService;
import util.HibernateUtil;

public class Main {

  static File dbConfig;
  static User currentUser;
  static UserService userService;
  static ProjectService projectService;
  static TaskService taskService;

  public static void main(String[] args) throws IOException {

    //DB initialization

    while (true) {

      List<File> dbFiles = new ArrayList<>();
      List<String> dbNames = new ArrayList<>();

      File currentDir = new File(".");

      for (File file : currentDir.listFiles()) {

        if (file.getName().matches(".*\\.mv\\.db")) {
          dbFiles.add(file);
        }

      }

      if (dbFiles.size() > 0) {

        System.out.println("Доступные базы:");

        for (File file : dbFiles) {
          String dbName = file.getName().split("\\.mv\\.db")[0];

          dbNames.add(dbName);
          System.out.println(dbName);
        }

      }

      System.out.println("Выбрать/создать базу для работы: start DB [name]");

      Scanner scanner = new Scanner(System.in);

      String input = scanner.nextLine();

      if (input.matches("start DB [A-z0-9]{1,10}")) {

        String dbName = input.split("start DB ")[1];

        String dbMode = "create";

        if (dbNames.contains(dbName)) {
          dbMode = "validate";
        }

        dbConfig = new File(dbName + "_hibernate.cfg.xml");

        BufferedWriter writer = new BufferedWriter(new FileWriter(dbConfig));

        String config = String.format(HibernateUtil.getConfig(), dbName, dbMode);

        writer.write(config);
        writer.close();

        userService = new UserService(dbConfig);
        projectService = new ProjectService(dbConfig);
        taskService = new TaskService(dbConfig);

        break;
      }

      System.out.println("Введите корректную команду.");

    }

    //User select

    while (true) {

      List<User> users = userService.findAll();

      if (users.size() > 0) {

        System.out.println("Доступные пользователи:");

        for (User user : users) {
          System.out.println(user.getName());
        }

      }

      System.out.println("Выбрать/создать пользователя для работы: user [name]");
      System.out.println("Удалить пользователя: del user [name]");

      Scanner scanner = new Scanner(System.in);

      String input = scanner.nextLine();

      if (input.matches("user [A-z0-9]{2,20}")) {

        String userName = input.split("user ")[1];

        for (User user : users) {
          if (user.getName().equals(userName)) {
            currentUser = user;
            break;
          }
        }

        if (currentUser == null) {
          currentUser = new User();
          currentUser.setName(userName);
          userService.persist(currentUser);
        }

        break;
      }

      if (input.matches("del user [A-z0-9]{2,20}")) {

        String name = input.split(" ")[2];
        User user = users.stream().filter(u -> u.getName().equals(name)).findFirst().get();

        userService.delete(user);
        continue;
      }

      System.out.println("Введите корректную команду.");
    }

    //Get Projects and Tasks and next Action

    while (true) {

      System.out.println("Projects:");
      List<Project> projects = projectService.findAll();
      List<String> projectNames =
          projects.stream().map(Project::getName).collect(Collectors.toList());

      for (Project project : projects) {
        System.out.println(
            project.getName() + " <- " + project.getTasks().size() + " tasks");
      }
      System.out.println();

      System.out.println("Tasks:");
      List<Task> tasks = taskService.findAll();

      for (Task task : tasks) {
        System.out.println(
            task.getId() + " " + task.getTopic() + " -> " + task.getProject().getName());
      }
      System.out.println();

      System.out.println("Создать проект: add project [project name]");
      System.out.println("Удалить проект: del project [project name]");
      System.out.println("Создать задачу: add task");
      System.out.println("Удалить задачу: del task [task id]");

      Scanner scanner = new Scanner(System.in);

      String command = scanner.nextLine();

      boolean checkProjectCommand =
          command.matches("add project [A-z0-9]{2,20}")
              || command.matches("del project [A-z0-9]{2,20}");
      boolean checkTaskCommand =
          command.matches("add task")
              || command.matches("del task [0-9]{1,3}");

      if (checkProjectCommand) {

        String action = command.split(" ")[0];
        String name = command.split(" ")[2];

        if ("add".equals(action)) {
          if (!projectNames.contains(name)) {
            Project project = new Project();
            project.setName(name);
            projectService.persist(project);
          }
        } else {
          if (projectNames.contains(name)) {
            Project project =
                projectService.findAll().stream()
                    .filter(p -> p.getName().equals(name)).findFirst().get();

            projectService.delete(project);
          }
        }
        continue;
      }

      if (checkTaskCommand) {

        String action = command.split(" ")[0];

        if ("add".equals(action)) {
          Task task = new Task();
          task.setUser(currentUser);
          System.out.print("Укажите имя проекта задачи: ");
          String projectName = scanner.nextLine();

          if (!projectNames.contains(projectName)) {
            continue;
          }
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
          taskService.persist(task);
        } else {
          taskService.delete(
              taskService.findById(Integer.parseInt(command.split(" ")[2])));
        }

        continue;
      }

      System.out.println("Введите корректную команду.");
    }
  }
}