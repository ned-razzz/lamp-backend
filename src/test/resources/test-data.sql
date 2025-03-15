-- 테이블 데이터 초기화
DELETE FROM _photo_tag;
DELETE FROM _document_tag;
DELETE FROM file;
DELETE FROM photo;
DELETE FROM document;
DELETE FROM tag;
DELETE FROM member;

-- Member 데이터 생성
INSERT INTO member (member_id, name, email, nickname, status, created, updated)
VALUES (1, '홍길동', 'hong@example.com', '관리자', 'ACTIVE', NOW(), NOW());
INSERT INTO member (member_id, name, email, nickname, status, created, updated)
VALUES (2, '김영희', 'kim@example.com', '일반유저', 'ACTIVE', NOW(), NOW());
INSERT INTO member (member_id, name, email, nickname, status, created, updated)
VALUES (3, '이철수', 'lee@example.com', '사진작가', 'ACTIVE', NOW(), NOW());

-- Tag 데이터 생성
INSERT INTO tag (tag_id, name, status, created, updated)
VALUES (1, 'tag1', 'ACTIVE', NOW(), NOW());
INSERT INTO tag (tag_id, name, status, created, updated)
VALUES (2, 'tag2', 'ACTIVE', NOW(), NOW());
INSERT INTO tag (tag_id, name, status, created, updated)
VALUES (3, 'tag3', 'ACTIVE', NOW(), NOW());
INSERT INTO tag (tag_id, name, status, created, updated)
VALUES (4, 'tag4', 'ACTIVE', NOW(), NOW());
INSERT INTO tag (tag_id, name, status, created, updated)
VALUES (5, 'tag5', 'ACTIVE', NOW(), NOW());
INSERT INTO tag (tag_id, name, status, created, updated)
VALUES (6, 'tag6', 'ACTIVE', NOW(), NOW());
INSERT INTO tag (tag_id, name, status, created, updated)
VALUES (7, 'tag7', 'ACTIVE', NOW(), NOW());
INSERT INTO tag (tag_id, name, status, created, updated)
VALUES (8, 'tag8', 'ACTIVE', NOW(), NOW());
INSERT INTO tag (tag_id, name, status, created, updated)
VALUES (9, 'tag9', 'ACTIVE', NOW(), NOW());
INSERT INTO tag (tag_id, name, status, created, updated)
VALUES (10, 'tag10', 'ACTIVE', NOW(), NOW());

-- Document 데이터 생성
INSERT INTO document (document_id, title, description, author_name, status, created, updated, member_id)
VALUES (1, '공지사항 문서', '중요한 공지사항입니다', '홍길동', 'ACTIVE', NOW(), NOW(), 1);
INSERT INTO document (document_id, title, description, author_name, status, created, updated, member_id)
VALUES (2, '회의록', '2023년 3월 정기회의 내용입니다', '김영희', 'ACTIVE', NOW(), NOW(), 2);
INSERT INTO document (document_id, title, description, author_name, status, created, updated, member_id)
VALUES (3, '행사계획서', '여름 행사 계획서입니다', '홍길동', 'ACTIVE', NOW(), NOW(), 1);

-- DocumentTag 데이터 생성 (각 문서마다 여러 태그)
INSERT INTO _document_tag (document_id, tag_id) VALUES (1, 1);  -- 공지사항 문서 - 공지사항 태그
INSERT INTO _document_tag (document_id, tag_id) VALUES (1, 7);  -- 공지사항 문서 - 중요 태그
INSERT INTO _document_tag (document_id, tag_id) VALUES (1, 2);  -- 공지사항 문서 - 자료 태그

INSERT INTO _document_tag (document_id, tag_id) VALUES (2, 2);  -- 회의록 - 자료 태그
INSERT INTO _document_tag (document_id, tag_id) VALUES (2, 6);  -- 회의록 - 회의 태그
INSERT INTO _document_tag (document_id, tag_id) VALUES (2, 4);  -- 회의록 - 양식 태그

INSERT INTO _document_tag (document_id, tag_id) VALUES (3, 4);  -- 행사계획서 - 양식 태그
INSERT INTO _document_tag (document_id, tag_id) VALUES (3, 5);  -- 행사계획서 - 행사 태그
INSERT INTO _document_tag (document_id, tag_id) VALUES (3, 2);  -- 행사계획서 - 자료 태그
INSERT INTO _document_tag (document_id, tag_id) VALUES (3, 7);  -- 행사계획서 - 중요 태그

