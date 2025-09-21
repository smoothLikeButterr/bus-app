package com.busQR.busApp.comment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class CommentJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Transactional
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            em.persist(comment);
            return comment;
        }
        else {
            return em.merge(comment);
        }
    }

    public List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId) {
        return em.createQuery(
                "select c from Comment c " +
                "join fetch c.author a " +
                "where c.post.id = :postId " +
                "order by c.createdAt asc", Comment.class
        ).setParameter("postId", postId).getResultList();
    }
}
