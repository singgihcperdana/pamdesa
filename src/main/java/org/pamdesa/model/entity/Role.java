package org.pamdesa.model.entity;

import lombok.Data;
import org.pamdesa.model.enums.UserRole;
import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private UserRole userRole;

}