-- Photo 데이터 생성
INSERT INTO photo (photo_id, title, description, photographer, taken_at, status, created, updated, member_id)
VALUES (1, '행사 사진', '2023년 여름 행사 사진입니다', '이철수', '2023-07-15 14:00:00', 'ACTIVE', NOW(), NOW(), 3);
INSERT INTO photo (photo_id, title, description, photographer, taken_at, status, created, updated, member_id)
VALUES (2, '모임 사진', '정기 모임 단체 사진입니다', '이철수', '2023-06-20 18:30:00', 'ACTIVE', NOW(), NOW(), 3);
INSERT INTO photo (photo_id, title, description, photographer, taken_at, status, created, updated, member_id)
VALUES (3, '회의 현장', '3월 정기회의 현장입니다', '김영희', '2023-03-10 10:00:00', 'ACTIVE', NOW(), NOW(), 2);

-- PhotoTag 데이터 생성 (각 사진마다 여러 태그)
INSERT INTO _photo_tag (photo_id, tag_id) VALUES (1, 3);  -- 행사 사진 - 사진 태그
INSERT INTO _photo_tag (photo_id, tag_id) VALUES (1, 5);  -- 행사 사진 - 행사 태그
INSERT INTO _photo_tag (photo_id, tag_id) VALUES (1, 7);  -- 행사 사진 - 중요 태그

INSERT INTO _photo_tag (photo_id, tag_id) VALUES (2, 3);  -- 모임 사진 - 사진 태그
INSERT INTO _photo_tag (photo_id, tag_id) VALUES (2, 8);  -- 모임 사진 - 모임 태그

INSERT INTO _photo_tag (photo_id, tag_id) VALUES (3, 3);  -- 회의 현장 - 사진 태그
INSERT INTO _photo_tag (photo_id, tag_id) VALUES (3, 6);  -- 회의 현장 - 회의 태그
INSERT INTO _photo_tag (photo_id, tag_id) VALUES (3, 2);  -- 회의 현장 - 자료 태그

-- Single Table 전략에 따라 file 테이블에 DocumentFile과 PhotoFile 모두 저장
-- DocumentFile 데이터 (dtype으로 구분)
INSERT INTO file (file_id, file_name, file_key, extension, status, created, updated, document_id, dtype)
VALUES (1, '공지사항', 'documents/notice_20230101', 'PDF', 'ACTIVE', NOW(), NOW(), 1, 'document');
INSERT INTO file (file_id, file_name, file_key, extension, status, created, updated, document_id, dtype)
VALUES (2, '회의록', 'documents/meeting_20230303', 'DOCX', 'ACTIVE', NOW(), NOW(), 2, 'document');
INSERT INTO file (file_id, file_name, file_key, extension, status, created, updated, document_id, dtype)
VALUES (3, '행사계획서', 'documents/event_plan_20230505', 'PPTX', 'ACTIVE', NOW(), NOW(), 3, 'document');
INSERT INTO file (file_id, file_name, file_key, extension, status, created, updated, document_id, dtype)
VALUES (4, '첨부자료', 'documents/attachment_20230505', 'XLSX', 'ACTIVE', NOW(), NOW(), 3, 'document');

-- PhotoFile 데이터 (dtype으로 구분)
INSERT INTO file (file_id, file_name, file_key, extension, status, created, updated, photo_id, dtype)
VALUES (5, '행사사진', 'photos/event_photo_20230715', 'JPG', 'ACTIVE', NOW(), NOW(), 1, 'photo');
INSERT INTO file (file_id, file_name, file_key, extension, status, created, updated, photo_id, dtype)
VALUES (6, '모임사진', 'photos/meeting_photo_20230620', 'JPG', 'ACTIVE', NOW(), NOW(), 2, 'photo');
INSERT INTO file (file_id, file_name, file_key, extension, status, created, updated, photo_id, dtype)
VALUES (7, '회의현장', 'photos/meeting_scene_20230310', 'PNG', 'ACTIVE', NOW(), NOW(), 3, 'photo');