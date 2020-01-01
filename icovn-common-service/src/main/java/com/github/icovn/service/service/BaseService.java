package com.github.icovn.service.service;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseService<T, ID extends Serializable> {

  T create(T d);

  T createNow(T d);

  T update(ID id, T d);

  T updateNow(ID id, T d);

  void delete(ID id);

  T find(String filter);

  T findById(ID id);

  T findByIdWithLock(ID id, Class<T> tClass);

  T findOneByField(String field, Object value);

  List<T> findByIds(List<ID> ids);

  Page<T> find(Pageable pageable);

  Page<T> find(String filter, Pageable pageable);

  List<T> findByField(String field, Object value);

  List<T> findByField(String field, List<Object> values);

  Page<T> findByField(String field, Object value, Pageable pageable);

  Page<T> findByIds(List<ID> ids, Pageable pageable);

  long count();
}
