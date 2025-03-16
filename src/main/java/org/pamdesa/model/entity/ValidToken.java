package org.pamdesa.model.entity;

import lombok.Data;
import org.pamdesa.model.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "valid_token")
@Data
public class ValidToken extends BaseEntity implements Serializable {

  @Column(nullable = false, length = 500, unique = true)
  private String token;

  @Column(name = "expiration_time", nullable = false)
  private LocalDateTime expirationTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

}
