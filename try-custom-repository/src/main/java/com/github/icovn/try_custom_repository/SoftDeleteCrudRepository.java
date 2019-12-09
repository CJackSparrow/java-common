package com.github.icovn.try_custom_repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface SoftDeleteCrudRepository<T extends BasicEntity, ID extends Long> extends
    JpaRepository<T, ID> {
  @Override
  @Transactional(readOnly = true)
  @Query("select e from #{#entityName} e where e.isActive = true")
  List<T> findAll();

  @Transactional(readOnly = true)
  @Query("select e from #{#entityName} e where e.id in ?1 and e.isActive = true")
  Iterable<T> findAll(Iterable<ID> ids);

  @Transactional(readOnly = true)
  @Query("select e from #{#entityName} e where e.id = ?1 and e.isActive = true")
  T findOne(ID id);

  //Look up deleted entities
  @Query("select e from #{#entityName} e where e.isActive = false")
  @Transactional(readOnly = true)
  List<T> findInactive();

  @Override
  @Transactional(readOnly = true)
  @Query("select count(e) from #{#entityName} e where e.isActive = true")
  long count();

  @Transactional(readOnly = true)
  default boolean exists(ID id) {
    return findOne(id) != null;
  }

  @Query("update #{#entityName} e set e.isActive=false where e.id = ?1")
  @Transactional
  @Modifying
  void delete(Long id);


  @Override
  @Transactional
  default void delete(T entity) {
    delete(entity.getId());
  }

  @Transactional
  default void delete(Iterable<? extends T> entities) {
    entities.forEach(entitiy -> delete(entitiy.getId()));
  }

  @Override
  @Query("update #{#entityName} e set e.isActive=false")
  @Transactional
  @Modifying
  void deleteAll();
}
