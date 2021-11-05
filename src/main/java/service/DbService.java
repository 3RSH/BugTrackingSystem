package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import org.hibernate.cfg.Configuration;

public class DbService {

  private static final String config = "<!DOCTYPE hibernate-configuration PUBLIC\n"
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
      + "    <property name=\"show_sql\">false</property>\n"
      + "    <property name=\"current_session_context_class\">thread</property>\n"
      + "    <property name=\"hbm2ddl.auto\">%s</property>\n"
      + "\n"
      + "    <mapping class=\"model.User\"/>\n"
      + "    <mapping class=\"model.Project\"/>\n"
      + "    <mapping class=\"model.Task\"/>\n"
      + "  </session-factory>\n"
      + "</hibernate-configuration>";

  private static final String dbFileExtension = "\\.mv\\.db";
  private static final String startDbCommand = "start db ";

  private static final Scanner scanner = new Scanner(System.in);


  public File dbInit() {

    while (true) {

      List<String> dbNames = getDbNames();

      System.out.println("Выбрать/создать базу для работы: start db [db name]");
      System.out.println("Выход из программы: exit");
      System.out.print("-> ");
      String input = scanner.nextLine();

      if (input.matches(startDbCommand + "[A-z0-9]{1,10}")) {

        String dbName = input.split(startDbCommand)[1];
        String dbMode = "create";

        if (dbNames.contains(dbName)) {
          dbMode = "validate";
        }

        File dbConfig = createHibernateConfiguration(dbName, dbMode);

        if (dbMode.equals("create")) {
          new Configuration().configure(dbConfig).buildSessionFactory().close();
          dbConfig.delete();
          dbMode = "validate";
          dbConfig = createHibernateConfiguration(dbName, dbMode);
        }

        return dbConfig;
      } else if (input.matches("exit")) {

        return null;
      }

      System.out.println("Введите корректную команду.");
    }
  }

  private File createHibernateConfiguration(String dbName, String dbMode) {

    File dbConfig = new File(dbName + "_hibernate.cfg.xml");

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(dbConfig))) {

      String hibernateConfig = String.format(config, dbName, dbMode);
      writer.write(hibernateConfig);
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return dbConfig;
  }

  private List<String> getDbNames() {

    List<String> dbNames = new ArrayList<>();
    List<File> dbFiles = new ArrayList<>();
    File currentDir = new File(".");

    for (File file : Objects.requireNonNull(currentDir.listFiles())) {

      if (file.getName().matches(".*" + dbFileExtension)) {
        dbFiles.add(file);
      }
    }

    if (dbFiles.size() > 0) {

      System.out.println("\nДоступные базы:");

      for (File file : dbFiles) {
        String dbName = file.getName().split(dbFileExtension)[0];

        dbNames.add(dbName);
        System.out.println(dbName);
      }

      System.out.println();
    }

    return dbNames;
  }
}