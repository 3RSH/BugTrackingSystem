import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.User;
import service.UserService;
import util.HibernateUtil;

public class Main {

  static File dbConfig;
  static UserService userService;
  static User currentUser;


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

        dbConfig.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(dbConfig));

        String config = String.format(HibernateUtil.getConfig(), dbName, dbMode);

        writer.write(config);
        writer.flush();
        writer.close();

        break;
      }

      System.out.println("Введите корректную команду.");

    }

    //User select

    userService = new UserService(dbConfig);

    List<User> users = userService.findAll();

    while (true) {

      if (users.size() > 0) {

        System.out.println("Доступные пользователи:");

        for (User user : users) {
          System.out.println(user.getName());
        }

      }

      System.out.println("Выбрать/создать пользователя для работы: user [name]");

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

      System.out.println("Введите корректную команду.");
    }


  }


}