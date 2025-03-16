package org.pamdesa.model.entity;

import lombok.Data;
import org.pamdesa.model.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Data
@Table(name = "rate")
public class Rate extends BaseEntity implements Serializable {

  @Size(max = 255)
  private String description;

  @Size(min = 1, max = 20)
  private String code;

  private Double amount;

  @ManyToOne(optional = false)
  @JoinColumn(name = "organization_id", nullable = false)
  @NotNull
  private Organization organization;

}
