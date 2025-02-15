package org.younginhambak.backend.archive;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.file.DataFile;

@Entity
@DiscriminatorValue("document")
@Getter
@NoArgsConstructor
public class DocumentFile extends DataFile {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "document_id")
  private Document document;
}
