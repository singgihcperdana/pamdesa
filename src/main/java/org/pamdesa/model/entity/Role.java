package org.pamdesa.model.entity;

import lombok.Data;
import org.pamdesa.model.enums.UserRole;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
@Data
public class Role extends BaseEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private UserRole userRole;

  @Version
  private Integer version;
}