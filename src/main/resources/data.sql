-- 유저
INSERT INTO users (email, password_hash, nickname, role, status, created_at, updated_at)
VALUES ('test@local', '{noop}pw', 'tester', 'USER', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 게시판 2종
INSERT INTO board (code, name, description, visibility, sort_order, created_at, updated_at)
VALUES ('NOTICE', '공지사항', '사이트 공지', 'PUBLIC', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO board (code, name, description, visibility, sort_order, created_at, updated_at)
VALUES ('FREE', '자유게시판', '자유롭게 이야기해요', 'PUBLIC', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 샘플 글 (FREE)
INSERT INTO post (board_id, author_id, title, content, status, view_count, like_count, dislike_count, comment_count, created_at, updated_at)
VALUES (
  (SELECT id FROM board WHERE code='FREE'),
  (SELECT id FROM users WHERE email='test@local'),
  '첫 글입니다',
  '안녕하세요! 게시판 MVP 가동 테스트 글이에요.',
  'PUBLISHED', 0, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);




