package org.pamdesa.model.entity.base;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseReadOnlyEntity {

  @CreatedBy @Column(name = "created_by", updatable = false) private String createdBy;

  @CreatedDate @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;

}
