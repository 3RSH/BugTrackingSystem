import java.io.File;
import java.util.List;
import java.util.Scanner;
import model.Task;
import model.User;
import service.DbService;
import service.ProjectService;
import service.TaskService;
import service.UserService;

public class Main {

  private static File dbConfig;
  private static User currentUser;
  private static UserService userService;
  private static ProjectService projectService;
  private static TaskService taskService;

  public static void main(String[] args) {

    while (true) {

      DbService dbService = new DbService();

      if (dbConfig != null) {
        clearWorkData();
      }

      dbConfig = dbService.dbInit();

      if (dbConfig == null) {
        break;
      }

      userService = new UserService(dbConfig);
      projectService = new ProjectService(dbConfig);
      taskService = new TaskService(dbConfig, projectService, userService);

      currentUser = userService.getUser();

      if (currentUser == null) {
        continue;
      }

      while (true) {
        List<String> projectNames = projectService.getAndPrintProjectNamesList();
        taskService.printTaskList();

        System.out.println("Создать проект: add project [project name]");
        System.out.println("Удалить проект: del project [project name]");
        System.out.println("Создать задачу: add task");
        System.out.println("Удалить задачу: del task [task id]");
        System.out.println("Сменить пользователя: exit");

        System.out.print(currentUser.getName() + " -> ");

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

          projectService.addOrDelProject(action, name, projectNames);
          continue;
        }

        if (checkTaskCommand) {

          String action = command.split(" ")[0];

          if (action.equals("add")) {
            Task task = taskService.createTask(currentUser, projectNames);

            if (task != null) {
              taskService.persist(task);
            }
          } else {
            Task task = taskService.findById(Integer.parseInt(command.split(" ")[2]));

            if (task != null) {
              taskService.delete(task);
            }
          }

          continue;
        }

        if (command.matches("exit")) {
          currentUser = userService.getUser();

          if (currentUser == null) {
            break;
          }

          continue;
        }

        System.out.println("Введите корректную команду.");
      }

      clearWorkData();
    }
  }

  private static void clearWorkData() {

    if (dbConfig.delete()) {
      userService = null;
      projectService = null;
      taskService = null;
      currentUser = null;
      dbConfig = null;
    }
  }
}