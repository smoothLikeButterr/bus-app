package com.busQR.busApp.post;

import com.busQR.busApp.board.Board;
import com.busQR.busApp.board.BoardJpaRepository;
import com.busQR.busApp.board.dto.BoardSearchForm;
import com.busQR.busApp.common.PageResult;
import com.busQR.busApp.post.dto.PostCreateForm;
import com.busQR.busApp.user.User;
import com.busQR.busApp.user.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postRepo;
    private final BoardJpaRepository boardRepo;
    private final UserJpaRepository userRepo;

    @Transactional
    public Long create(String boardCode, Long authorId, PostCreateForm form) {
        Board board = boardRepo.findByCode(boardCode)
                .orElseThrow(() -> new EntityNotFoundException("Board not found: " + boardCode));
        User author = userRepo.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + authorId));

        Post post = new Post();
        post.setBoard(board);
        post.setAuthor(author);
        post.setTitle(form.title());
        post.setContent(form.content());
        post.setStatus(PostStatus.PUBLISHED);

        return postRepo.save(post).getId();
    }

    public Post get(Long id) {
        return postRepo.findWithAuthorAndBoard(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found: " + id));
    }

    public List<Post> list(String boardCode, int page, int size) {
        return postRepo.findPageByBoardCodeAndStatus(boardCode, PostStatus.PUBLISHED, page, size);
    }

    public long count(String boardCode) {
        return postRepo.countByBoardCodeAndStatus(boardCode, PostStatus.PUBLISHED);
    }

    public PageResult<Post> searchBoard(String boardCode, BoardSearchForm form, int page, int size) {
        return postRepo.searchByBoardCode(boardCode, form, page, size);
    }
}
