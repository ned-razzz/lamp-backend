package org.younginhambak.backend.tag.repository;

import org.younginhambak.backend.tag.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

  Optional<Tag> findByName(String name);

  List<Tag> findAll();
}
