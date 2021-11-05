package service;

import dao.UserDao;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import model.User;

public class UserService {

  private static final String userNameCommand = "user [A-z0-9]{2,20}";
  private static final Scanner scanner = new Scanner(System.in);

  private final UserDao userDao;


  public UserService(File dbConfig) {
    userDao = new UserDao(dbConfig);
  }


  public User getUser() {

    while (true) {

      List<User> users = getUsersList();

      System.out.println("Выбрать/создать пользователя для работы: user [name]");
      System.out.println("Удалить пользователя: del user [name]");
      System.out.println("Вернуться к выбору базы: exit");
      System.out.print("-> ");

      String input = scanner.nextLine();

      if (input.matches(userNameCommand)) {
        String userName = input.split(" ")[1];

        for (User user : users) {

          if (user.getName().equals(userName)) {

            return user;
          }
        }

        User user = new User();
        user.setName(userName);
        persist(user);

        return user;
      } else if (input.matches("del " + userNameCommand)) {

        String userName = input.split(" ")[2];
        User user = users.stream().filter(u -> u.getName().equals(userName)).findFirst().get();

        delete(user);
        continue;
      } else if (input.matches("exit")) {

        return null;
      }

      System.out.println("Введите корректную команду.");
    }
  }


  private List<User> getUsersList() {
    List<User> users = findAll();

    if (users.size() > 0) {
      System.out.println("\nДоступные пользователи:");

      for (User user : users) {
        System.out.println(user.getName());
      }

      System.out.println();
    }

    return users;
  }

  private List<User> findAll() {
    userDao.openCurrentSession();
    List<User> users = userDao.findAll();
    userDao.closeCurrentSession();
    return users;
  }

  private void persist(User user) {
    userDao.openCurrentSessionWithTransaction();
    userDao.persist(user);
    userDao.closeCurrentSessionWithTransaction();
  }

  protected User findById(Integer id) {
    userDao.openCurrentSession();
    User user = userDao.findById(id);
    userDao.closeCurrentSession();
    return user;
  }

  private void delete(User user) {
    userDao.openCurrentSessionWithTransaction();
    userDao.delete(user);
    userDao.closeCurrentSessionWithTransaction();
  }
}