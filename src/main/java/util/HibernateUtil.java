package util;

import java.io.File;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

  private static StandardServiceRegistry registry;
  private static SessionFactory sessionFactory;

  @Getter
  private static String config = "<!DOCTYPE hibernate-configuration PUBLIC\n"
      + "  \"-//Hibernate/Hibernate Configuration DTD 3.0//EN\"\n"
      + "  \"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd\">\n"
      + "<hibernate-configuration>\n"
      + "  <session-factory>\n"
      + "    <property name=\"connection.driver_class\">org.h2.Driver</property>\n"
      + "    <property name=\"connection.url\">jdbc:h2:./%s</property>\n"
      + "    <property name=\"connection.username\">sa</property>\n"
      + "    <property name=\"connection.password\"></property>\n"
      + "\n"
      + "    <property name=\"connection.pool_size\">1</property>\n"
      + "    <property name=\"dialect\">org.hibernate.dialect.H2Dialect</property>\n"
      + "    <property name=\"show_sql\">true</property>\n"
      + "    <property name=\"current_session_context_class\">thread</property>\n"
      + "    <property name=\"hbm2ddl.auto\">%s</property>\n"
      + "\n"
      + "    <mapping class=\"model.User\"/>\n"
      + "    <mapping class=\"model.Project\"/>\n"
      + "    <mapping class=\"model.Task\"/>\n"
      + "  </session-factory>\n"
      + "</hibernate-configuration>";

  public static SessionFactory getSessionFactory(File configFile) {

    if (sessionFactory == null) {

      sessionFactory = new Configuration().configure(configFile).buildSessionFactory();

    }

    return sessionFactory;
  }

  public static void shutdown() {
    if (registry != null) {
      StandardServiceRegistryBuilder.destroy(registry);
      sessionFactory.close();
      sessionFactory = null;
    }
  }
}