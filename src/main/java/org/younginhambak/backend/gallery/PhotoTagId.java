package org.younginhambak.backend.gallery;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class PhotoTagId implements Serializable {
  private Long photoId;
  private Long tagId;
}
