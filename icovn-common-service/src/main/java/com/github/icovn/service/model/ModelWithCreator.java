package com.github.icovn.service.model;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class ModelWithCreator extends BaseModel {

  @Column(name = "created_at")
  @CreatedDate
  private Long createdAt;

  @Column(name = "created_by")
  @CreatedBy
  private String createdBy;
}
