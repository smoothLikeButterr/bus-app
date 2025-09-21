package com.busQR.busApp.reaction;

import com.busQR.busApp.post.Post;
import com.busQR.busApp.post.PostJpaRepository;
import com.busQR.busApp.user.User;
import com.busQR.busApp.user.UserJpaRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostReactionService {

    private final PostReactionJpaRepository reactionRepo;
    private final PostJpaRepository postRepo;
    private final UserJpaRepository userRepo;

    /**
     * 요청된 타입(type)으로 리액션을 토글한다.
     * - 없으면 생성(+1)
     * - 같은 타입이면 삭제(-1)
     * - 다른 타입이면 변경(한쪽 -1, 다른쪽 +1)
     *
     * 동시성:
     * - (post_id, user_id) 유니크 제약으로 중복 insert 방지
     * - 예외시 재조회 후 변경로직 수행
     */
    @Transactional
    public ToggleResult toggle(Long postId, Long userId, ReactionType requested) {
        // 존재 확인
        var existingOpt = reactionRepo.findByPostIdAndUserId(postId, userId);
        if (existingOpt.isEmpty()) {
            // 생성 경로
            return createReaction(postId, userId, requested);
        } else {
            var existing = existingOpt.get();
            if (existing.getType() == requested) {
                // 같은 타입 → 취소(삭제)
                reactionRepo.delete(existing);
                adjustCounter(postId, requested, -1);
                return ToggleResult.removed(requested);
            } else {
                // 다른 타입 → 변경
                ReactionType before = existing.getType();
                existing.setType(requested);
                reactionRepo.save(existing);
                adjustCounter(postId, before, -1);
                adjustCounter(postId, requested, +1);
                return ToggleResult.switched(before, requested);
            }
        }
    }

    private ToggleResult createReaction(Long postId, Long userId, ReactionType requested) {
        Post post = new Post();
        post.setId(postId); // 프록시 참조 (성능 위해 전체 로딩 생략)

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        PostReaction r = new PostReaction();
        r.setPost(post);
        r.setUser(user);
        r.setType(requested);

        try {
            reactionRepo.save(r);
            adjustCounter(postId, requested, +1);
            return ToggleResult.created(requested);
        } catch (PersistenceException e) {
            // 동시에 같은 사용자/글에 생성 충돌 가능성 → 유니크 제약 위반
            if (isUniqueViolation(e)) {
                // 누군가 먼저 생성 → 다시 조회해서 변경 분기 처리
                var existing = reactionRepo.findByPostIdAndUserId(postId, userId)
                        .orElseThrow(EntityExistsException::new);
                if (existing.getType() == requested) {
                    // 이미 같은 타입이면 "결과적으로" 눌린 상태 → 아무 것도 안함
                    return ToggleResult.noop(requested);
                } else {
                    ReactionType before = existing.getType();
                    existing.setType(requested);
                    reactionRepo.save(existing);
                    adjustCounter(postId, before, -1);
                    adjustCounter(postId, requested, +1);
                    return ToggleResult.switched(before, requested);
                }
            }
            throw e;
        }
    }

    private void adjustCounter(Long postId, ReactionType type, int delta) {
        if (type == ReactionType.LIKE) postRepo.incLikeCount(postId, delta);
        else postRepo.incDislikeCount(postId, delta);
    }

    private boolean isUniqueViolation(Throwable e) {
        while (e != null) {
            if (e instanceof ConstraintViolationException) return true;
            e = e.getCause();
        }
        return false;
    }

    // 결과를 표현하는 작은 DTO
    public record ToggleResult(String status, ReactionType before, ReactionType after) {
        public static ToggleResult created(ReactionType t){ return new ToggleResult("CREATED", null, t); }
        public static ToggleResult removed(ReactionType t){ return new ToggleResult("REMOVED", t, null); }
        public static ToggleResult switched(ReactionType b, ReactionType a){ return new ToggleResult("SWITCHED", b, a); }
        public static ToggleResult noop(ReactionType a){ return new ToggleResult("NOOP", a, a); }
    }
}
