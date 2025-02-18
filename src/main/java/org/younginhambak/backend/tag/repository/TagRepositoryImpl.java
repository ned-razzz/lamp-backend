package org.younginhambak.backend.tag.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.younginhambak.backend.tag.Tag;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository{

  private final EntityManager em;

  @Override
  public Optional<Tag> findByName(String name) {
    Tag tag = em.createQuery("select t from Tag t where t.name = :name", Tag.class)
            .setParameter("name", name)
            .getSingleResult();
    return Optional.of(tag);
  }

  @Override
  public List<Tag> findAll() {
    return em.createQuery("select t from Tag t", Tag.class)
            .getResultList();
  }
}
