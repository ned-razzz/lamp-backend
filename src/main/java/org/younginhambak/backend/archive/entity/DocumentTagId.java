package org.younginhambak.backend.archive.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTagId implements Serializable {

  private Long documentId;
  private Long tagId;
}
