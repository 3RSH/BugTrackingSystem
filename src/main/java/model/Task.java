package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(name = "task")
@Entity
@RequiredArgsConstructor
public class Task {

  @Id
  @Setter
  @Getter
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Setter
  @Getter
  @Column(nullable = false)
  private String topic;

  @Getter
  @Setter
  @Column(nullable = false)
  private String description;

  @Setter
  @Getter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TaskType type;

  @Setter
  @Getter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Priority priority;

  @Getter
  @Setter
  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Setter
  @Getter
  @ManyToOne(optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;


  public enum TaskType {
    MANDATORY, OPTIONAL
  }

  public enum Priority {
    HIGH, LOW
  }
}