package org.younginhambak.backend.tag.repository;

import org.younginhambak.backend.tag.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

  Optional<Tag> findByName(String name);
  List<Tag> findByNameIn(List<String> names);
  List<Tag> findAll();
  List<String> findExistingNames(List<String> names);

  List<Tag> findTagsByRelatedDocumentsExists();
  List<Tag> findTagsByRelatedPhotosExists();

}
