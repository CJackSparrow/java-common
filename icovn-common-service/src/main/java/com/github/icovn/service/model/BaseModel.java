package com.github.icovn.service.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseModel {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "deleted")
  private Integer deleted = 0;

  @PrePersist
  private void ensureId() {
    this.setId(UUID.randomUUID().toString());
  }
}
