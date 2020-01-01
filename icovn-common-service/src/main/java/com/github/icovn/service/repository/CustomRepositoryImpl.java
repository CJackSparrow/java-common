package com.github.icovn.service.repository;

import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

public class CustomRepositoryImpl<T, ID extends Serializable>
    extends SimpleJpaRepository<T, ID> implements CustomRepository<T, ID> {

  private final EntityManager entityManager;

  public CustomRepositoryImpl(JpaEntityInformation entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  @Override
  public void clear() {
    entityManager.clear();
  }

  @Override
  @Transactional
  public void refresh(T t) {
    entityManager.refresh(t);
  }

  @Override
  public T findByIdWithLock(ID id, Class<T> tClass) {
    return entityManager.find(tClass, id, PESSIMISTIC_WRITE);
  }
}
