package org.pamdesa.model.entity;

import lombok.Data;
import org.pamdesa.model.entity.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Data
@Table(name = "rate")
public class Rate extends BaseEntity implements Serializable {

  @Size(min = 3, max = 255)
  private String description;

  private Double amount;
}
