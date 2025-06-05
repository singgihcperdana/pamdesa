package org.pamdesa.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pamdesa.model.entity.base.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "organization")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Organization extends BaseEntity implements Serializable {

  @Size(max = 10)
  private String code;

  @Size(max = 100)
  private String name;

  @Size(max = 255)
  private String address;

  @Size(max = 255)
  private String description;

  @Size(max = 255)
  private String logo;

  @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<User> users;

}
