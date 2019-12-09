package com.github.icovn.try_custom_repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LinkRepository extends SoftDeleteCrudRepository<Link, Long> {

  Link findByFullUrl(String fullUrl);

  Link save(Link link);

  Link findByShortUrl(String shortUrl);

  @Modifying
  @Query(value = "UPDATE link l set l.click_count = l.click_count + 1 WHERE l.short_url = :shortUrl ", nativeQuery = true)
  @Transactional
  void incrementClickCountByOne(@Param("shortUrl") String shortUrl);
}
