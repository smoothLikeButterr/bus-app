package com.busQR.busApp.post;

import com.busQR.busApp.board.dto.BoardSearchForm;
import com.busQR.busApp.common.PageResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class PostJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(em.find(Post.class, id));
    }

    public Optional<Post> findWithAuthorAndBoard(Long id) {
        try {
            Post p = em.createQuery(
                    "select p from Post p "
                            + "join fetch p.author a "
                            + "join fetch p.board b "
                            + "where p.id = :id", Post.class
            ).setParameter("id", id).getSingleResult();
            return Optional.of(p);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Post save(Post post) {
        if (post.getId() == null) {
            em.persist(post);
            return post;
        } else {
            return em.merge(post);
        }
    }

    public long countByBoardCodeAndStatus(String boardCode, PostStatus status) {
        return em.createQuery(
                        "select count(p) from Post p " +
                                "where p.board.code = :code and p.status = :status", Long.class)
                .setParameter("code", boardCode)
                .setParameter("status", status)
                .getSingleResult();
    }

    public List<Post> findPageByBoardCodeAndStatus(String boardCode, PostStatus status,
                                                   int page, int size) {
        return em.createQuery(
                        "select p from Post p " +
                                "join fetch p.author a " +
                                "join fetch p.board b " +
                                "where b.code = :code and p.status = :status " +
                                "order by p.pinnedUntil desc nulls last, p.createdAt desc", Post.class)
                .setParameter("code", boardCode)
                .setParameter("status", status)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Transactional
    public int incLikeCount(Long postId, int delta) {
        return em.createQuery(
                        "update Post p set p.likeCount = p.likeCount + :d where p.id = :id"
                ).setParameter("d", delta)
                .setParameter("id", postId)
                .executeUpdate();
    }

    @Transactional
    public int incDislikeCount(Long postId, int delta) {
        return em.createQuery(
                        "update Post p set p.dislikeCount = p.dislikeCount + :d where p.id = :id")
                .setParameter("d", delta)
                .setParameter("id", postId)
                .executeUpdate();
    }

    public PageResult<Post> searchByBoardCode(
            String boardCode,
            BoardSearchForm form,
            int page, int size
    ) {
        StringBuilder jpql = new StringBuilder();
        StringBuilder countJpql = new StringBuilder();

        // 기본 FROM / JOIN
        String baseFrom =
                " from Post p " +
                        " join p.board b " +
                        " join p.author a " +
                        " where b.code = :code and p.status = :status ";

        // 동적 WHERE
        Map<String, Object> params = new HashMap<>();
        params.put("code", boardCode);
        params.put("status", PostStatus.PUBLISHED);


        String CONTENT_LOWER = "lower(cast(p.content as string))";

        if (form != null) {
            if (form.getQuery() != null && !form.getQuery().isBlank()) {
                String q = "%" + form.getQuery().trim().toLowerCase() + "%";
                params.put("q", q);

                BoardSearchForm.Field f = form.getField();
                if (f == BoardSearchForm.Field.TITLE) {
                    baseFrom += " and lower(p.title) like :q ";
                } else if (f == BoardSearchForm.Field.CONTENT) {
                    baseFrom += " and " + CONTENT_LOWER + " like :q ";
                } else if (f == BoardSearchForm.Field.TITLE_CONTENT) {
                    baseFrom += " and (lower(p.title) like :q or " + CONTENT_LOWER + " like :q) ";
                } else if (f == BoardSearchForm.Field.AUTHOR) {
                    baseFrom += " and lower(a.nickname) like :q ";
                }
            }

            if (form.getAuthor() != null && !form.getAuthor().isBlank()) {
                params.put("author", "%" + form.getAuthor().trim().toLowerCase() + "%");
                baseFrom += " and lower(a.nickname) like :author ";
            }
            if (form.getFromDate() != null) {
                params.put("fromDt", form.getFromDate().atStartOfDay());
                baseFrom += " and p.createdAt >= :fromDt ";
            }
            if (form.getToDate() != null) {
                params.put("toDt", form.getToDate().plusDays(1).atStartOfDay());
                baseFrom += " and p.createdAt < :toDt ";
            }
        }

//        // 검색어
//        if (form != null) {
//            if (form.getQuery() != null && !form.getQuery().isBlank()
//                    && (form.getField() == BoardSearchForm.Field.TITLE
//                    || form.getField() == BoardSearchForm.Field.CONTENT
//                    || form.getField() == BoardSearchForm.Field.TITLE_CONTENT)) {
//                String q = "%" + form.getQuery().trim().toLowerCase() + "%";
//                params.put("q", q);
//                if (form.getField() == BoardSearchForm.Field.TITLE) {
//                    baseFrom += " and lower(p.title) like :q ";
//                } else if (form.getField() == BoardSearchForm.Field.CONTENT) {
//                    baseFrom += " and lower(p.content) like :q ";
//                } else {
//                    baseFrom += " and (lower(p.title) like :q or lower(p.content) like :q) ";
//                }
//            }
//
//            // 작성자 닉네임
//            if (form.getAuthor() != null && !form.getAuthor().isBlank()) {
//                params.put("author", "%" + form.getAuthor().trim().toLowerCase() + "%");
//                baseFrom += " and lower(a.nickname) like :author ";
//            }
//
//            // 날짜 범위 (작성일: createdAt 기준)
//            if (form.getFromDate() != null) {
//                params.put("fromDt", form.getFromDate().atStartOfDay());
//                baseFrom += " and p.createdAt >= :fromDt ";
//            }
//            if (form.getToDate() != null) {
//                // inclusive 위해 다음날 0시 미만
//                params.put("toDt", form.getToDate().plusDays(1).atStartOfDay());
//                baseFrom += " and p.createdAt < :toDt ";
//            }
//        }

        // 핀 고정 우선 + 정렬키별 보조 정렬
        BoardSearchForm.SortKey sortKey = (form != null && form.getSort() != null) ? form.getSort() : BoardSearchForm.SortKey.LATEST;

        String orderBy;
        switch (sortKey) {
            case LIKES:
                orderBy = " order by p.pinnedUntil desc, p.likeCount desc, p.createdAt desc ";
                break;
            case COMMENTS:
                orderBy = " order by p.pinnedUntil desc, p.commentCount desc, p.createdAt desc ";
                break;
            case VIEWS:
                orderBy = " order by p.pinnedUntil desc, p.viewCount desc, p.createdAt desc ";
                break;
            case LATEST:
            default:
                orderBy = " order by p.pinnedUntil desc, p.createdAt desc ";
                break;
        }

        // 목록 JPQL
        jpql.append("select p ").append(baseFrom).append(orderBy);
        // 카운트 JPQL
        countJpql.append("select count(p.id) ").append(baseFrom);

        // 실행
        TypedQuery<Post> query = em.createQuery(jpql.toString(), Post.class);
        TypedQuery<Long> countQuery = em.createQuery(countJpql.toString(), Long.class);

        params.forEach((k, v) -> {
            query.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        List<Post> content = query.getResultList();
        long total = countQuery.getSingleResult();

        // 저자/보드 지연로딩이 필요하면 fetch join으로 바꾸거나, 여기서 초기화
        // (지금은 목록에서 저자 닉네임/보드명 정도만 쓴다면 fetch join 권장)

        return new PageResult<>(content, total, page, size);

    }

    @Transactional
    public void increaseLikeCount(Long postId, int delta) {
        em.createQuery(
                        "update Post p set p.likeCount = p.likeCount + :d where p.id = :id"
                ).setParameter("d", delta)
                .setParameter("id", postId)
                .executeUpdate();

        // 벌크 이후 1차 캐시 불일치 방지
        em.clear();
    }


    @Transactional
    public void increaseCommentCount(Long postId, int delta) {
        em.createQuery(
                        "update Post p set p.commentCount = p.commentCount + :d where p.id = :id"
                ).setParameter("d", delta)
                .setParameter("id", postId)
                .executeUpdate();

        // 벌크 업데이트 후 1차 캐시와 불일치 방지
        em.clear();
    }

}
