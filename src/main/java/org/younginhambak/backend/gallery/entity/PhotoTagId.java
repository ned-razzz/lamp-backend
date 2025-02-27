package org.younginhambak.backend.gallery.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PhotoTagId implements Serializable {
  private Long photoId;
  private Long tagId;
}
