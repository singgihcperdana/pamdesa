package org.pamdesa.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
@Data
public class Role implements Serializable {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;

  @Column(length = 20) private String name;

}