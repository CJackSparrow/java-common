package com.github.icovn.service.model;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class ModelWithUpdater extends ModelWithCreator {

  @Column(name = "updated_at")
  @LastModifiedDate
  private Long updatedAt;

  @Column(name = "updated_by")
  @LastModifiedBy
  private String updatedBy;
}
