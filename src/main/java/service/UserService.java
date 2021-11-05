package service;

import dao.UserDao;
import java.io.File;
import java.util.List;
import model.User;

public class UserService {

  private static UserDao userDao;

  public UserService(File dbConfig) {
    userDao = new UserDao(dbConfig);
  }


  public List<User> findAll() {
    userDao.openCurrentSession();
    List<User> users = userDao.findAll();
    userDao.closeCurrentSession();
    return users;
  }

  public void persist(User user) {
    userDao.openCurrentSessionWithTransaction();
    userDao.persist(user);
    userDao.closeCurrentSessionWithTransaction();
  }

  public User findById(Integer id) {
    userDao.openCurrentSession();
    User user = userDao.findById(id);
    userDao.closeCurrentSession();
    return user;
  }

  public void delete(User user) {
    userDao.openCurrentSessionWithTransaction();
    userDao.delete(user);
    userDao.closeCurrentSessionWithTransaction();
  }
}