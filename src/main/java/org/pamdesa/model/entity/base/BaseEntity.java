package org.pamdesa.model.entity.base;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity extends BaseReadOnlyEntity {

  @LastModifiedBy @Column(name = "updated_by")
  private String updatedBy;

  @LastModifiedDate @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Version private Integer version;
}
