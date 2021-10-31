import java.util.List;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class Main {

  public static void main(String[] args) {

    User user = new User("Ivan");

    try (Session session = HibernateUtil.getSessionFactory().openSession()) {

      Transaction transaction = session.beginTransaction();

      session.save(user);

      transaction.commit();
    }

    try (Session session = HibernateUtil.getSessionFactory().openSession()) {

      List<User> users = session.createQuery("from User", User.class).list();
      users.forEach(u -> System.out.println(u.getId() + " " + u.getName()));
    }
  }
}