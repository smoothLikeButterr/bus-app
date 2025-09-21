package com.busQR.busApp.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class BoardJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Optional<Board> findById(Long id) {
        return Optional.ofNullable(em.find(Board.class, id));
    }

    public Optional<Board> findByCode(String code) {
        try {
            Board b = em.createQuery(
                            "select b from Board b where b.code = :code", Board.class)
                    .setParameter("code", code)
                    .getSingleResult();
            return Optional.of(b);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Board save(Board board) {
        if (board.getId() == null) {
            em.persist(board);
            return board;
        } else {
            return em.merge(board);
        }
    }
}
