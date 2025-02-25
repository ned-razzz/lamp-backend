package org.younginhambak.backend.gallery;

import org.junit.jupiter.api.Test;
import org.younginhambak.backend.file.entity.FileExtension;
import org.younginhambak.backend.file.entity.PhotoFile;
import org.younginhambak.backend.member.Member;
import org.younginhambak.backend.tag.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PhotoTest {

  @Test
  public void 사진추가() throws Exception {
    //given
    Member member1 = Member.create("member1", "member@gmail.com", "nickname1");
    PhotoFile photoFile = PhotoFile.create("file1", "url1", FileExtension.JPG);
    ArrayList<PhotoTag> photoTags = createPhotoTags();

    //when
    Photo photo = createPhoto(member1, photoFile, photoTags);

    //then
    assertThat(photo.getMember().getPhotos()).contains(photo);
    assertThat(photo.getFile().getPhoto()).isEqualTo(photo);
    assertThat(photo.getPhotoTags().size()).isEqualTo(2);
    assertThat(photo.getPhotoTags().get(0).getPhoto()).isEqualTo(photo);
  }

  @Test
  public void 사진수정() throws Exception {
    //given
    Member member1 = Member.create("member1", "member@gmail.com", "nickname1");
    PhotoFile photoFile = PhotoFile.create("file1", "url1", FileExtension.JPG);
    ArrayList<PhotoTag> photoTags = createPhotoTags();
    Photo photo = createPhoto(member1, photoFile, photoTags);

    //when
    Member member2 = Member.create("member2", "member2@gmail.com", "nickname2");
    PhotoFile photoFile2 = PhotoFile.create("file2", "url2", FileExtension.PNG);

    Tag tag3 = Tag.create("tag3");
    PhotoTag photoTag3 = PhotoTag.create(tag3);

    ArrayList<PhotoTag> photoTagsU = new ArrayList<>();
    photoTagsU.add(photoTag3);
    photoTagsU.add(photoTags.get(1));

    photo.update(
            "titleU",
            "descriptionU",
            "grapherU",
            LocalDateTime.now(),
            member2,
            photoFile2,
            photoTagsU
            );

    //then
    assertThat(photo.getMember()).isEqualTo(member2);

    assertThat(member1.getPhotos()).doesNotContain(photo);
    assertThat(photo.getPhotoTags()).containsAll(photoTagsU);
    assertThat(photoTags.get(0).getPhoto()).isNull();

    assertThat(photo.getFile().getPhoto()).isEqualTo(photo);
    assertThat(photoFile2.getPhoto()).isEqualTo(photo);
    assertThat(photoFile.getPhoto()).isNull();
  }

  @Test
  public void 사진삭제() throws Exception {
    //given
    Member member1 = Member.create("member1", "member@gmail.com", "nickname1");
    PhotoFile photoFile = PhotoFile.create("file1", "url1", FileExtension.JPG);
    ArrayList<PhotoTag> photoTags = createPhotoTags();
    Photo photo = createPhoto(member1, photoFile, photoTags);

    //when
    photo.delete();

    //then
    assertThat(photo.getStatus()).isEqualTo(PhotoStatus.DELETED);
    assertThat(member1.getPhotos()).doesNotContain(photo);
    assertThat(photoFile.getPhoto()).isNull();
    photoTags.forEach(photoTag -> {
      assertThat(photoTag.getPhoto()).isNull();
    });

  }


  public Photo createPhoto(Member member, PhotoFile file, List<PhotoTag> photoTags) {

    return Photo.create(
            "title1",
            "description1",
            "grapher1",
            LocalDateTime.now(),
            member,
            file,
            photoTags
    );
  }

  private static ArrayList<PhotoTag> createPhotoTags() {
    Tag tag1 = Tag.create("tag1");
    Tag tag2 = Tag.create("tag2");
    PhotoTag photoTag1 = PhotoTag.create(tag1);
    PhotoTag photoTag2 = PhotoTag.create(tag2);

    ArrayList<PhotoTag> photoTags = new ArrayList<>();
    photoTags.add(photoTag1);
    photoTags.add(photoTag2);
    return photoTags;
  }

}