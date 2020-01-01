package com.github.icovn.service.service;

import com.github.icovn.service.model.BaseModel;
import com.github.icovn.service.repository.CustomRepository;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseServiceSqlSoftDeleteImpl<T extends BaseModel, ID extends Serializable> extends
    BaseServiceSqlImpl<T, ID> {

  public abstract CustomRepository<T, ID> getRepository();

  @Override
  public void delete(ID id) {
    log.info("(delete)id: {}", id);
    T entity = findById(id);
    entity.setDeleted(1);
    getRepository().save(entity);
  }
}
