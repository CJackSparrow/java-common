package com.github.icovn.try_custom_repository;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class BasicEntity {

  @Id
  @GeneratedValue
  private Long id;

  private Boolean isActive;
}
