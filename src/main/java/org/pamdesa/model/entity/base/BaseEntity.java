package org.pamdesa.model.entity.base;

import lombok.Getter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseReadOnlyEntity {

  @Id
  private String id;

  public BaseEntity() {
    this.id = UUID.randomUUID().toString().replace("-", "");
  }

  @LastModifiedBy @Column(name = "updated_by")
  private String updatedBy;

  @LastModifiedDate @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Version private Integer version;
}
