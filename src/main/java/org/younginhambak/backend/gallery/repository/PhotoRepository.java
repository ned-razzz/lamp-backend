package org.younginhambak.backend.gallery.repository;

import org.younginhambak.backend.gallery.entity.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository {

  void save(Photo photo);

  void saveAll(List<Photo> photos);

  Optional<Photo> findById(Long id);

  List<Photo> findByTitle(String title);

  List<Photo> findAll();
}

