package com.busQR.busApp.comment;

import com.busQR.busApp.comment.dto.CommentCreateForm;
import com.busQR.busApp.post.Post;
import com.busQR.busApp.post.PostJpaRepository;
import com.busQR.busApp.user.User;
import com.busQR.busApp.user.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentJpaRepository commentRepo;
    private final PostJpaRepository postRepo;
    private final UserJpaRepository userRepo;

    @Transactional(readOnly = true)
    public List<Comment> listByPost(Long postId) {
        return commentRepo.findByPostIdOrderByCreatedAtAsc(postId);
    }

    @Transactional
    public Long add(Long postId, Long authorId, CommentCreateForm form) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found: " + postId));
        User author = userRepo.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + authorId));

        Comment c = new Comment();
        c.setPost(post);
        c.setAuthor(author);
        c.setContent(form.content());
        if (form.parentId() != null) {
            Comment parent = commentRepo.findById(form.parentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent comment not found: " + form.parentId()));
            c.setParent(parent);
            c.setDepth(parent.getDepth() + 1);
        } else {
            c.setDepth(0);
        }

        // ✅ 카운터 +1 (동시성 안전)
        postRepo.increaseCommentCount(postId, 1);

        return commentRepo.save(c).getId();
    }
}
