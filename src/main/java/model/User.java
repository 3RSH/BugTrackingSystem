package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(name = "user")
@Entity
@RequiredArgsConstructor
public class User {

  @Id
  @Getter
  @Setter
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Getter
  @Setter
  @Column(nullable = false, unique = true)
  private String name;

  @Getter
  @OneToMany(mappedBy = "user", cascade = jakarta.persistence.CascadeType.ALL)
  private List<Task> tasks;

  public User(String name) {
    this.name = name;
  }
}