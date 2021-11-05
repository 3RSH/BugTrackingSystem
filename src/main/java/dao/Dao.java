package dao;

import java.io.Serializable;
import java.util.List;

public interface Dao<T, Id extends Serializable> {

  List<T> findAll();

  void persist(T entity);

  T findById(Id id);

  void delete(T entity);
}