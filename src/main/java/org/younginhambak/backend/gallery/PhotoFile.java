package org.younginhambak.backend.gallery;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.file.DataFile;

@Entity
@DiscriminatorValue("photo")
@Getter
@NoArgsConstructor
public class PhotoFile extends DataFile {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "photo_id")
  private Photo photo;
}
