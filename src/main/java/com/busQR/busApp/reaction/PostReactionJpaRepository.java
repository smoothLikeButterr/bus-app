package com.busQR.busApp.reaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class PostReactionJpaRepository {

    @PersistenceContext
    EntityManager em;

    public Optional<PostReaction> findByPostIdAndUserId(Long postId, Long userId) {
        TypedQuery<PostReaction> q = em.createQuery(
                "select r from PostReaction r " +
                        "where r.post.id = :pid and r.user.id = :uid", PostReaction.class);

        q.setParameter("pid", postId);
        q.setParameter("uid", userId);
        return q.getResultStream().findFirst();
    }

    @Transactional
    public PostReaction save(PostReaction r) {
        if (r.getId() == null) {
            em.persist(r);
            return r;
        } else {
            return em.merge(r);
        }
    }

    @Transactional
    public void delete(PostReaction r) {
        em.remove(em.contains(r) ? r : em.merge(r));
    }



}
