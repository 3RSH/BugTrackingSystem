package dao;

import java.io.Serializable;
import java.util.List;

public interface Dao<T, Integer extends Serializable> {

  List<T> findAll();

  void persist(T entity);

}